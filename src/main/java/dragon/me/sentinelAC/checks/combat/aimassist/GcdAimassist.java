package dragon.me.sentinelAC.checks.combat.aimassist;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dragon.me.sentinelAC.checks.Check;
import dragon.me.sentinelAC.data.PlayerData;
import dragon.me.sentinelAC.utils.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GcdAimassist extends Check implements PacketListener {
    public GcdAimassist(String checkName) {
        super(checkName);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION)
        {
            PlayerData data = PlayerDataManager.dataMap.get(event.getUser().getUUID());
            double sens = data.getSensitivity() * 0.6F * 0.2F;
            float f = (float) (sens * sens * sens * 8.0F);
            int expectedDX = Math.round((float) ((data.getLastDeltaYaw() / 0.15F) / f));
            int expectedDY = Math.round((float) ((data.getLastDeltaPitch() / 0.15F) / f));

            float predictedChangeDx = expectedDX * f;
            float predictedChangeDy = expectedDY * f;

            float differenceDx = Math.abs(expectedDX - predictedChangeDx);
            float differenceDy = Math.abs(expectedDY - predictedChangeDy);
/*
            event.getUser().sendMessage(
                    Component.text()
                            .append(Component.text("===[ GcdAimAssist Debug ]===\n", NamedTextColor.GOLD))
                            .append(Component.text("• Sensitivity: ", NamedTextColor.GRAY))
                            .append(Component.text(String.format("%.5f", sens) + "\n", NamedTextColor.WHITE))
                            .append(Component.text("• Factor (f): ", NamedTextColor.GRAY))
                            .append(Component.text(String.format("%.5f", f) + "\n", NamedTextColor.WHITE))
                            .append(Component.text("• Last ΔYaw: ", NamedTextColor.GRAY))
                            .append(Component.text(String.format("%.2f", data.getLastDeltaYaw()) + "\n", NamedTextColor.WHITE))
                            .append(Component.text("• Last ΔPitch: ", NamedTextColor.GRAY))
                            .append(Component.text(String.format("%.2f", data.getLastDeltaPitch()) + "\n", NamedTextColor.WHITE))
                            .append(Component.text("• Expected DX: ", NamedTextColor.GRAY))
                            .append(Component.text(expectedDX + "\n", NamedTextColor.WHITE))
                            .append(Component.text("• Expected DY: ", NamedTextColor.GRAY))
                            .append(Component.text(expectedDY + "\n", NamedTextColor.WHITE))
                            .append(Component.text("• Predicted ΔYaw: ", NamedTextColor.GRAY))
                            .append(Component.text(String.format("%.2f", predictedChangeDx) + "\n", NamedTextColor.WHITE))
                            .append(Component.text("• Predicted ΔPitch: ", NamedTextColor.GRAY))
                            .append(Component.text(String.format("%.2f", predictedChangeDy) + "\n", NamedTextColor.WHITE))
                            .append(Component.text("• ΔYaw Error: ", NamedTextColor.GRAY))
                            .append(Component.text(String.format("%.2f", differenceDx) + "\n", NamedTextColor.RED))
                            .append(Component.text("• ΔPitch Error: ", NamedTextColor.GRAY))
                            .append(Component.text(String.format("%.2f", differenceDy), NamedTextColor.RED))
                            .build()
            );

 */
        }
    }
}
