package dragon.me.Aegis.commands;

import com.github.retrooper.packetevents.PacketEvents;
import dragon.me.Aegis.commands.subcommands.AegisReload;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.ReloadCommand;
import org.jetbrains.annotations.NotNull;

public class AegisMainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (s.equalsIgnoreCase("aegis")){
            if (commandSender.hasPermission("aegis.admin")){
                AegisReload.reload(PacketEvents.getAPI().getPlayerManager().getUser(commandSender));

            }
        }


        return true;
    }
}
