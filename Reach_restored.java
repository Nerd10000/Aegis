package dragon.me.Aegis.checks.combat.reach;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import dragon.me.Aegis.checks.Check;
import dragon.me.Aegis.data.PlayerData;
import dragon.me.Aegis.utils.PlayerDataManager;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class Reach extends Check implements PacketListener {

    public Reach(String checkName) {
        super(checkName);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY){
            PlayerData SourceData = PlayerDataManager.dataMap.get(event.getUser().getUUID());

            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            if (SpigotConversionUtil.getEntityById(SourceData.getWorld(),wrapper.getEntityId()) instanceof LivingEntity){

                PlayerData targetData = PlayerDataManager.dataMap.get(event.getUser().getUUID());



                Vector source = SourceData.getEye();
                BoundingBox targetBox = SourceData.getBoundingBox();
                Vector3d centered = new Vector3d(
                        (targetBox.getMinX() + targetBox.getMaxX()) / 2,
                        (targetBox.getMinY() + targetBox.getMaxY()) / 2,
                        (targetBox.getMinZ() + targetBox.getMaxZ()) / 2
                );
                double dist = source.distance(new Vector(centered.x,centered.y,centered.z));

                if (SourceData.getGameMode() == GameMode.CREATIVE){
                    return;
                }else {
                    if (dist > 3.1){

                        SourceData.alert(this.getCheckName(),dist,10);
                    }
                }
            }


        }
    }
}
