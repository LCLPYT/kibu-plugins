package work.lclpnet.kibu.plugin.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.KibuCommands;
import work.lclpnet.mplugins.ext.Unloadable;

import java.util.ArrayList;
import java.util.List;

public class CommandContainer implements CommandRegistrar, Unloadable {

    private final Object mutex = new Object();
    private final List<LiteralCommandNode<ServerCommandSource>> commands = new ArrayList<>();

    @Override
    public LiteralCommandNode<ServerCommandSource> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
        synchronized (mutex) {
            var cmd = KibuCommands.register(command);
            commands.add(cmd);
            return cmd;
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
        var tmp = new ArrayList<>(commands);
        tmp.forEach(this::unregisterCommand);
    }
}
