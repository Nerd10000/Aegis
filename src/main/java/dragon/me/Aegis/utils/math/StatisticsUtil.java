package dragon.me.Aegis.utils.math;

import java.util.*;

public class StatisticsUtil {


    public static int meanInt(List<Integer> intList){
        int total = 0;
        int length = intList.size();
        for (int x : intList){
            total += x;
        }
        return total / length;
    }

    public static double medianInt(List<Integer> intList){
        Collections.sort(intList);
        int n = intList.size();
        double median = 0;
        if (intList.size() % 2 == 0){
            median = (double) (intList.get((n / 2) - 1) + intList.get((n / 2))) / 2;
        }else {
            median = intList.get( n / 2 );
        }
        return  median;
    }
    public static int modeInt(List<Integer> intList) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        int mode = intList.get(0);
        int maxCount = 0;

        // Count frequencies
        for (int num : intList) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);

            // Update mode and maxCount on the fly
            if (frequencyMap.get(num) > maxCount) {
                maxCount = frequencyMap.get(num);
                mode = num;
            }
        }

        return mode;
    }
    public static double variance(List<Integer> values) {
        if (values == null || values.size() < 2) {
            throw new IllegalArgumentException("At least two values are required");
        }

        int n = 0;
        double mean = 0.0;
        double M2 = 0.0;

        for (int x : values) {
            n++;
            double delta = x - mean;
            mean += delta / n;
            M2 += delta * (x - mean);
        }

        return M2 / (n - 1);  // Sample variance
    }
    public static double standardDeviation(List<Integer> values) {
        if (values == null || values.size() < 2) {
            throw new IllegalArgumentException("At least two values are required");
        }

        int n = 0;
        double mean = 0.0;
        double M2 = 0.0;

        for (int x : values) {
            n++;
            double delta = x - mean;
            mean += delta / n;
            M2 += delta * (x - mean);
        }

        double variance = M2 / (n - 1); // Sample variance
        return Math.sqrt(variance);
    }
}
