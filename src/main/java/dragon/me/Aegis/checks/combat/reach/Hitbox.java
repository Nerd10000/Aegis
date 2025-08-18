package dragon.me.Aegis.checks.combat.reach;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import dragon.me.Aegis.Aegis;
import dragon.me.Aegis.checks.Check;
import dragon.me.Aegis.data.PlayerData;
import dragon.me.Aegis.utils.PlayerDataManager;
import dragon.me.Aegis.utils.math.MinecraftMath;
import dragon.me.Aegis.utils.math.RayAABB;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Hitbox extends Check  implements PacketListener {


    public Hitbox(String checkName) {
        super(checkName);
    }

    // Experimental


    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            PlayerData data = PlayerDataManager.dataMap.get(event.getUser().getUUID());

            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);

            if (wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                Vector origin = data.getEye().toLocation(data.getWorld()).toVector();
                Vector direction = data.getEye().toLocation(data.getWorld()).getDirection().normalize();
                Entity victim = SpigotConversionUtil.getEntityById(data.getWorld(), wrapper.getEntityId());

                if (victim == null) return; // Defensive check

                BoundingBox victimBox = victim.getBoundingBox();
                Vector boxMin = new Vector(victimBox.getMinX(), victimBox.getMinY(), victimBox.getMinZ());
                Vector boxMax = new Vector(victimBox.getMaxX(), victimBox.getMaxY(), victimBox.getMaxZ());

                RayAABB ray = new RayAABB(origin, direction, 10);
                Vector hit = ray.getIntersectionPoint(boxMin, boxMax);

                if (hit != null) {
                    // Ray hit the bounding box - do something useful here, e.g. calculate offset from box center:
                    Vector boxCenter = boxMin.clone().add(boxMax).multiply(0.5);
                    Vector offset = hit.clone().subtract(boxCenter);

                    data.alert(getCheckName(),
                            String.format("Hit offset: x=%.3f y=%.3f z=%.3f", offset.getX(), offset.getY(), offset.getZ()), 10);
                } else {
                    // Ray missed bounding box
                    data.alert(getCheckName(), "Ray missed victim bounding box.", 5);
                }
            }
        }
    }
}
