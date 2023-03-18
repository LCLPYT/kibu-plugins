package work.lclpnet.kibu.plugin.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.kibu.cmd.KibuCommands;

import java.util.ArrayList;
import java.util.List;

public class CommandContainer implements CommandRegistrar {

    private final List<LiteralCommandNode<ServerCommandSource>> commands = new ArrayList<>();

    @Override
    public LiteralCommandNode<ServerCommandSource> registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
        var cmd = KibuCommands.register(command);
        commands.add(cmd);
        return cmd;
    }

    @Override
    public void unregisterCommand(LiteralCommandNode<ServerCommandSource> command) {
        KibuCommands.unregister(command);
        commands.remove(command);
    }

    @Override
    public void unregisterAllCommands() {
        commands.forEach(this::unregisterCommand);
    }
}
