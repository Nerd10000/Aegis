package dragon.me.Aegis.data.interceptors;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import dragon.me.Aegis.utils.PlayerDataManager;

import java.util.UUID;

public class CombatInterceptor implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        UUID uuid = event.getUser().getUUID();

        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY){
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            if (wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK){
                PlayerDataManager.dataMap.get(uuid).recordClicks();
                PlayerDataManager.dataMap.get(uuid).addCurrentCpsToList();

                // Only clear after the checks have processed the data
                if (PlayerDataManager.dataMap.get(uuid).getCpsList().size() > 100) {
                    PlayerDataManager.dataMap.get(uuid).getCpsList().clear();
                }
            }
        }
    }
}