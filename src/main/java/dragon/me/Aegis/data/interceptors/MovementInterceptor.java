package dragon.me.Aegis.data.interceptors;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import dragon.me.Aegis.utils.PlayerDataManager;
import org.bukkit.entity.Player;

public class MovementInterceptor implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())){
            Player player = event.getPlayer();
            WrapperPlayServerPing ping = new WrapperPlayServerPing((int) Math.random() * 100000);

            PlayerDataManager.dataMap.get(player.getUniqueId()).setLastOnGround(PlayerDataManager.dataMap.get(player.getUniqueId()).isOnGround());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setWorld(player.getWorld());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setLastSprinting(PlayerDataManager.dataMap.get(player.getUniqueId()).isSprinting());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setLastSneaking(PlayerDataManager.dataMap.get(player.getUniqueId()).isSneaking());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setLastInLiquid(PlayerDataManager.dataMap.get(player.getUniqueId()).isInLiquid());

            PlayerDataManager.dataMap.get(player.getUniqueId()).setLastYaw(PlayerDataManager.dataMap.get(player.getUniqueId()).getYaw());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setLastPitch(PlayerDataManager.dataMap.get(player.getUniqueId()).getPitch());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setPingSentTime(System.currentTimeMillis());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setPingID(ping.getId());

            PlayerDataManager.dataMap.get(player.getUniqueId()).setLastPosition(PlayerDataManager.dataMap.get(player.getUniqueId()).getPosition());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setLastDeltas(PlayerDataManager.dataMap.get(player.getUniqueId()).getDeltas());

            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

            PlayerDataManager.dataMap.get(player.getUniqueId()).setPosition(new Vector3d(wrapper.getLocation().getX(),wrapper.getLocation().getY(),wrapper.getLocation().getZ()));

            PlayerDataManager.dataMap.get(player.getUniqueId()).setDeltas(new Vector3d(Math.abs(PlayerDataManager.dataMap.get(player.getUniqueId()).getPosition().getX() - PlayerDataManager.dataMap.get(player.getUniqueId()).getLastPosition().getX()),Math.abs(PlayerDataManager.dataMap.get(player.getUniqueId()).getPosition().getY() - PlayerDataManager.dataMap.get(player.getUniqueId()).getLastPosition().getY()), Math.abs(PlayerDataManager.dataMap.get(player.getUniqueId()).getPosition().getZ() - PlayerDataManager.dataMap.get(player.getUniqueId()).getLastPosition().getZ())));

            PlayerDataManager.dataMap.get(player.getUniqueId()).setSprinting(player.isSprinting());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setSneaking(player.isSneaking());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setInLiquid(player.isInWater());

            PlayerDataManager.dataMap.get(player.getUniqueId()).setOnGround(player.isOnGround());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setPotionEffectList(player.getActivePotionEffects().stream().toList());


            PlayerDataManager.dataMap.get(player.getUniqueId()).setYaw(player.getLocation().getYaw());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setPitch(player.getLocation().getPitch());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setEye(player.getEyeLocation().toVector());
            PlayerDataManager.dataMap.get(player.getUniqueId()).setBoundingBox(player.getBoundingBox());
            event.getUser().sendPacket(ping);

        }else if(event.getPacketType() == PacketType.Play.Client.PONG){
            Player player = event.getPlayer();
            long time = System.currentTimeMillis();


            PlayerDataManager.dataMap.get(player.getUniqueId()).setPongReceivedTime(time);

            long delta = time - PlayerDataManager.dataMap.get(player.getUniqueId()).getPingSentTime();
            PlayerDataManager.dataMap.get(player.getUniqueId()).setTransactionDelta(delta);

        }
    }
}
