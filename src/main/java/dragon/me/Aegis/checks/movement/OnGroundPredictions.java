package dragon.me.Aegis.checks.movement;

import com.github.retrooper.packetevents.util.Vector3d;
import dragon.me.Aegis.data.PlayerData;
import dragon.me.Aegis.utils.PredictionArea;
import org.bukkit.Location;

public class OnGroundPredictions {
    //Note I make the code a bit more readable a simpler with AI
    public static double predict(PlayerData data,PredictionArea area) {


        Vector3d lastDeltas = data.getLastDeltas();
        Vector3d currentDeltas = data.getDeltas();

        // Get block slipperiness (previous and current)
        Location lastBlock = new Location(data.getWorld(), data.getLastPosition().x, data.getLastPosition().y - 1, data.getLastPosition().z);
        Location currentBlock = new Location(data.getWorld(), data.getPosition().x, data.getPosition().y - 1, data.getPosition().z);

        double slipperinessLast = lastBlock.getBlock().getType().getSlipperiness();
        double slipperinessNow = currentBlock.getBlock().getType().getSlipperiness();

        // Get player yaw in radians
        double yaw = Math.toRadians(data.getYaw());

        // Estimate movement intent (1.0 if player is moving)
        boolean moving = Math.abs(currentDeltas.x) > 0.001 || Math.abs(currentDeltas.z) > 0.001;
        double input = moving ? 1.0 : 0.0;
        double speed = 1.0; // default player movement speed

        // Acceleration factor
        double accelFactor = 0.1 * input * speed * Math.pow(0.6 / slipperinessNow, 3);

        // Calculate directional acceleration
        double accelX = accelFactor * Math.sin(yaw);
        double accelZ = accelFactor * Math.cos(yaw);

        // Final predicted velocity
        double predictedX = lastDeltas.x * slipperinessLast * 0.91 + accelX;
        double predictedZ = lastDeltas.z * slipperinessLast * 0.91 + accelZ;

        Vector3d prediction = new Vector3d(predictedX, currentDeltas.y, predictedZ);
        area.expand(prediction);

        double overflow = 0;
        if (!area.contains(currentDeltas)) {
            overflow = area.getOverflow(currentDeltas).length();
        }

        return overflow;
    }
}

