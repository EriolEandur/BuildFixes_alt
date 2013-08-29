package me.dags.BuildFixes.Listeners;

import static me.dags.BuildFixes.BuildFixes.doors;
import static me.dags.BuildFixes.BuildFixes.eggBreak;
import static me.dags.BuildFixes.BuildFixes.logs;
import static me.dags.BuildFixes.BuildFixes.multiWorlds;
import static me.dags.BuildFixes.BuildFixes.noPhysList;
import static me.dags.BuildFixes.BuildFixes.noPhysics;
import static me.dags.BuildFixes.MultiWorld.Worlds.worldsCFG;

import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled=true)
	private void eggInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.hasBlock() && event.getClickedBlock().getTypeId() == 122) {
			boolean cancel = true;
			if (multiWorlds) {
				cancel = worldsCFG.get(event.getPlayer().getWorld().getName()).get(1);
			} else {
				cancel = eggBreak;
			}

			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
					&& (!player.hasPermission("BuildFixes.eggInteract"))) {
				event.setCancelled(cancel);
			}
			if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if (!player.getGameMode().equals(GameMode.CREATIVE)) {
					event.setCancelled(cancel);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled=true)
	private void logPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		int logData = p.getItemInHand().getData().getData();
		if (p.getItemInHand().getTypeId() == 17 && logData >= 12
				&& logData <= 15) {

			boolean allow = false;
			if (multiWorlds) {
				allow = worldsCFG.get(event.getPlayer().getWorld().getName()).get(2);
			} else {
				allow = logs;
			}

			if (p.hasPermission("BuildFixes.logs") && allow) {
				event.getBlock().setData((byte) logData);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled=true)
	private void doorPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		int blockType = p.getItemInHand().getTypeId();
		if (blockType == 64 || blockType == 71) {

			boolean allow = false;
			if (multiWorlds) {
				allow = worldsCFG.get(event.getPlayer().getWorld().getName()).get(0);
			} else {
				allow = doors;
			}

			if (p.hasPermission("BuildFixes.doors") && allow) {
				int yaw = (int) p.getLocation().getYaw();
				if (blockType == 64 || blockType == 71) {
					if ((yaw >= -225 && yaw < -135)
							|| (yaw >= 135 && yaw <= 225)) {
						event.getBlockPlaced().setTypeId(blockType);
						event.getBlockPlaced().setTypeIdAndData(blockType,
								(byte) 3, false);
						event.getBlockPlaced()
								.getRelative(BlockFace.UP)
								.setTypeIdAndData(blockType, (byte) 3,
										false);
					} else if ((yaw >= -135 && yaw < -45)
							|| (yaw >= 225 && yaw < 315)) {
						event.getBlockPlaced().setTypeId(blockType);
						event.getBlockPlaced().setTypeIdAndData(blockType,
								(byte) 0, false);
						event.getBlockPlaced()
								.getRelative(BlockFace.UP)
								.setTypeIdAndData(blockType, (byte) 0,
										false);
					} else if ((yaw >= -45 && yaw < 45)
							|| (yaw >= -360 && yaw < -315)
							|| (yaw >= 315 && yaw <= 360)) {
						event.getBlockPlaced().setTypeId(blockType);
						event.getBlockPlaced().setTypeIdAndData(blockType,
								(byte) 1, false);
						event.getBlockPlaced()
								.getRelative(BlockFace.UP)
								.setTypeIdAndData(blockType, (byte) 1,
										false);
					} else if ((yaw >= -315 && yaw < -225)
							|| (yaw >= 45 && yaw < 135)) {
						event.getBlockPlaced().setTypeId(blockType);
						event.getBlockPlaced().setTypeIdAndData(blockType,
								(byte) 2, false);
						event.getBlockPlaced()
								.getRelative(BlockFace.UP)
								.setTypeIdAndData(blockType, (byte) 2,
										false);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	private void doorBreak(BlockBreakEvent event) {
		int doorType = event.getBlock().getTypeId();
		if (doorType == 64 || doorType == 71) {

			boolean allow = false;
			if (multiWorlds) {
				allow = worldsCFG.get(event.getPlayer().getWorld().getName()).get(0);
			} else {
				allow = doors;
			}

			if (allow) {
				int blockAbove = event.getBlock().getRelative(BlockFace.UP)
						.getTypeId();
				int blockBelow = event.getBlock()
						.getRelative(BlockFace.DOWN).getTypeId();
				if (blockAbove == 64 || blockAbove == 71) {
					event.getBlock().getRelative(BlockFace.UP).setTypeId(0);
				}
				if (blockBelow == 64 || blockBelow == 71) {
					event.getBlock().getRelative(BlockFace.DOWN)
							.setTypeId(0);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void noPhysicsList(BlockPhysicsEvent event) {
		int npBlockType = event.getBlock().getTypeId();
		if (noPhysList.contains(npBlockType)) {

			boolean cancel = true;
			if (multiWorlds) {
				cancel = worldsCFG.get(event.getBlock().getWorld().getName()).get(3);
			} else {
				cancel = noPhysics;
			}

			event.setCancelled(cancel);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void noPhysicsDoors(BlockPhysicsEvent event) {
		int id = event.getBlock().getTypeId();
		if ((id == 64 || id == 71)) {

			int idabove = event.getBlock().getRelative(BlockFace.UP)
					.getTypeId();
			int idbelow = event.getBlock().getRelative(BlockFace.DOWN)
					.getTypeId();

			if ((id == idabove || id == idbelow)) {

				byte dmg = event.getBlock().getData();
				byte above = event.getBlock().getRelative(BlockFace.UP)
						.getData();
				byte below = event.getBlock().getRelative(BlockFace.DOWN)
						.getData();

				if ((id == idabove && dmg == above)
						|| (id == idbelow && dmg == below)) {
					boolean cancel = true;
					if (multiWorlds) {
						cancel = worldsCFG.get(event.getBlock().getWorld().getName()).get(0);
					} else {
						cancel = doors;
					}
					event.setCancelled(cancel);
				}
			}
		}
	}
}
