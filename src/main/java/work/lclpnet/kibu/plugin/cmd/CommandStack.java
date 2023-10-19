package work.lclpnet.kibu.plugin.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandReference;
import work.lclpnet.kibu.plugin.util.UnloadableStack;

public class CommandStack extends UnloadableStack<CommandContainer> implements CommandRegistrar {

    public CommandStack() {
        super(CommandContainer::new);
    }

    @Override
    public CommandReference<ServerCommandSource> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
        return current().registerCommand(command);
    }

    @Override
    public CommandReference<ServerCommandSource> registerCommand(CommandFactory<ServerCommandSource> factory) {
        return current().registerCommand(factory);
    }

    @Override
    public void unregisterCommand(LiteralCommandNode<ServerCommandSource> command) {
        current().unregisterCommand(command);
    }
}
