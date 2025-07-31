package dragon.me.sentinelAC;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import dragon.me.sentinelAC.bukkit.PlayerJoinLeaveEvent;
import dragon.me.sentinelAC.bukkit.timer.TpsTimer;
import dragon.me.sentinelAC.checks.combat.aimassist.GcdAimassist;
import dragon.me.sentinelAC.checks.combat.clicks.*;
import dragon.me.sentinelAC.checks.combat.reach.Reach;
import dragon.me.sentinelAC.checks.movement.PredictionEngine;
import dragon.me.sentinelAC.checks.world.FastBreak;
import dragon.me.sentinelAC.data.interceptors.ActionInterceptor;
import dragon.me.sentinelAC.data.interceptors.CombatInterceptor;
import dragon.me.sentinelAC.data.interceptors.MovementInterceptor;
import dragon.me.sentinelAC.data.interceptors.RotationInterceptor;
import dragon.me.sentinelAC.utils.CheckRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;

public final class SentinelAC extends JavaPlugin {
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
        registry.customRegistration(new VarianceClicks("Clicks(Variance)"),PacketListenerPriority.HIGHEST);
        registry.customRegistration(new StdClicks("Clicks(Std)"),PacketListenerPriority.HIGHEST);
        registry.register(new GcdAimassist("AimAssist(GCD)"));


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
