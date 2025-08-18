package dragon.me.Aegis.checks.world;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import dragon.me.Aegis.checks.Check;
import dragon.me.Aegis.data.PlayerData;
import dragon.me.Aegis.utils.PlayerDataManager;
import net.kyori.adventure.text.Component;

public class FastBreak extends Check implements PacketListener {


    public FastBreak(String checkName) {
        super(checkName);
    }
    private  boolean startedDigging,canceledDigging,finishedDigging,lastStartedDigging,lastCanceledDigging,lastFinishedDigging = false;
    private  double deltaTime,startTime,finishTime;
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            PlayerData data = PlayerDataManager.dataMap.get(event.getUser().getUUID());
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event);
            long difference = data.getTransactionDelta();
            lastStartedDigging = startedDigging;
            lastFinishedDigging = finishedDigging;
            lastCanceledDigging = canceledDigging;


            startedDigging = false;
            canceledDigging = false;
            finishedDigging = false;
            if (wrapper.getAction() == DiggingAction.START_DIGGING) {
                startedDigging = true;
                startTime = System.currentTimeMillis();

            } else if (wrapper.getAction() == DiggingAction.CANCELLED_DIGGING || wrapper.getAction() ==
                    DiggingAction.DROP_ITEM || wrapper.getAction() == DiggingAction.DROP_ITEM_STACK ||
                    wrapper.getAction() == DiggingAction.RELEASE_USE_ITEM || wrapper.getAction() == DiggingAction.SWAP_ITEM_WITH_OFFHAND) {
                canceledDigging = true;

            } else {
                finishedDigging = true;

                startTime = 0;
            }
            event.getUser().sendMessage(Component.text("Canceled? " + canceledDigging + " / started?" + startedDigging + " / finished? " + finishedDigging + "| ping=" + difference + " ! debug: dTime= "+deltaTime));

            if (lastStartedDigging && finishedDigging){
                deltaTime = Math.abs(System.currentTimeMillis() - startTime);
            }
        }
    }
}
