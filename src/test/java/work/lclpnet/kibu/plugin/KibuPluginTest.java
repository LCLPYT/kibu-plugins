package work.lclpnet.kibu.plugin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.cmd.type.*;
import work.lclpnet.kibu.cmd.type.impl.DynamicCommandReference;
import work.lclpnet.kibu.cmd.util.CommandDispatcherUtils;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.kibu.plugin.ext.KibuPlugin;
import work.lclpnet.kibu.plugin.ext.PluginContext;
import work.lclpnet.kibu.scheduler.KibuScheduling;
import work.lclpnet.mplugins.ext.Unloadable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static org.junit.jupiter.api.Assertions.*;

public class KibuPluginTest {

    private static final Hook<Runnable> TEST_HOOK = HookFactory.createArrayBacked(Runnable.class, runnables -> () -> {
        for (var runnable : runnables) {
            runnable.run();
        }
    });

    private static final Logger logger = LoggerFactory.getLogger(KibuPluginTest.class);

    @Test
    void testHookUnRegistration() {
        final var plugin = new TestPlugin();
        final var executed = new AtomicBoolean(false);

        plugin.executed = executed;
        plugin.load();

        TEST_HOOK.invoker().run();
        assertTrue(executed.get());

        plugin.unload();

        executed.set(false);
        TEST_HOOK.invoker().run();

        assertFalse(executed.get());
    }

    @Test
    void testSchedulerUnRegistration() {
        final var plugin = new TestPlugin();
        final var executed = new AtomicBoolean(false);

        plugin.load();

        plugin.getScheduler().immediate(() -> executed.set(true));

        KibuScheduling.getRootScheduler().tick();
        assertTrue(executed.get());

        plugin.unload();

        executed.set(false);
        plugin.getScheduler().immediate(() -> executed.set(true));
        KibuScheduling.getRootScheduler().tick();
        assertFalse(executed.get());
    }

    @Test
    void testCommandUnRegistration() {
        final var plugin = new TestPlugin();
        final var register = new TestRegister<ServerCommandSource>();
        plugin.commandRegister = register;
        plugin.registerUnloadable(register);
        plugin.load();

        var cmd = plugin.registerCommand(literal("test")).getCommand().orElseThrow();
        var registered = register.dispatcher.getRoot().getChild("test");

        assertNotNull(cmd);
        assertEquals(registered, cmd);

        plugin.unload();

        registered = register.dispatcher.getRoot().getChild("test");
        assertNull(registered);
    }

    @Test
    void testCommandFactoryUnRegistration() {
        final var plugin = new TestPlugin();
        final var register = new TestRegister<ServerCommandSource>();
        plugin.commandRegister = register;
        plugin.registerUnloadable(register);
        plugin.load();

        var cmd = plugin.registerCommand(context -> literal("test")).getCommand().orElseThrow();
        var registered = register.dispatcher.getRoot().getChild("test");

        assertNotNull(cmd);
        assertEquals(registered, cmd);

        plugin.unload();

        registered = register.dispatcher.getRoot().getChild("test");
        assertNull(registered);
    }

    @Test
    void testEnvironment() {
        final var pluginContext = (PluginContext) new TestPlugin();
        assertNotNull(pluginContext.getEnvironment());
    }

    private static class TestPlugin extends KibuPlugin {

        TestRegister<ServerCommandSource> commandRegister;
        AtomicBoolean executed = new AtomicBoolean(false);

        private TestPlugin() {
            super(x -> logger);
        }

        @Override
        protected void loadKibuPlugin() {
            registerHook(TEST_HOOK, () -> executed.set(true));
        }

        @Override
        public CommandReference<ServerCommandSource> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
            var ref = new DynamicCommandReference<>(this::unregisterCommand);
            commandRegister.register(command, ref);
            return ref;
        }

        @Override
        public CommandReference<ServerCommandSource> registerCommand(CommandFactory<ServerCommandSource> factory) {
            var ref = new DynamicCommandReference<>(this::unregisterCommand);
            commandRegister.register(factory, ref);
            return ref;
        }

        @Override
        public void unregisterCommand(LiteralCommandNode<ServerCommandSource> command) {
            commandRegister.unregister(command);
        }
    }

    private static class TestRegister<S> implements CommandRegister<S>, Unloadable, CommandRegistrationContext, CommandRegistryAccess {

        private final CommandDispatcher<S> dispatcher = new CommandDispatcher<>();
        private final List<LiteralCommandNode<S>> commands = new ArrayList<>();

        @Override
        public boolean register(LiteralArgumentBuilder<S> command, CommandConsumer<S> consumer) {
            var cmd = CommandDispatcherUtils.register(dispatcher, command);
            commands.add(cmd);
            consumer.acceptCommand(cmd);
            return true;
        }

        @Override
        public boolean register(CommandFactory<S> factory, CommandConsumer<S> consumer) {
            var command = factory.create(this);
            return register(command, consumer);
        }

        @Override
        public boolean unregister(LiteralCommandNode<S> command) {
            CommandDispatcherUtils.unregister(dispatcher, command);
            commands.remove(command);
            return true;  // assume unregistered
        }

        @Override
        public void unload() {
            new ArrayList<>(commands).forEach(this::unregister);
        }

        @Override
        public CommandRegistryAccess registryAccess() {
            return this;
        }

        @Override
        public CommandManager.RegistrationEnvironment environment() {
            return CommandManager.RegistrationEnvironment.DEDICATED;
        }

        @Override
        public <T> RegistryWrapper<T> createWrapper(RegistryKey<? extends Registry<T>> registryRef) {
            return null;
        }
    }
}
