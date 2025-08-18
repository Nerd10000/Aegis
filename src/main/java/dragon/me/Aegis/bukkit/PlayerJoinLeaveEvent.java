package dragon.me.Aegis.bukkit;

import com.github.retrooper.packetevents.util.Vector3d;
import dragon.me.Aegis.data.PlayerData;
import dragon.me.Aegis.utils.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveEvent implements Listener {
    @EventHandler
    public void onEvent(org.bukkit.event.player.PlayerJoinEvent e){
        PlayerDataManager.dataMap.put(e.getPlayer().getUniqueId(), new PlayerData(e.getPlayer().getUniqueId()));
        PlayerDataManager.dataMap.get(e.getPlayer().getUniqueId()).setDeltas(new Vector3d());
        PlayerDataManager.dataMap.get(e.getPlayer().getUniqueId()).setLastDeltas(new Vector3d());

        PlayerDataManager.dataMap.get(e.getPlayer().getUniqueId()).setVl(0);

    }
    @EventHandler
    public void onEventLeave(PlayerQuitEvent e){
        PlayerDataManager.dataMap.remove(e.getPlayer().getUniqueId());

    }
}
