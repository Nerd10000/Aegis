package dragon.me.sentinelAC.checks.movement;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import dragon.me.sentinelAC.bukkit.timer.TpsTimer;
import dragon.me.sentinelAC.checks.Check;
import dragon.me.sentinelAC.data.PlayerData;
import dragon.me.sentinelAC.utils.PlayerDataManager;
import dragon.me.sentinelAC.utils.PredictionArea;
import org.bukkit.entity.Player;

public class PredictionEngine extends Check implements PacketListener {

    public PredictionEngine(String checkName) {
        super(checkName);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())){
            //Need to account to Null pointer exception

            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);
            Player p = event.getPlayer();
            PlayerData data = PlayerDataManager.dataMap.get(p.getUniqueId());
            PredictionArea area = new PredictionArea(data.getDeltas());
            double overflow = OnGroundPredictions.predict(data,area);
            //p.sendMessage("Overflow: "+ overflow + "| ticks behind: "+ data.getTransactionDelta() / TpsTimer.getDelta());
        }
    }

}
