package dragon.me.sentinelAC.utils.math;

public class MinecraftMath {

    // I used the function grim uses because it works fine, and I didn't want to re-invent the wheel.
    public static final double MINIMUM_DIVISOR = ((Math.pow(0.2f, 3) * 8) * 0.15) - 1e-3;
    public static double gcd(double a, double b) {
        if (a == 0) return 0;

        // Make sure a is larger than b
        if (a < b) {
            double temp = a;
            a = b;
            b = temp;
        }

        while (b > MINIMUM_DIVISOR) {
            double temp = a - (Math.floor(a / b) * b);
            a = b;
            b = temp;
        }

        return a;
    }
    public static double convertToSensitivity(double var13) {
        double var11 = var13 / 0.15F / 8.0D;
        double var9 = Math.cbrt(var11);
        return (var9 - 0.2f) / 0.6f;
    }
}
