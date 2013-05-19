package com.dags.BuildFixes;

import static com.dags.BuildFixes.BuildFixes.eggBreak;
import static com.dags.BuildFixes.BuildFixes.lamps;
import static com.dags.BuildFixes.BuildFixes.logs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListener implements Listener{
	
	static int logData;
	
	@EventHandler(priority = EventPriority.HIGH)
	private void playerInteract(PlayerInteractEvent event) {
		//DRAGON EGG INTERACTION BLOCKER!
		if(eggBreak){
			if(!(event.getPlayer().hasPermission("BuildFixes.dragonegg"))){
				if(  (event.getAction().equals(Action.LEFT_CLICK_BLOCK) 
					|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					&& event.getClickedBlock().getType().equals(Material.DRAGON_EGG)  ){
					event.setCancelled(true);
				}
			}
		}
	}
	
    @EventHandler(priority = EventPriority.HIGH)
    private void blockPhysics(BlockPhysicsEvent event) { 
    	if(event.getBlock().getType().equals(Material.DRAGON_EGG)){
    		event.setCancelled(true);
    	}
    }
	
    @EventHandler(priority = EventPriority.HIGH)
    private void blockPlace(BlockPlaceEvent event) {   
    	Player player = event.getPlayer();
    	//PLACE COVERED LOGS!
    	if(logs){
    		if(player.hasPermission("BuildFixes.logs")
    			&& player.getItemInHand().getType().equals(Material.LOG)){
    			logData = player.getItemInHand().getData().getData();
    			if(logData >= 12){
    				event.getBlock().setData((byte) logData);
    			}
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
}
