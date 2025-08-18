package dragon.me.Aegis.data.interceptors;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import dragon.me.Aegis.utils.PlayerDataManager;
import dragon.me.Aegis.utils.math.MinecraftMath;

public class RotationInterceptor implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())){
            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

            // Get current player data
            var playerData = PlayerDataManager.dataMap.get(event.getUser().getUUID());

            // Get current rotation values (in degrees)
            float currentYaw = wrapper.getLocation().getYaw();
            float currentPitch = wrapper.getLocation().getPitch();

            // Calculate deltas using PREVIOUS values (in degrees)
            float deltaYaw = currentYaw - playerData.getLastYaw();
            float deltaPitch = currentPitch - playerData.getLastPitch();

            // Handle yaw wrapping (-180 to 180 degrees)
            if (deltaYaw > 180) {
                deltaYaw -= 360;
            } else if (deltaYaw < -180) {
                deltaYaw += 360;
            }

            // Store previous deltas
            playerData.setLastDeltaYaw(playerData.getDeltaYaw());
            playerData.setLastDeltaPitch(playerData.getDeltaPitch());

            // Update current deltas
            playerData.setDeltaYaw(deltaYaw);
            playerData.setDeltaPitch(deltaPitch);

            // Calculate GCD and sensitivity (only if deltas are significant)
            if (Math.abs(deltaYaw) > 0.1) {
                double gcdX = MinecraftMath.gcd(playerData.getLastDeltaYaw(), deltaYaw);
                playerData.setSensitivityX(MinecraftMath.convertToSensitivity(gcdX));
            }

            if (Math.abs(deltaPitch) > 0.1) {
                double gcdY = MinecraftMath.gcd(playerData.getLastDeltaPitch(), deltaPitch);
                playerData.setSensitivityY(MinecraftMath.convertToSensitivity(gcdY));
            }

            // Update stored rotation values for next packet (keep in degrees)
            playerData.setLastYaw(currentYaw);
            playerData.setLastPitch(currentPitch);
            playerData.setYaw(currentYaw);
            playerData.setPitch(currentPitch);
        }
    }
}