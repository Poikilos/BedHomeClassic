package org.poikilos.bedhomeclassic;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BedHomeClassic extends JavaPlugin implements CommandExecutor {

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

        getCommand("bedhome").setExecutor(this);

        getLogger().info("BedHomeClassic enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BedHomeClassic disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bedhome.admin")) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            sender.sendMessage("§aBedHomeClassic config reloaded.");
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("set-home-even-if-cannot-sleep")) {
            String value = args[1].toLowerCase();
            if (!value.equals("true") && !value.equals("false")) {
                sender.sendMessage("§cUsage: /bedhome set-home-even-if-cannot-sleep <true|false>");
                return true;
            }
            boolean enabled = value.equals("true");
            getConfig().set("set-home-even-if-cannot-sleep", enabled);
            saveConfig();
            sender.sendMessage("§aset-home-even-if-cannot-sleep set to " + enabled + " and saved.");
            return true;
        }

        sender.sendMessage("§cUsage:");
        sender.sendMessage("§c/bedhome reload");
        sender.sendMessage("§c/bedhome set-home-even-if-cannot-sleep <true|false>");
        return true;
    }

    public static BedHomeClassic getInstance() {
        return instance;
    }
}
