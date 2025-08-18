package dragon.me.Aegis.checks.combat.aimassist;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import dragon.me.Aegis.checks.Check;
import dragon.me.Aegis.data.PlayerData;
import dragon.me.Aegis.utils.PlayerDataManager;

public class GcdAimassist extends Check implements PacketListener {
    public GcdAimassist(String checkName) {
        super(checkName);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            PlayerData data = PlayerDataManager.dataMap.get(event.getUser().getUUID());

            // Skip if no movement
            if (Math.abs(data.getLastDeltaYaw()) < 0.1 && Math.abs(data.getLastDeltaPitch()) < 0.1) {
                return;
            }

            // Calculate sensitivity factors (same as Minecraft client)
            double sensX = data.getSensitivityX() * 0.6F + 0.2F;
            double sensY = data.getSensitivityY() * 0.6F + 0.2F;
            float f = (float) (sensX * sensX * sensX * 8.0F);
            float f2 = (float) (sensY * sensY * sensY * 8.0F);

            // Calculate expected mouse input (reverse of Minecraft's mouse->rotation conversion)
            int expectedDX = Math.round((float) (data.getLastDeltaYaw() / 0.15F / f));
            int expectedDY = Math.round((float) (data.getLastDeltaPitch() / 0.15F / f2));

            // Calculate what the rotation change should be given the mouse input
            float predictedDeltaYaw = expectedDX * f * 0.15F;
            float predictedDeltaPitch = expectedDY * f2 * 0.15F;

            // Calculate differences (should be 0 for legitimate players)
            float differenceYaw = Math.abs(data.getLastDeltaYaw() - predictedDeltaYaw);
            float differencePitch = Math.abs(data.getLastDeltaPitch() - predictedDeltaPitch);


            // Flag logic would go here
            if (differenceYaw < getConfig().getDouble("aimassist_thresholds.gcd_x_min"
            ) || differenceYaw > getConfig().getDouble("aimassist_thresholds.gcd_x_max") ||
            differencePitch < getConfig().getDouble("aimassist_thresholds.gcd_y_min") ||
                    differencePitch > getConfig().getDouble("aimassist_thresholds.gcd_y_max")) {

                data.alert(getCheckName(),"sensivityX: "+ sensX + "; sensitivityY: " + sensY+"; deltaX: " + differenceYaw + "; deltaY: " + differencePitch, 1);
            }
        }
    }
}