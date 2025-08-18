package dragon.me.Aegis.commands.subcommands;

import com.github.retrooper.packetevents.protocol.player.User;
import dragon.me.Aegis.Aegis;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;

public class AegisReload {


    public static void reload(User u){
        Aegis.getPlugin().reloadConfig();
        u.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Aegis.getPlugin().getConfig().getString("reload-message")
                .replace("%prefix%",Aegis.getPlugin().getConfig().getString("prefix"))));
    }
}
