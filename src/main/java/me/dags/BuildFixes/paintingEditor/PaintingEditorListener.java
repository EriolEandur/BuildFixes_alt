/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.paintingEditor;

import org.bukkit.Art;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 *
 * @author Eriol_Eandur
 */
public class PaintingEditorListener implements Listener {
    
    private final static String PERMISSION = "BuildFixes.paintingEditor";
    
    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
    public void playerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if(!(entity instanceof Painting 
                && player.getItemInHand().getType().equals(Material.STICK))) {
            return;
        }
        if(player.hasPermission(PERMISSION)) {
             {
                Painting painting = (Painting) entity;
                int id = painting.getArt().getId();
                if(id<Art.values().length-1) {
                    id++;
                    painting.setArt(Art.getById(id));
                }
            }
        }
        else {
            sendNoPermissionMessage(player);
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
    public void hangingBreakByEntity(HangingBreakByEntityEvent event) {
        Entity damager = event.getRemover();
        Entity entity = event.getEntity();
        if(!(damager instanceof Player && entity instanceof Painting) ) {
            return;
        }
        Player player = (Player) damager;
        if(!player.getItemInHand().getType().equals(Material.STICK)) {
            return;
        }
        if(player.hasPermission(PERMISSION)) {
            Painting painting = (Painting) entity;
            int id = painting.getArt().getId();
            if(id>0) {
                id--;
                painting.setArt(Art.getById(id));
            }
        }
        else {
            sendNoPermissionMessage(player);
        }
        event.setCancelled(true);
    }
        
    private void sendNoPermissionMessage(Player player) {
        player.sendMessage("You don't have permission to do this.");
    }
    
}