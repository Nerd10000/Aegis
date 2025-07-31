package dragon.me.sentinelAC.checks;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import dragon.me.sentinelAC.SentinelAC;
import dragon.me.sentinelAC.data.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Check {


    private  String checkName;
    public Check(String checkName){

            this.checkName = checkName;
    }



    public String getCheckName() {
        return checkName;
    }

    public FileConfiguration getConfig(){
        return SentinelAC.getPlugin().getConfig();
    }
}
