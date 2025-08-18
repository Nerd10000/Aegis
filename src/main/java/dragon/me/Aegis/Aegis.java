package dragon.me.Aegis;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import dragon.me.Aegis.bukkit.PlayerJoinLeaveEvent;
import dragon.me.Aegis.bukkit.timer.TpsTimer;
import dragon.me.Aegis.checks.combat.aimassist.GcdAimassist;
import dragon.me.Aegis.checks.combat.clicks.*;
import dragon.me.Aegis.checks.combat.reach.Hitbox;
import dragon.me.Aegis.checks.combat.reach.Reach;
import dragon.me.Aegis.checks.movement.PredictionEngine;
import dragon.me.Aegis.checks.world.FastBreak;
import dragon.me.Aegis.data.interceptors.ActionInterceptor;
import dragon.me.Aegis.data.interceptors.CombatInterceptor;
import dragon.me.Aegis.data.interceptors.MovementInterceptor;
import dragon.me.Aegis.data.interceptors.RotationInterceptor;
import dragon.me.Aegis.utils.CheckRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Aegis extends JavaPlugin {
    private  CheckRegistry registry;
    private static Plugin plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        TpsTimer.run(this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveEvent(),this);
        plugin = this;
        saveDefaultConfig();
        registry = new CheckRegistry(this);
        PacketEvents.getAPI().getEventManager().registerListener(new MovementInterceptor(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().getEventManager().registerListener(new ActionInterceptor(),PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().getEventManager().registerListener(new CombatInterceptor(),PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().getEventManager().registerListener( new RotationInterceptor(),PacketListenerPriority.NORMAL);
        registry.register(new PredictionEngine("Prediction"));
        registry.register(new FastBreak("FastBreak"));
        registry.register(new Reach("Reach(Simple)"));
        registry.register(new MaxClicks("Clicks(Simple)"));
        registry.register(new MeanClicks("Clicks(Mean)"));
        registry.register(new ModeClicks("Clicks(Mode)"));
        //registry.customRegistration(new VarianceClicks("Clicks(Variance)"),PacketListenerPriority.HIGHEST);
        registry.customRegistration(new StdClicks("Clicks(Std)"),PacketListenerPriority.HIGHEST);
        //registry.register(new GcdAimassist("AimAssist(GCD)"));
        registry.register(new Hitbox("Hitbox(Simple)"));


        PacketEvents.getAPI().init();

    }

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        registry.dispose();
    }
}
