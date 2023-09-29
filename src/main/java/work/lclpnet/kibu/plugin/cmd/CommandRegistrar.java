package work.lclpnet.kibu.plugin.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.type.CommandFactory;
import work.lclpnet.kibu.cmd.type.CommandReference;

public interface CommandRegistrar {

    CommandReference<ServerCommandSource> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command);

    CommandReference<ServerCommandSource> registerCommand(CommandFactory<ServerCommandSource> factory);

    void unregisterCommand(LiteralCommandNode<ServerCommandSource> command);
}
