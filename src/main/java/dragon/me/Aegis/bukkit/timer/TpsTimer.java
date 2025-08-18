package dragon.me.Aegis.bukkit.timer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TpsTimer {

    private static long delta,lastTick,current;
    private static int count;
    public static void run(Plugin pLugin){
        Bukkit.getScheduler().runTaskTimer(pLugin,()-> {
            lastTick = current;
            count++;
            current = System.currentTimeMillis();

            delta = current-lastTick;


        },1L,1L);
    }

    public static long getDelta() {
        return delta;
    }
}
