package dragon.me.Aegis.checks.combat.clicks;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import dragon.me.Aegis.checks.Check;
import dragon.me.Aegis.data.PlayerData;
import dragon.me.Aegis.utils.PlayerDataManager;
import dragon.me.Aegis.utils.math.StatisticsUtil;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class VarianceClicks extends Check implements PacketListener {
    public VarianceClicks(String checkName) {
        super(checkName);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY){
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            if (wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK){
                PlayerData data = PlayerDataManager.dataMap.get(event.getUser().getUUID());
                 List<Integer> copy = data.getCpsList();
                if (copy.size() >= 20){
                    Bukkit.getLogger().info("Variance workin! (full)");
                    double variance = StatisticsUtil.variance(copy);

                    if (variance < getConfig().getDouble("autoclicker_thresholds.min_variance") || variance > getConfig().getDouble("autoclicker_thresholds.max_variance")){
                        data.alert(getCheckName(),"variance="+variance,3);
                        Entity e = SpigotConversionUtil.getEntityById(data.getWorld(),wrapper.getEntityId());
                        if (e instanceof  Player p){
                            p.damage(0);
                        }
                    }

                }

            }
        }
    }
}
