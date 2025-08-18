package dragon.me.Aegis.data.interceptors;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;

import dragon.me.Aegis.utils.PlayerDataManager;
import org.bukkit.entity.Player;

public class ActionInterceptor implements PacketListener {


    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION){
            Player player = event.getPlayer();
            WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(event);
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setGameMode(player.getGameMode());
            switch (wrapper.getAction()) {
                case STOP_SNEAKING:
                    PlayerDataManager.dataMap.get(event.getUser().getUUID()).setAction(Action.STOP_SNEAKING);
                    break;
                case START_SNEAKING:

                                                       
                default:
                    break;
            }


        }
    }
}
