package dragon.me.Aegis.utils.math;

import org.bukkit.util.Vector;

public class RayAABB {
    private final Vector origin;
    private final Vector direction;
    private final double maxDistance;

    public RayAABB(Vector origin, Vector direction, double maxDistance) {
        this.origin = origin;
        this.direction = direction;
        this.maxDistance = maxDistance;
    }

    /**
     * Checks ray-AABB intersection.
     * Returns hit point Vector if intersects within maxDistance, else null.
     */
    public Vector getIntersectionPoint(Vector boxMin, Vector boxMax) {
        double tmin = (boxMin.getX() - origin.getX()) / direction.getX();
        double tmax = (boxMax.getX() - origin.getX()) / direction.getX();
        if (tmin > tmax) { double temp = tmin; tmin = tmax; tmax = temp; }

        double tymin = (boxMin.getY() - origin.getY()) / direction.getY();
        double tymax = (boxMax.getY() - origin.getY()) / direction.getY();
        if (tymin > tymax) { double temp = tymin; tymin = tymax; tymax = temp; }

        if ( (tmin > tymax) || (tymin > tmax) ) return null;

        if (tymin > tmin) tmin = tymin;
        if (tymax < tmax) tmax = tymax;

        double tzmin = (boxMin.getZ() - origin.getZ()) / direction.getZ();
        double tzmax = (boxMax.getZ() - origin.getZ()) / direction.getZ();
        if (tzmin > tzmax) { double temp = tzmin; tzmin = tzmax; tzmax = temp; }

        if ( (tmin > tzmax) || (tzmin > tmax) ) return null;

        if (tzmin > tmin) tmin = tzmin;
        if (tzmax < tmax) tmax = tzmax;

        if (tmin < 0 || tmin > maxDistance) return null;

        // Calculate exact hit point
        return origin.clone().add(direction.clone().multiply(tmin));
    }
}