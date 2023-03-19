package work.lclpnet.kibu.plugin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.plugin.cmd.CommandContainer;
import work.lclpnet.kibu.plugin.cmd.CommandRegistrar;
import work.lclpnet.kibu.plugin.hook.HookContainer;
import work.lclpnet.kibu.plugin.hook.HookListenerModule;
import work.lclpnet.kibu.plugin.hook.HookRegistrar;
import work.lclpnet.kibu.scheduler.KibuScheduling;
import work.lclpnet.kibu.scheduler.api.Scheduler;
import work.lclpnet.mplugins.ext.FabricPlugin;
import work.lclpnet.mplugins.ext.Unloadable;
import work.lclpnet.mplugins.util.MPluginsLoggerSupplier;
import work.lclpnet.mplugins.util.PluginLoggerSupplier;

public class KibuPlugin extends FabricPlugin implements PluginContext {

	private final HookRegistrar hookRegistrar;
	private final CommandRegistrar commandRegistrar;
	private Scheduler scheduler = null;

	public KibuPlugin() {
		this(new HookContainer(), new CommandContainer());
	}

	public KibuPlugin(PluginLoggerSupplier loggerSupplier) {
		this(new HookContainer(), new CommandContainer(), loggerSupplier);
	}

	public KibuPlugin(HookRegistrar hookRegistrar, CommandRegistrar commandRegistrar) {
		this(hookRegistrar, commandRegistrar, new MPluginsLoggerSupplier());
	}

	public KibuPlugin(HookRegistrar hookRegistrar, CommandRegistrar commandRegistrar, PluginLoggerSupplier loggerSupplier) {
		super(loggerSupplier);

		this.hookRegistrar = hookRegistrar;
		this.commandRegistrar = commandRegistrar;

		registerUnloadable(hookRegistrar);
		registerUnloadable(commandRegistrar);
	}

	@Override
	protected final void loadFabricPlugin() {
		super.loadFabricPlugin();

		scheduler = new Scheduler(getLogger());
		KibuScheduling.getRootScheduler().addChild(scheduler);

		loadKibuPlugin();
	}

	@Override
	protected final void unloadFabricPlugin() {
		unloadKibuPlugin();

		if (scheduler != null) {
			KibuScheduling.getRootScheduler().removeChild(scheduler);
		}

		super.unloadFabricPlugin();
	}

	protected void loadKibuPlugin() {
		// no-op
	}

	protected void unloadKibuPlugin() {
		// no-op
	}

	@Override
	public <T> void registerHook(Hook<T> hook, T listener) {
		hookRegistrar.registerHook(hook, listener);
	}

	@Override
	public <T> void unregisterHook(Hook<T> hook, T listener) {
		hookRegistrar.unregisterHook(hook, listener);
	}

	@Override
	public void registerHooks(HookListenerModule hooks) {
		hookRegistrar.registerHooks(hooks);
	}

	@Override
	public LiteralCommandNode<ServerCommandSource> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
		return commandRegistrar.registerCommand(command);
	}

	@Override
	public void unregisterCommand(LiteralCommandNode<ServerCommandSource> command) {
		commandRegistrar.unregisterCommand(command);
	}

	@Override
	public Scheduler getScheduler() {
		if (scheduler == null) throw new IllegalStateException("Not initialized");
		return scheduler;
	}

	private void registerUnloadable(Object maybeUnloadable) {
		if (maybeUnloadable instanceof Unloadable unloadable) {
			registerUnloadable(unloadable);
		}
	}
}