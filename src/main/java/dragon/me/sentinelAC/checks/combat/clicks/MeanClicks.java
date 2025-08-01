package dragon.me.sentinelAC.checks.combat.clicks;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDamageEvent;
import dragon.me.sentinelAC.checks.Check;
import dragon.me.sentinelAC.data.PlayerData;
import dragon.me.sentinelAC.utils.PlayerDataManager;
import dragon.me.sentinelAC.utils.math.StatisticsUtil;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class MeanClicks extends Check implements PacketListener {
    public MeanClicks(String checkName) {
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
                    data.getIsFinishedArray()[0] = false;

                    double mean = StatisticsUtil.meanInt(copy);
                    if (mean > getConfig().getDouble("autoclicker_thresholds.max_mean")){
                        data.alert(getCheckName(),"mean="+mean,3);
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
