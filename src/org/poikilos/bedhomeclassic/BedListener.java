package org.poikilos.bedhomeclassic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Bed;
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
        setHomeFromBed(event.getPlayer(), event.getBed());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (!plugin.getConfig().getBoolean("enabled", true)) {
            return;
        }
        if (!plugin.getConfig().getBoolean("set-home-even-if-cannot-sleep", true)) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block clicked = event.getClickedBlock();
        if (clicked == null || clicked.getType() != Material.BED_BLOCK) {
            return;
        }
        setHomeFromBed(event.getPlayer(), clicked);
    }

    private void setHomeFromBed(Player player, Block bed) {
        if (player == null || bed == null || bed.getType() != Material.BED_BLOCK) {
            return;
        }
        if (!player.hasPermission("bedhome.use")) {
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

        Bed bedData = new Bed(bed.getType(), bed.getData());
        Block foot = bed;
        Block head = bed;
        if (bedData.isHeadOfBed()) {
            head = bed;
            BlockFace facing = bedData.getFacing();
            if (facing != null) {
                foot = bed.getRelative(facing.getOppositeFace());
            }
        } else {
            foot = bed;
            BlockFace facing = bedData.getFacing();
            if (facing != null) {
                head = bed.getRelative(facing);
            }
        }

        if (isObstructed(foot) || isObstructed(head)) {
            String warn = plugin.getConfig().getString("messages.obstructed", "&cBed is obstructed (home not set).");
            player.sendMessage(color(warn));
            return;
        }

        // Half a unit above the foot block (beds are only half a unit high)
        Location homeLoc = foot.getLocation().clone().add(0.5, 0.5, 0.5);
        homeLoc.setYaw(player.getLocation().getYaw());
        homeLoc.setPitch(player.getLocation().getPitch());

        String homeName = plugin.getConfig().getString("home-name", "home");
        user.setHome(homeName, homeLoc);

        String msg = plugin.getConfig().getString("messages.home-set", "&7Home set to your bed.");
        player.sendMessage(color(msg));
    }

    private boolean isObstructed(Block bedPart) {
        if (bedPart == null) {
            return true;
        }
        Block above1 = bedPart.getRelative(0, 1, 0);
        Block above2 = bedPart.getRelative(0, 2, 0);
        return above1.getType() != Material.AIR || above2.getType() != Material.AIR;
    }

    private String color(String s) {
        if (s == null) return "";
        return s.replace('&', '\u00A7');
    }
}
