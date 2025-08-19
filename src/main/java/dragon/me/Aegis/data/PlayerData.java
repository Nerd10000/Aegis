package dragon.me.Aegis.data;

import java.util.*;

import com.github.retrooper.packetevents.PacketEvents;
import dragon.me.Aegis.Aegis;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class PlayerData {

    private World world;
    private  UUID uuid;
    private int vl,sprintVL;
    private Vector3d position,lastPosition;
    private Vector3d deltas,lastDeltas;
    private boolean isLastSprinting,isSprinting,isLastSneaking,isSneaking,isLastInLiquid,isInLiquid;
    private Vector eye;
    private BoundingBox boundingBox;
    private long pingSentTime,pongReceivedTime,transactionDelta;
    private boolean isLastOnGround,isOnGround;
    private  int pingID;
    private float yaw,lastYaw,pitch,lastPitch,deltaYaw,deltaPitch,lastDeltaYaw,lastDeltaPitch;
    private  long breakActionStarted,breakActionStopped,lastBreakActionStarted,lastBreakActionStopped;
    private List<PotionEffect> potionEffectList = new ArrayList<>();
    private Action action,lastAction;
    private GameMode gameMode;
    private HashMap<String,Integer> violationMap = new HashMap<>();

    private int cps,lastCPS,deltaCPS;
    private List<Integer> CpsList = new ArrayList<>();
    private Deque<Long> clickTimestamps = new ArrayDeque<>();
    private double sensitivityX,sensitivityY;

    private boolean[] isFinishedArray = new boolean[10];
    private HashMap<String,Float> bufferedMap = new HashMap<>();
    public PlayerData(UUID uuid){
        this.uuid = uuid;
        this.world = Bukkit.getPlayer(uuid).getWorld();
    }

    public double getSensitivityY() {
        return sensitivityY;
    }

    public void setSensitivityY(double sensitivityY) {
        this.sensitivityY = sensitivityY;
    }

    public double getSensitivityX() {
        return sensitivityX;
    }

    public void setSensitivityX(double sensitivityX) {
        this.sensitivityX = sensitivityX;
    }

    public float getLastDeltaYaw() {
        return lastDeltaYaw;
    }

    public void setLastDeltaYaw(float lastDeltaYaw) {
        this.lastDeltaYaw = lastDeltaYaw;
    }

    public float getLastDeltaPitch() {
        return lastDeltaPitch;
    }

    public void setLastDeltaPitch(float lastDeltaPitch) {
        this.lastDeltaPitch = lastDeltaPitch;
    }

    public float getDeltaPitch() {
        return deltaPitch;
    }

    public void setDeltaPitch(float deltaPitch) {
        this.deltaPitch = deltaPitch;
    }

    public float getDeltaYaw() {
        return deltaYaw;
    }

    public void setDeltaYaw(float deltaYaw) {
        this.deltaYaw = deltaYaw;
    }

    public void setCpsList(List<Integer> cpsList) {
        CpsList = cpsList;
    }

    public void setClickTimestamps(Deque<Long> clickTimestamps) {
        this.clickTimestamps = clickTimestamps;
    }



    public boolean[] getIsFinishedArray() {
        return isFinishedArray;
    }

    public void setIsFinishedArray(boolean[] isFinishedArray) {
        this.isFinishedArray = isFinishedArray;
    }

    public List<Integer> getCpsList() {
        return CpsList;
    }

    public void alert(String name, Object debug, int maxVL) {
        // Increment violation level
        int newVL = violationMap.merge(name, 1, Integer::sum);

        // Prepare placeholders
        String prefix = Aegis.getPlugin().getConfig().getString("prefix", "&c[Aegis]");
        String alertTemplate = Aegis.getPlugin().getConfig().getString("alert_message", "%prefix% %player% failed %check% [%vl%/%max-vl%] // %offset%");
        String playerName = Optional.ofNullable(Bukkit.getPlayer(uuid))
                .map(Player::getDisplayName)
                .orElse("Unknown");

        String parsedMessage = alertTemplate
                .replace("%prefix%", prefix)
                .replace("%check%", name)
                .replace("%offset%", String.valueOf(debug))
                .replace("%vl%", String.valueOf(newVL))
                .replace("%max-vl%", String.valueOf(maxVL))
                .replace("%player%", playerName);

        // Deserialize color codes (& -> §)
        Component formattedMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(parsedMessage);

        // Send alert to players with permission
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("sentinel.alerts")) {
                PacketEvents.getAPI().getPlayerManager().getUser(player).sendMessage(formattedMessage);
            }
        }

        // Log to console (raw, no color)
        Bukkit.getLogger().info(parsedMessage.replaceAll("&.", ""));
    }

    public  void recordClicks(){
        long t = System.currentTimeMillis();
        clickTimestamps.addLast(t);
        cleanupOldClicks(t);
    }

    public void addCurrentCpsToList() {
        int currentCps = getCps();  // get current CPS (last 1 sec)
        CpsList.add(currentCps);    // add it to the list
    }

    public HashMap<String, Integer> getViolationMap() {
        return violationMap;
    }

    public void setViolationMap(HashMap<String, Integer> violationMap) {
        this.violationMap = violationMap;
    }
    private void cleanupOldClicks(long now) {

        while (!clickTimestamps.isEmpty() && now - clickTimestamps.peekFirst() > 1000) {
            clickTimestamps.removeFirst();
        }
    }

    public int getCps() {
        cleanupOldClicks(System.currentTimeMillis());
        return clickTimestamps.size();
    }

    public Deque<Long> getClickTimestamps() {
        return clickTimestamps;
    }

    public void setCps(int cps) {
        this.cps = cps;
    }

    public int getLastCPS() {
        return lastCPS;
    }

    public void setLastCPS(int lastCPS) {
        this.lastCPS = lastCPS;
    }

    public int getDeltaCPS() {
        return deltaCPS;
    }

    public void setDeltaCPS(int deltaCPS) {
        this.deltaCPS = deltaCPS;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Vector getEye() {
        return eye;
    }

    public void setEye(Vector eye) {
        this.eye = eye;
    }


    public long getLastBreakActionStopped() {
        return lastBreakActionStopped;
    }

    public void setLastBreakActionStopped(long lastBreakActionStopped) {
        this.lastBreakActionStopped = lastBreakActionStopped;
    } 

    public long getLastBreakActionStarted() {
        return lastBreakActionStarted;
    }

    public void setLastBreakActionStarted(long lastBreakActionStarted) {
        this.lastBreakActionStarted = lastBreakActionStarted;
    }

    public long getBreakActionStopped() {
        return breakActionStopped;
    }

    public void setBreakActionStopped(long breakActionStopped) {
        this.breakActionStopped = breakActionStopped;
    }

    public long getBreakActionStarted() {
        return breakActionStarted;
    }

    public void setBreakActionStarted(long breakActionStarted) {
        this.breakActionStarted = breakActionStarted;
    }
    public Action getAction(){
        return action;
    }
    public void setAction(Action action){
        this.action = action;
    }
    public Action getLastAction(){
        return lastAction;
    }
    public void setLastAction(Action action){
        this.lastAction = action;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
    }

    public List<PotionEffect> getPotionEffectList() {
        return potionEffectList;
    }

    public void setPotionEffectList(List<PotionEffect> potionEffectList) {
        this.potionEffectList = potionEffectList;
    }

    public int getSprintVL() {
        return sprintVL;
    }

    public void setSprintVL(int sprintVL) {
        this.sprintVL = sprintVL;
    }

    public int getVl() {
        return vl;
    }

    public void setVl(int vl) {
        this.sprintVL = vl;
        this.vl = vl;
    }

    public boolean isLastOnGround() {
        return isLastOnGround;
    }

    public void setLastOnGround(boolean lastOnGround) {
        isLastOnGround = lastOnGround;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }

    public boolean isLastSprinting() {
        return isLastSprinting;
    }

    public void setLastSprinting(boolean lastSprinting) {
        isLastSprinting = lastSprinting;
    }

    public boolean isLastSneaking() {
        return isLastSneaking;
    }

    public void setLastSneaking(boolean lastSneaking) {
        isLastSneaking = lastSneaking;
    }

    public boolean isLastInLiquid() {
        return isLastInLiquid;
    }

    public void setLastInLiquid(boolean lastInLiquid) {
        isLastInLiquid = lastInLiquid;
    }

    public boolean isInLiquid() {
        return isInLiquid;
    }

    public void setInLiquid(boolean inLiquid) {
        isInLiquid = inLiquid;
    }

    public long getTransactionDelta() {
        return transactionDelta;
    }

    public void setTransactionDelta(long transactionDelta) {
        this.transactionDelta = transactionDelta;
    }

    public long getPingSentTime() {
        return pingSentTime;
    }

    public int getPingID() {
        return pingID;
    }

    public void setPingID(int pingID) {
        this.pingID = pingID;
    }

    public void setPingSentTime(long pingSentTime) {
        this.pingSentTime = pingSentTime;
    }

    public long getPongReceivedTime() {
        return pongReceivedTime;
    }

    public void setPongReceivedTime(long pongReceivedTime) {
        this.pongReceivedTime = pongReceivedTime;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public void setLastPosition(Vector3d lastPosition) {
        this.lastPosition = lastPosition;
    }

    public void setDeltas(Vector3d deltas) {
        this.deltas = deltas;
    }

    public void setLastDeltas(Vector3d lastDeltas) {
        this.lastDeltas = lastDeltas;
    }

    public Vector3d getPosition() {
        return position;
    }

    public Vector3d getLastPosition() {
        return lastPosition;
    }

    public Vector3d getDeltas() {
        return deltas;
    }

    public Vector3d getLastDeltas() {
        return lastDeltas;
    }



    public World getWorld() {
        return world;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setSneaking(boolean sneaking) {
        isSneaking = sneaking;
    }

    public void setSprinting(boolean sprinting) {
        isSprinting = sprinting;
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public boolean isSneaking() {
        return isSneaking;
    }

    public HashMap<String, Float> getBufferMap() {
        return bufferedMap;
    }


}
