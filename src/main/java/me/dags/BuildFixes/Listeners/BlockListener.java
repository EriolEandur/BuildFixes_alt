package me.dags.BuildFixes.Listeners;

import static me.dags.BuildFixes.BuildFixes.noPhysList;
import static me.dags.BuildFixes.BuildFixes.worldsCFG;

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
		Player p = event.getPlayer();
		
		if( worldsCFG.get(p.getWorld().getName()).get(1) ){
			
			if (event.hasBlock() && event.getClickedBlock().getTypeId() == 122) {
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

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled=true)
	private void logPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		int logData = p.getItemInHand().getData().getData();
		
		if(worldsCFG.get(p.getWorld().getName()).get(2)){
			
			if (p.getItemInHand().getTypeId() == 17 && logData >= 12
					&& logData <= 15) {
				if (p.hasPermission("BuildFixes.logs")) {
					event.getBlock().setData((byte) logData);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled=true)
	private void doorPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		
		if (worldsCFG.get(p.getWorld().getName()).get(0)) {
			
			int blockType = p.getItemInHand().getTypeId();
			if (blockType == 64 || blockType == 71) {
				if (p.hasPermission("BuildFixes.doors")) {
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
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	private void doorBreak(BlockBreakEvent event) {
		
		if (worldsCFG.get(event.getPlayer().getWorld().getName()).get(0)) {
			
			int doorType = event.getBlock().getTypeId();
			if (doorType == 64 || doorType == 71) {
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
		
		if (worldsCFG.get(event.getBlock().getWorld().getName()).get(3)){
			
			int npBlockType = event.getBlock().getTypeId();
			if (noPhysList.contains(npBlockType)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void noPhysicsDoors(BlockPhysicsEvent event) {
		
		if (worldsCFG.get(event.getBlock().getWorld().getName()).get(0)) {
			
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
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
