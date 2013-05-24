package com.dags.BuildFixes;

import static com.dags.BuildFixes.BuildFixes.doors;
import static com.dags.BuildFixes.BuildFixes.eggBreak;
import static com.dags.BuildFixes.BuildFixes.lamps;
import static com.dags.BuildFixes.BuildFixes.logs;
import static com.dags.BuildFixes.BuildFixes.noPhysics;
import static com.dags.BuildFixes.BuildFixes.noPhysList;

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

public class EventListener implements Listener{
	
	@EventHandler(priority = EventPriority.HIGH)
	private void playerInteract(PlayerInteractEvent event) {
		//DRAGON EGG INTERACTION BLOCKER!
		if(eggBreak){
			if(!(event.getPlayer().hasPermission("BuildFixes.eggbreak"))){
				if(  (event.getAction().equals(Action.LEFT_CLICK_BLOCK) 
					|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					&& event.getClickedBlock().getType().equals(Material.DRAGON_EGG)  ){
					event.setCancelled(true);
				}
			}
		}
	}
	
    @EventHandler(priority = EventPriority.HIGH)
    private void blockPlace(BlockPlaceEvent event) {   
    	Player player = event.getPlayer();
    	//PLACE COVERED LOGS!
    	if(logs){
    		if(player.hasPermission("BuildFixes.logs")
    			&& player.getItemInHand().getType().equals(Material.LOG)){
    			int logData = player.getItemInHand().getData().getData();
    			if(logData >= 12){
    				event.getBlock().setData((byte) logData);
    			}
    		}
    	}
    	//PLACE HALF DOORS!
    	if(player.hasPermission("BuildFixes.doors") && doors){
    		int blockType = player.getItemInHand().getTypeId();
    		int yaw = (int) player.getLocation().getYaw();
    		if(blockType == 64 || blockType == 71){
    			//NORTH
    			if(  yaw > -225
    					&& yaw < -135){
    				int dirData = 3;
    				event.getBlockPlaced().setTypeId(blockType);
    				event.getBlockPlaced().setTypeIdAndData(blockType, (byte) dirData, false);
    				event.getBlockPlaced().getRelative(BlockFace.UP).setTypeIdAndData(blockType, (byte) dirData, false);
    			}
    			//EAST
    			if(yaw >= -135
    					&& yaw < -45){
    				int dirData = 0;
    				event.getBlockPlaced().setTypeId(blockType);
    				event.getBlockPlaced().setTypeIdAndData(blockType, (byte) dirData, false);
    				event.getBlockPlaced().getRelative(BlockFace.UP).setTypeIdAndData(blockType, (byte) dirData, false);
    			}
    			//SOUTH
    			if((yaw > -45
    					&& yaw < 0)
    					||(yaw > -360
    							&& yaw < -315)){
    				int dirData = 1;
    				event.getBlockPlaced().setTypeId(blockType);
    				event.getBlockPlaced().setTypeIdAndData(blockType, (byte) dirData, false);
    				event.getBlockPlaced().getRelative(BlockFace.UP).setTypeIdAndData(blockType, (byte) dirData, false);
    			}
    			//WEST
    			if(yaw > -315
    					&& yaw < -225){
    				int dirData = 2;
    				event.getBlockPlaced().setTypeId(blockType);
    				event.getBlockPlaced().setTypeIdAndData(blockType, (byte) dirData, false);
    				event.getBlockPlaced().getRelative(BlockFace.UP).setTypeIdAndData(blockType, (byte) dirData, false);
    			}
    		}
    	}
    }
    
    //BREAK EVENT FOR HALF-DOORS (ALSO REMOVES INVISIBLE HALF)
    @EventHandler(priority = EventPriority.HIGH)
    private void blockBreak(BlockBreakEvent event) {
    	int doorType = event.getBlock().getTypeId();
    	if(doorType == 64 || doorType == 71){
    		int blockAbove = event.getBlock().getRelative(BlockFace.UP).getTypeId();
    		int blockBelow = event.getBlock().getRelative(BlockFace.DOWN).getTypeId();
    		if(blockAbove == 64 || blockAbove == 71){
    			event.getBlock().getRelative(BlockFace.UP).setTypeId(0);
    		}
    		if(blockBelow == 64 || blockBelow ==71){
    			event.getBlock().getRelative(BlockFace.DOWN).setTypeId(0);
    		}
    	} 
    }
    
    //PLACE POWERED RESTONE LAMPS
    @EventHandler(priority = EventPriority.HIGH)
    private void lampPlace(BlockRedstoneEvent event) {
    	if(lamps && event.getBlock().getType().equals(Material.REDSTONE_LAMP_ON)){
    		event.setNewCurrent(16);
    	}
    }
    
    //NOPHYSICS HANDLER FOR DRAGON EGGS AND HALF-DOORS
    @EventHandler(priority = EventPriority.HIGH)
    private void doorPlace(BlockPhysicsEvent event) {
    	if(noPhysics){
    		int npBlockType = event.getBlock().getTypeId();
    		if(noPhysList.contains(npBlockType)){
    			event.setCancelled(true);
    		}
    	}
    }
}
