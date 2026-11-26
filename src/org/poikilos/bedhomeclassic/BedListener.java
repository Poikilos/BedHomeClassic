package org.poikilos.bedhomeclassic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

public class BedListener implements Listener {

    private final BedHomeClassic plugin;

    public BedListener(BedHomeClassic plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (!plugin.getConfig().getBoolean("enabled", true)) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission("bedhomeclassic.use")) {
            return;
        }

        Block bed = event.getBed();
        if (bed == null || bed.getType() != Material.BED_BLOCK) {
            return;
        }

        Plugin essPlugin = plugin.getServer().getPluginManager().getPlugin("Essentials");
        if (essPlugin == null || !(essPlugin instanceof Essentials)) {
            player.sendMessage(color(plugin.getConfig().getString("messages.no-essentials", "&cEssentials not found.")));
            return;
        }

        Essentials essentials = (Essentials) essPlugin;
        User user = essentials.getUser(player);
        if (user == null) {
            return;
        }

        // 1 unit above the bed block (not the player's standing position)
        Location homeLoc = bed.getLocation().clone().add(0.5, 1.0, 0.5);
        homeLoc.setYaw(player.getLocation().getYaw());
        homeLoc.setPitch(player.getLocation().getPitch());

        String homeName = plugin.getConfig().getString("home-name", "home");
        user.setHome(homeName, homeLoc);

        String msg = plugin.getConfig().getString("messages.home-set", "&aHome set to your bed!");
        player.sendMessage(color(msg));
    }

    private String color(String s) {
        if (s == null) return "";
        return s.replace('&', '\u00A7');
    }
}
