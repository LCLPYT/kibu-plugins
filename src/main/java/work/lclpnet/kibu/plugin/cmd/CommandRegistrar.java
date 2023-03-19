package work.lclpnet.kibu.plugin.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandRegistrar {

    LiteralCommandNode<ServerCommandSource> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command);

    void unregisterCommand(LiteralCommandNode<ServerCommandSource> command);
}
