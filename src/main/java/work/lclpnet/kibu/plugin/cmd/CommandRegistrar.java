package work.lclpnet.kibu.plugin.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.type.CommandFactory;

import java.util.concurrent.CompletableFuture;

public interface CommandRegistrar {

    CompletableFuture<LiteralCommandNode<ServerCommandSource>> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command);

    CompletableFuture<LiteralCommandNode<ServerCommandSource>> registerCommand(CommandFactory<ServerCommandSource> factory);

    void unregisterCommand(LiteralCommandNode<ServerCommandSource> command);
}
