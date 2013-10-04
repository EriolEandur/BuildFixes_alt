package me.dags.BuildFixes.Listeners;

import me.dags.BuildFixes.Configuration.WorldConfig;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import static me.dags.BuildFixes.Configuration.WorldConfig.getWorldConf;

/**
 * @author dags_ <dags@dags.me>
 */

public class BlockListener implements Listener {

    private WorldConfig getConf(Player p) {
        return getWorldConf(p.getWorld());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void eggInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (event.hasBlock() && event.getClickedBlock().getType().equals(Material.DRAGON_EGG)) {
            //
            if (getConf(p).eggs()) {
                //
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                        && (!p.hasPermission("BuildFixes.eggInteract"))) {
                    event.setCancelled(true);
                }
                if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void logPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();

        if (p.getItemInHand().getType().equals(Material.LOG)) {
            //
            if (getConf(p).logs()) {
                //
                Block b = event.getBlockPlaced();
                MaterialData md = p.getItemInHand().getData();
                BlockState bs = b.getState();

                bs.setData(md);
                bs.update();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void doorPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        Material blockType = event.getBlock().getType();
        if (blockType.equals(Material.WOODEN_DOOR)
                || blockType.equals(Material.IRON_DOOR_BLOCK)) {
            //
            if (getConf(p).doors()) {
                //
                if (p.hasPermission("BuildFixes.doors")) {
                    float yaw = p.getLocation().getYaw();
                    byte data = getDat(yaw);

                    ItemStack is = new ItemStack(blockType, 1, data);
                    MaterialData md = is.getData();

                    Block b = event.getBlock();
                    Block b1 = b.getRelative(BlockFace.UP);
                    b1.setType(blockType);

                    BlockState bsB = b.getState();
                    BlockState bsB1 = b1.getState();

                    bsB.setData(md);
                    bsB.update(true, false);
                    bsB1.setData(md);
                    bsB1.update(true, false);
                }
            }
        }
    }

    private byte getDat(float yaw) {
        if ((yaw >= -225 && yaw < -135)
                || (yaw >= 135 && yaw <= 225)) {
            return 3;
        } else if ((yaw >= -135 && yaw < -45)
                || (yaw >= 225 && yaw < 315)) {
            return 0;
        } else if ((yaw >= -45 && yaw < 45)
                || (yaw >= -360 && yaw < -315)
                || (yaw >= 315 && yaw <= 360)) {
            return 1;
        } else if ((yaw >= -315 && yaw < -225)
                || (yaw >= 45 && yaw < 135)) {
            return 2;
        } else {
            return 0;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void doorBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Material doorType = event.getBlock().getType();

        if (doorType.equals(Material.WOODEN_DOOR) || doorType.equals(Material.IRON_DOOR_BLOCK)) {
            //
            if (getConf(p).doors()) {
                //
                Material blockAbove = event.getBlock().getRelative(BlockFace.UP).getType();
                Material blockBelow = event.getBlock().getRelative(BlockFace.DOWN).getType();
                if (blockAbove.equals(doorType)) {
                    event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
                }
                if (blockBelow.equals(doorType)) {
                    event.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void noPhysicsList(BlockPhysicsEvent event) {
        WorldConfig conf = getWorldConf(event.getBlock().getWorld());
        int npBlockType = event.getBlock().getType().ordinal();
        //
        if (conf.noPhys()) {
            //
            event.setCancelled(conf.noPhysList().contains(npBlockType));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    private void noPhysicsDoors(BlockPhysicsEvent event) {
        WorldConfig conf = getWorldConf(event.getBlock().getWorld());
        Material doorType = event.getBlock().getType();
        if (doorType.equals(Material.WOODEN_DOOR) || doorType.equals(Material.IRON_DOOR_BLOCK)) {
            //
            if (conf.doors()) {
                //
                Material above = event.getBlock().getRelative(BlockFace.UP).getType();
                Material below = event.getBlock().getRelative(BlockFace.DOWN).getType();

                if ((above.equals(doorType) || below.equals(doorType))) {
                    Block b = event.getBlock();

                    MaterialData mdB = b.getState().getData();
                    MaterialData mdB1 = b.getRelative(BlockFace.UP).getState().getData();
                    MaterialData mdB2 = b.getRelative(BlockFace.DOWN).getState().getData();

                    if (mdB.equals(mdB1) || mdB.equals(mdB2)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
