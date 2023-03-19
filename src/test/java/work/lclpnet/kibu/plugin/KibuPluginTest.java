package work.lclpnet.kibu.plugin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.cmd.KibuCommands;
import work.lclpnet.kibu.cmd.type.CommandRegister;
import work.lclpnet.kibu.cmd.util.CommandDispatcherUtils;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.kibu.scheduler.KibuScheduling;

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
        plugin.load();

        var register = new TestRegister<ServerCommandSource>();
        new KibuCommands<>(register).onInitialize();

        var cmd = plugin.registerCommand(literal("test"));
        var registered = register.dispatcher.getRoot().getChild("test");

        assertNotNull(cmd);
        assertEquals(registered, cmd);

        plugin.unload();

        registered = register.dispatcher.getRoot().getChild("test");
        assertNull(registered);
    }

    private static class TestPlugin extends KibuPlugin {

        AtomicBoolean executed;

        private TestPlugin() {
            super(x -> logger);
        }

        @Override
        protected void loadKibuPlugin() {
            registerHook(TEST_HOOK, () -> executed.set(true));
        }
    }

    private static class TestRegister<S> implements CommandRegister<S> {

        private final CommandDispatcher<S> dispatcher = new CommandDispatcher<>();

        @Override
        public LiteralCommandNode<S> register(LiteralArgumentBuilder<S> command) {
            return CommandDispatcherUtils.register(dispatcher, command);
        }

        @Override
        public void unregister(LiteralCommandNode<S> command) {
            CommandDispatcherUtils.unregister(dispatcher, command);
        }
    }
}
