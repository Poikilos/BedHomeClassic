package org.poikilos.bedhomeclassic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

        boolean alwaysSetFoot = plugin.getConfig().getBoolean("always-set-foot", false);
        boolean setIfObstructed = plugin.getConfig().getBoolean("set-if-obstructed", true);
        boolean setSpawn = plugin.getConfig().getBoolean("set-spawn", true);
        boolean silentNight = plugin.getConfig().getBoolean("silent-night", true);

        Block target = bed;
        if (alwaysSetFoot) {
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
            if (!setIfObstructed && (isObstructed(foot) || isObstructed(head))) {
                String warn = plugin.getConfig().getString("messages.obstructed", "&cBed is obstructed (home not set).");
                player.sendMessage(color(warn));
                return;
            }
            target = foot;
        } else if (!setIfObstructed && isObstructed(bed)) {
            String warn = plugin.getConfig().getString("messages.obstructed", "&cBed is obstructed (home not set).");
            player.sendMessage(color(warn));
            return;
        }

        // Half a unit above the target bed block
        Location homeLoc = target.getLocation().clone().add(0.5, 0.5, 0.5);
        homeLoc.setYaw(player.getLocation().getYaw());
        homeLoc.setPitch(player.getLocation().getPitch());

        String homeName = plugin.getConfig().getString("home-name", "home");
        user.setHome(homeName, homeLoc);

        if (setSpawn) {
            // Exact bed block location (Essentials /home bed requires the block to still be a bed)
            final Location spawnLoc = target.getLocation().clone();
            spawnLoc.setX(target.getX());
            spawnLoc.setY(target.getY());
            spawnLoc.setZ(target.getZ());
            // Set now and again next tick so vanilla/Essentials cannot overwrite it
            player.setBedSpawnLocation(spawnLoc);
            user.setBedSpawnLocation(spawnLoc);
            final Player p = player;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    if (p.isOnline()) {
                        p.setBedSpawnLocation(spawnLoc);
                    }
                }
            }, 1L);
        }

        if (silentNight && isNight(player.getWorld())) {
            return;
        }

        String msg = plugin.getConfig().getString("messages.home-set", "&7Home set to your bed.");
        player.sendMessage(color(msg));
    }

    private boolean isNight(World world) {
        if (world == null) {
            return false;
        }
        long time = world.getTime() % 24000;
        // Vanilla sleep window is roughly 12541-23458
        return time >= 12541 && time <= 23458;
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
