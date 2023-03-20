package work.lclpnet.kibu.plugin.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.KibuCommands;
import work.lclpnet.mplugins.ext.Unloadable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandContainer implements CommandRegistrar, Unloadable {

    private final Object mutex = new Object();
    private final List<LiteralCommandNode<ServerCommandSource>> commands = new ArrayList<>();

    @Override
    public CompletableFuture<LiteralCommandNode<ServerCommandSource>> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
        return KibuCommands.register(command).thenApply(cmd -> {
            registerCommand0(cmd);
            return cmd;
        });
    }

    private void registerCommand0(LiteralCommandNode<ServerCommandSource> cmd) {
        synchronized (mutex) {
            commands.add(cmd);
        }
    }

    @Override
    public void unregisterCommand(LiteralCommandNode<ServerCommandSource> command) {
        synchronized (mutex) {
            KibuCommands.unregister(command);
            commands.remove(command);
        }
    }

    @Override
    public void unload() {
        List<LiteralCommandNode<ServerCommandSource>> tmp;

        synchronized (mutex) {
            tmp = new ArrayList<>(commands);
        }

        tmp.forEach(this::unregisterCommand);
    }
}
