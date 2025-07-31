package dragon.me.sentinelAC.checks.combat.clicks;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDamageEvent;
import dragon.me.sentinelAC.checks.Check;
import dragon.me.sentinelAC.data.PlayerData;
import dragon.me.sentinelAC.utils.PlayerDataManager;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MaxClicks extends Check implements PacketListener {


    public MaxClicks(String checkName) {
        super(checkName);
    }

    int id;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY){
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            id = wrapper.getEntityId();
            if (wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK){

                PlayerData data = PlayerDataManager.dataMap.get(event.getUser().getUUID());
                if (data.getCps() > getConfig().getInt("autoclicker_thresholds.max")){
                    data.alert(getCheckName(),"cps="+data.getCps(),10);
                    Entity e = SpigotConversionUtil.getEntityById(data.getWorld(),wrapper.getEntityId());
                    if (e instanceof  Player p){
                        p.damage(0);
                    }
                }
            }
        }


    }


}
