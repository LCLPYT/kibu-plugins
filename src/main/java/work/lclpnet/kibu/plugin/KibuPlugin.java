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
import work.lclpnet.mplugins.ext.FabricPlugin;

public class KibuPlugin extends FabricPlugin implements HookRegistrar, CommandRegistrar {

	private final HookRegistrar hookRegistrar;
	private final CommandRegistrar commandRegistrar;

	public KibuPlugin() {
		this(new HookContainer(), new CommandContainer());
	}

	public KibuPlugin(HookRegistrar hookRegistrar, CommandRegistrar commandRegistrar) {
		this.hookRegistrar = hookRegistrar;
		this.commandRegistrar = commandRegistrar;
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
	public void unregisterAllHooks() {
		hookRegistrar.unregisterAllHooks();
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
	public void unregisterAllCommands() {
		commandRegistrar.unregisterAllCommands();
	}
}