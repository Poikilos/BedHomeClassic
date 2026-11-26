package org.poikilos.bedhomeclassic;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BedHomeClassic extends JavaPlugin {

    private static BedHomeClassic instance;
    private BedListener listener;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if (Bukkit.getPluginManager().getPlugin("Essentials") == null) {
            getLogger().warning("Essentials not found! BedHomeClassic will not set homes.");
        }

        listener = new BedListener(this);
        Bukkit.getPluginManager().registerEvents(listener, this);

        getLogger().info("BedHomeClassic enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BedHomeClassic disabled.");
    }

    public static BedHomeClassic getInstance() {
        return instance;
    }
}
