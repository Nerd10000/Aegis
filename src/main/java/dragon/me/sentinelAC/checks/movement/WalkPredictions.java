package dragon.me.sentinelAC.checks.movement;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import dragon.me.sentinelAC.checks.Check;
import dragon.me.sentinelAC.data.PlayerData;
import dragon.me.sentinelAC.utils.PlayerDataManager;

public class WalkPredictions extends Check {
    public WalkPredictions(String checkName) {
        super(checkName);
    }

    public double predict(PlayerData data){



        return 0;
    }
}
