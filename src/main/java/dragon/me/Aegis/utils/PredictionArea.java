package dragon.me.Aegis.utils;

import com.github.retrooper.packetevents.util.Vector3d;

import java.util.HashMap;
import java.util.UUID;

public class PredictionArea {
    public static HashMap<UUID,PredictionArea> areaHashMap = new HashMap<>();

    private Vector3d min,max;

    public PredictionArea(Vector3d position){
        min = position;
        max = position;
    }
    public void expand(Vector3d pos) {
        min = new Vector3d(
                Math.min(min.x, pos.x),
                Math.min(min.y, pos.y),
                Math.min(min.z, pos.z)
        );
        max = new Vector3d(
                Math.max(max.x, pos.x),
                Math.max(max.y, pos.y),
                Math.max(max.z, pos.z)
        );
    }
    public boolean contains(Vector3d pos) {
        return pos.x >= min.x && pos.x <= max.x &&
                pos.y >= min.y && pos.y <= max.y &&
                pos.z >= min.z && pos.z <= max.z;
    }
    public Vector3d getOverflow(Vector3d pos) {
        double overflowX = 0.0;
        double overflowY = 0.0;
        double overflowZ = 0.0;

        if (pos.x < min.x) {
            overflowX = pos.x - min.x;
        } else if (pos.x > max.x) {
            overflowX = pos.x - max.x;
        }

        if (pos.y < min.y) {
            overflowY = pos.y - min.y;
        } else if (pos.y > max.y) {
            overflowY = pos.y - max.y;
        }

        if (pos.z < min.z) {
            overflowZ = pos.z - min.z;
        } else if (pos.z > max.z) {
            overflowZ = pos.z - max.z;
        }

        return new Vector3d(overflowX, overflowY, overflowZ);
    }




}
