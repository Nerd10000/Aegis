package dragon.me.Aegis.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import dragon.me.Aegis.checks.Check;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CheckRegistry {

    private  final List<Check> checks = new ArrayList<>();

    public CheckRegistry(Plugin plugin){
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(plugin));
        PacketEvents.getAPI().load();
    }

    public void register(Check check){

        PacketEvents.getAPI().getEventManager().registerListener((PacketListener) check, PacketListenerPriority.NORMAL);

        checks.add(check);
    }
    public void customRegistration(Check check,PacketListenerPriority priority){
        PacketEvents.getAPI().getEventManager().registerListener((PacketListener) check, priority);
    }
    public void unRegister(Check check){

        checks.remove(check);
    }

    public void dispose(){
        PacketEvents.getAPI().terminate();
        checks.clear();
    }

}
