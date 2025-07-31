package dragon.me.sentinelAC.data.interceptors;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import dragon.me.sentinelAC.utils.PlayerDataManager;
import dragon.me.sentinelAC.utils.math.MinecraftMath;

public class RotationInterceptor implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION){
            WrapperPlayClientPlayerRotation wrapper = new WrapperPlayClientPlayerRotation(event);

            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setLastYaw((float) Math.toRadians(wrapper.getYaw()));
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setLastPitch((float) Math.toRadians(wrapper.getPitch()));

            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setPitch((float) Math.toRadians(wrapper.getPitch()));
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setYaw((float) Math.toRadians(wrapper.getYaw()));

            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setLastDeltaYaw((PlayerDataManager.dataMap.get(event.getUser().getUUID()).getDeltaYaw()));
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setLastDeltaPitch((PlayerDataManager.dataMap.get(event.getUser().getUUID()).getDeltaPitch()));

            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setDeltaYaw((float) (Math.toRadians(wrapper.getYaw()) - PlayerDataManager.dataMap.get(event.getUser().getUUID()).getLastYaw()));
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setDeltaPitch((float) (Math.toRadians(wrapper.getPitch()) - PlayerDataManager.dataMap.get(event.getUser().getUUID()).getLastPitch()));

            double gcd = MinecraftMath.gcd(PlayerDataManager.dataMap.get(event.getUser().getUUID()).getDeltaYaw(),PlayerDataManager.dataMap.get(event.getUser().getUUID()).getDeltaPitch());
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setSensitivity(MinecraftMath.convertToSensitivity(gcd));

        }else if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())){

            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setLastYaw((float) Math.toRadians(wrapper.getLocation().getYaw()));
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setLastPitch((float) Math.toRadians(wrapper.getLocation().getPitch()));

            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setPitch((float) Math.toRadians(wrapper.getLocation().getPitch()));
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setYaw((float) Math.toRadians(wrapper.getLocation().getYaw()));

            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setLastDeltaYaw((PlayerDataManager.dataMap.get(event.getUser().getUUID()).getDeltaYaw()));
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setLastDeltaPitch((PlayerDataManager.dataMap.get(event.getUser().getUUID()).getDeltaPitch()));

            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setDeltaYaw((float) (Math.toRadians(wrapper.getLocation().getYaw()) - PlayerDataManager.dataMap.get(event.getUser().getUUID()).getLastYaw()));
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setDeltaPitch((float) (Math.toRadians(wrapper.getLocation().getPitch()) - PlayerDataManager.dataMap.get(event.getUser().getUUID()).getLastPitch()));

            double gcd = MinecraftMath.gcd(PlayerDataManager.dataMap.get(event.getUser().getUUID()).getDeltaYaw(),PlayerDataManager.dataMap.get(event.getUser().getUUID()).getDeltaPitch());
            PlayerDataManager.dataMap.get(event.getUser().getUUID()).setSensitivity(MinecraftMath.convertToSensitivity(gcd));

        }
    }
}
