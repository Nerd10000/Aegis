package dragon.me.Aegis.checks.combat.reach;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import dragon.me.Aegis.checks.Check;
import dragon.me.Aegis.data.PlayerData;
import dragon.me.Aegis.utils.PlayerDataManager;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;


// Code reformatted using ChatGPT
public class Reach extends Check implements PacketListener {

    public Reach(String checkName) {
        super(checkName);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            PlayerData sourceData = PlayerDataManager.dataMap.get(event.getUser().getUUID());

            if (sourceData.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            if (wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                Entity victim = SpigotConversionUtil.getEntityById(
                        sourceData.getWorld(), wrapper.getEntityId());

                Vector source = sourceData.getEye();
                if (victim == null) {
                    return;
                }
                

                double max = getConfig().getDouble("reach_thresholds.simple_max");
                BoundingBox targetBox = victim.getBoundingBox();
                double allowedExtra = 0;
                double allowedDist = calculateDistanceToBox(source, targetBox);

                if (sourceData.getTransactionDelta()
                        > getConfig().getInt("reach_thresholds.compensation_needed_thresholds")) {
                    allowedExtra = Math.min(
                            (sourceData.getTransactionDelta()
                                    - getConfig().getInt("reach_thresholds.compensation_needed_thresholds")) / 1000.0,
                            getConfig().getDouble("reach_thresholds.compensation_threshold"));

                    max += allowedExtra;
                }

                if (allowedDist > max) {
                   sourceData.getBufferMap().put(
                           "Reach(Simple)",
                           sourceData.getBufferMap().getOrDefault("Reach(Simple)", 0F) + 1);

                   if (sourceData.getBufferMap().getOrDefault("Reach(Simple)", 0F) >

                   getConfig().getInt("reach_thresholds.simple_max_buffer")){

                       sourceData.alert(
                               getCheckName(),
                               "Distance: " + allowedDist
                                       + " Max: " + max
                                       + " Compensated: " + allowedExtra
                                       + " Latency: " + sourceData.getTransactionDelta(),
                               10
                       );
                       sourceData.getBufferMap().put("Reach(Simple)", 0F);
                    }

                }else {
                    sourceData.getBufferMap().put("Reach(Simple)",
                            Math.max(0,sourceData.getBufferMap().getOrDefault("Reach(Simple)", 0.0f) - 0.3f));
                }
            }
        }
    }

    private double calculateDistanceToBox(Vector point, BoundingBox box) {
        double dx = Math.max(0,
                Math.max(box.getMinX() - point.getX(), point.getX() - box.getMaxX()));
        double dy = Math.max(0,
                Math.max(box.getMinY() - point.getY(), point.getY() - box.getMaxY()));
        double dz = Math.max(0,
                Math.max(box.getMinZ() - point.getZ(), point.getZ() - box.getMaxZ()));

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}