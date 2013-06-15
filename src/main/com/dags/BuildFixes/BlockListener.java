package com.dags.BuildFixes;

import static com.dags.BuildFixes.BuildFixes.doors;
import static com.dags.BuildFixes.BuildFixes.eggBreak;
import static com.dags.BuildFixes.BuildFixes.lamps;
import static com.dags.BuildFixes.BuildFixes.logs;
import static com.dags.BuildFixes.BuildFixes.noPhysList;
import static com.dags.BuildFixes.BuildFixes.noPhysics;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	private void playerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		// DRAGON EGG INTERACTION BLOCKER!
		if (eggBreak) {
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
					&& event.getClickedBlock().getType()
							.equals(Material.DRAGON_EGG)
					&& (!player.hasPermission("BuildFixes.eggInteract"))) {
				event.setCancelled(true);
			}
			if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)
					&& event.getClickedBlock().getType()
							.equals(Material.DRAGON_EGG)) {
				if (player.getGameMode().equals(GameMode.CREATIVE)) {
					event.setCancelled(false);
				} else {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void blockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		// PLACE COVERED LOGS!
		if (logs) {
			if (player.hasPermission("BuildFixes.logs")
					&& player.getItemInHand().getType().equals(Material.LOG)) {
				int logData = player.getItemInHand().getData().getData();
				if (logData >= 12) {
					event.getBlock().setData((byte) logData);
				}
			}
		}
		// PLACE HALF DOORS!
		if (player.hasPermission("BuildFixes.doors") && doors) {
			int blockType = player.getItemInHand().getTypeId();
			int yaw = (int) player.getLocation().getYaw();
			if (blockType == 64 || blockType == 71) {
				// NORTH
				if ((yaw >= -225 && yaw < -135) 
						|| (yaw >= 135 && yaw <= 225)) {
					event.getBlockPlaced().setTypeId(blockType);
					event.getBlockPlaced().setTypeIdAndData(blockType,
							(byte) 3, false);
					event.getBlockPlaced().getRelative(BlockFace.UP)
							.setTypeIdAndData(blockType, (byte) 3, false);
				}
				// EAST
				else if ((yaw >= -135 && yaw < -45)
						|| (yaw >= 225 && yaw < 315)) {
					event.getBlockPlaced().setTypeId(blockType);
					event.getBlockPlaced().setTypeIdAndData(blockType,
							(byte) 0, false);
					event.getBlockPlaced().getRelative(BlockFace.UP)
							.setTypeIdAndData(blockType, (byte) 0, false);
				}
				// SOUTH
				else if ((yaw >= -45 && yaw < 45)
						|| (yaw >= -360 && yaw < -315)
						|| (yaw >= 315 && yaw <= 360)) {
					event.getBlockPlaced().setTypeId(blockType);
					event.getBlockPlaced().setTypeIdAndData(blockType,
							(byte) 1, false);
					event.getBlockPlaced().getRelative(BlockFace.UP)
							.setTypeIdAndData(blockType, (byte) 1, false);
				}
				// WEST
				else if ((yaw >= -315 && yaw < -225)
						|| (yaw >= 45 && yaw < 135)) {
					event.getBlockPlaced().setTypeId(blockType);
					event.getBlockPlaced().setTypeIdAndData(blockType,
							(byte) 2, false);
					event.getBlockPlaced().getRelative(BlockFace.UP)
							.setTypeIdAndData(blockType, (byte) 2, false);
				}
			}
		}
	}

	// BREAK EVENT FOR HALF-DOORS (ALSO REMOVES INVISIBLE HALF)
	@EventHandler(priority = EventPriority.HIGH)
	private void blockBreak(BlockBreakEvent event) {
		int doorType = event.getBlock().getTypeId();
		if (doorType == 64 || doorType == 71) {
			int blockAbove = event.getBlock().getRelative(BlockFace.UP)
					.getTypeId();
			int blockBelow = event.getBlock().getRelative(BlockFace.DOWN)
					.getTypeId();
			if (blockAbove == 64 || blockAbove == 71) {
				event.getBlock().getRelative(BlockFace.UP).setTypeId(0);
			}
			if (blockBelow == 64 || blockBelow == 71) {
				event.getBlock().getRelative(BlockFace.DOWN).setTypeId(0);
			}
		}
	}

	// PLACE POWERED RESTONE LAMPS
	@EventHandler(priority = EventPriority.HIGH)
	private void lampPlace(BlockRedstoneEvent event) {
		if (lamps
				&& event.getBlock().getType().equals(Material.REDSTONE_LAMP_ON)) {
			event.setNewCurrent(16);
		}
	}

	// NOPHYSICS HANDLER FOR DRAGON EGGS AND HALF-DOORS
	@EventHandler(priority = EventPriority.HIGH)
	private void doorPlace(BlockPhysicsEvent event) {
		if (noPhysics) {
			int npBlockType = event.getBlock().getTypeId();
			if (noPhysList.contains(npBlockType)) {
				event.setCancelled(true);
			}
		}
		if(doors){
			if(event.getBlock().getTypeId() == 64
					|| event.getBlock().getTypeId() == 71){
				event.setCancelled(true);
			}
		}
	}
}
