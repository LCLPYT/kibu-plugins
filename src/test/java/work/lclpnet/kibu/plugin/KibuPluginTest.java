package work.lclpnet.kibu.plugin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.util.CommandDispatcherUtils;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.kibu.plugin.ext.KibuPlugin;
import work.lclpnet.kibu.plugin.ext.PluginContext;
import work.lclpnet.kibu.scheduler.KibuScheduling;
import work.lclpnet.mplugins.ext.Unloadable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

        var cmd = plugin.registerCommand(literal("test")).join();
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
        public CompletableFuture<LiteralCommandNode<ServerCommandSource>> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
            return commandRegister.register(command);
        }

        @Override
        public void unregisterCommand(LiteralCommandNode<ServerCommandSource> command) {
            commandRegister.unregister(command);
        }
    }

    private static class TestRegister<S> implements CommandRegister<S>, Unloadable {

        private final CommandDispatcher<S> dispatcher = new CommandDispatcher<>();
        private final List<LiteralCommandNode<S>> commands = new ArrayList<>();

        @Override
        public CompletableFuture<LiteralCommandNode<S>> register(LiteralArgumentBuilder<S> command) {
            var cmd = CommandDispatcherUtils.register(dispatcher, command);
            commands.add(cmd);
            return CompletableFuture.completedFuture(cmd);
        }

        @Override
        public boolean unregister(LiteralCommandNode<S> command) {
            CommandDispatcherUtils.unregister(dispatcher, command);
            commands.remove(command);
            return true;  // assume unregistered
        }

        @Override
        public void unload() {
            commands.forEach(this::unregister);
        }
    }
}
