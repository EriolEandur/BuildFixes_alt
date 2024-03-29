/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.armorStandEditor;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

/**
 *
 * @author Eriol_Eandur
 */
public class ArmorStandEditorListener implements Listener {

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if(p.getItemInHand().getType().equals(Material.ARMOR_STAND) 
                && !p.hasPermission(ArmorStandEditorCommand.PERMISSION)
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            p.sendMessage(ChatColor.RED+"Sorry, you are not allowed to place armor stands.");
        }
        ArmorStandEditorConfig config = ArmorStandEditorCommand.getPlayerConfig(p);
        if(config.getEditorMode().equals(ArmorStandEditorMode.PASTE)
                && (p.getItemInHand().getType().equals(Material.STICK))) {
            Location loc;
            boolean exact;
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                loc = event.getClickedBlock().getRelative(BlockFace.UP, 1).getLocation();
                exact = false;
            }
            else {
                loc = p.getLocation();
                exact = true;
            }
            config.placeArmorStand(loc, exact);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof ArmorStand) {
            if(!(event.getDamager() instanceof Player)
                    || !((Player)event.getDamager()).hasPermission(ArmorStandEditorCommand.PERMISSION)) {
                event.setCancelled(true);
                if(event.getDamager() instanceof Player) {
                    ((Player)event.getDamager()).sendMessage(ChatColor.RED+"Sorry, you are not allowed to remove armor stands.");
                }
                return;
            }
            event.setCancelled(event.isCancelled() 
                    | manipulate((ArmorStand)event.getEntity(), (Player) event.getDamager(), false));
        } 
    }
    
    @EventHandler
    public void PlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked() instanceof ArmorStand) {
            if(!event.getPlayer().hasPermission(ArmorStandEditorCommand.PERMISSION)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED+"Sorry, you are not allowed to interact with armor stands.");
                return;
            }
            event.setCancelled(event.isCancelled() 
                    | manipulate((ArmorStand) event.getRightClicked(),event.getPlayer(),true));
        }
    }
    
    private boolean manipulate(ArmorStand armorStand, Player player, boolean rightClick) {
        ArmorStandEditorConfig config = ArmorStandEditorCommand.getPlayerConfig(player);
        ArmorStandEditorMode mode = config.getEditorMode();
        int stepInDegree = config.getRotationStep();
        if(!(player.getItemInHand().getType().equals(Material.STICK)
                || mode.equals(ArmorStandEditorMode.HAND))) {
            return false;
        }
        ArmorStandPart part = config.getPart();
        ArmorStandEditorMode modifiedMode = mode;
        switch(mode) {
            case HAND:
                if(!rightClick) {
                    armorStand.setItemInHand(null);
                    break;
                }
                ItemStack oldItem = armorStand.getItemInHand();
                ItemStack newItem = new ItemStack(player.getItemInHand());
                newItem.setAmount(1);
                armorStand.setItemInHand(newItem);
                if(player.getGameMode().equals(GameMode.SURVIVAL)) {
                    int amount = player.getItemInHand().getAmount();
                    if(amount==1) {
                        player.setItemInHand(null);
                    }
                    else {
                        player.getItemInHand().setAmount(amount-1);
                    }
                    player.getInventory().addItem(oldItem);
                }
                break;
            case ROTATE:
                Location playerLoc = player.getLocation();
                if(playerLoc.getPitch()>45 || playerLoc.getPitch()<-45) {
                    modifiedMode = ArmorStandEditorMode.YROTATE;
                    if(playerLoc.getPitch()<-45) {
                        rightClick=!rightClick;
                    }
                }
                else 
                {
                    float yaw = playerLoc.getYaw() - armorStand.getLocation().getYaw();
                    while(yaw<-180) {
                        yaw+=360;
                    }
                    while(yaw>180) {
                        yaw-=360;
                    }
                    if((yaw<45 && yaw>-45) || (yaw>135 || yaw<-135)) {
                        modifiedMode = ArmorStandEditorMode.ZROTATE;
                        if(yaw<45 && yaw>-45) {
                            rightClick=!rightClick;
                        }
                    } else {
                        modifiedMode = ArmorStandEditorMode.XROTATE;
                        if(yaw>45 && yaw<135) {
                            rightClick=!rightClick;
                        }
                    }
                }
            case XROTATE:
            case YROTATE:
            case ZROTATE:
                switch(part) {
                    case HEAD:
                        armorStand.setHeadPose(rotate(modifiedMode,rightClick,stepInDegree,armorStand.getHeadPose()));
                        break;
                    case RARM:
                        armorStand.setRightArmPose(rotate(modifiedMode,rightClick,stepInDegree,armorStand.getRightArmPose()));
                        break;
                    case LARM:
                        armorStand.setLeftArmPose(rotate(modifiedMode,rightClick,stepInDegree,armorStand.getLeftArmPose()));
                        break;
                    case LLEG:
                        armorStand.setLeftLegPose(rotate(modifiedMode,rightClick,stepInDegree,armorStand.getLeftLegPose()));
                        break;
                    case RLEG:
                        armorStand.setRightLegPose(rotate(modifiedMode,rightClick,stepInDegree,armorStand.getRightLegPose()));
                        break;
                    case BODY:
                        armorStand.setBodyPose(rotate(modifiedMode,rightClick,stepInDegree,armorStand.getRightLegPose()));
                }
                break;
            case TURN:
                armorStand.teleport(turn(armorStand.getLocation(),rightClick,stepInDegree));
                break;
            case XMOVE:
                armorStand.teleport(moveX(armorStand.getLocation(),rightClick,stepInDegree));
                break;
            case YMOVE:
                armorStand.teleport(moveY(armorStand.getLocation(),rightClick,stepInDegree));
                break;
            case ZMOVE:
                armorStand.teleport(moveZ(armorStand.getLocation(),rightClick,stepInDegree));
                break;
            case MOVE:
                playerLoc = player.getLocation();
                float yaw = playerLoc.getYaw();
                while(yaw<0) {
                    yaw+=360;
                }
                while(yaw>360) {
                    yaw-=360;
                }
                if(yaw<45 || yaw>315 || (yaw>135 && yaw<225)) {
                    if(yaw>135 && yaw<225) {
                        rightClick=!rightClick;
                    }
                    armorStand.teleport(moveX(armorStand.getLocation(),rightClick,stepInDegree));
                } else {
                    if(!(yaw>45 && yaw<135)) {
                        rightClick=!rightClick;
                    }
                    armorStand.teleport(moveZ(armorStand.getLocation(),rightClick,stepInDegree));
                }
                break;
            case SIZE:
                armorStand.setSmall(!armorStand.isSmall());
                player.sendMessage("Switched armor stand size.");
                break;
            case VISIBLE:
                armorStand.setVisible(!armorStand.isVisible());
                player.sendMessage("Switched visibility.");
                break;
            case GRAVITY:
                armorStand.setGravity(!armorStand.hasGravity());
                player.sendMessage("Switched armor stand affected by gravity.");
                break;
            case BASE:
                armorStand.setBasePlate(!armorStand.hasBasePlate());
                player.sendMessage("Switched armor stand base plate.");
                break;
            case MARKER:
                armorStand.setMarker(!armorStand.isMarker());
                player.sendMessage("Switch ");
                break;
            case ARMS:
                armorStand.setArms(!armorStand.hasArms());
                player.sendMessage("Switched armor stand arms.");
                break;
            case PASTE:
                //nothing to do here
                break;
            case COPY:
                config.copyArmorStand(armorStand);
                sendCopyMessage(player);
                break;
        }
        return true;
    }

    private EulerAngle rotate(ArmorStandEditorMode mode, boolean positive, int stepInDegree, EulerAngle previous) {
        double step = stepInDegree*3.1412/180;
        if(!positive) {
            step *= -1;
        }
        switch(mode) {
            case XROTATE:
                return new EulerAngle(previous.getX()+step,previous.getY(),previous.getZ());
            case YROTATE:
                return new EulerAngle(previous.getX(),previous.getY()+step,previous.getZ());
            case ZROTATE:
                return new EulerAngle(previous.getX(),previous.getY(),previous.getZ()+step);
            default:
                return EulerAngle.ZERO;
        }
    }
    
    private Location turn(Location oldLoc, boolean positive, int stepInDegree) {
        float step = stepInDegree;
        if(!positive) {
            step *= -1;
        }
        oldLoc.setYaw(oldLoc.getYaw()+step);
        return oldLoc;
    }

    private Location moveX(Location location, boolean rightClick, int stepInDegree) {
        double step = stepInDegree/100d;
        if(!rightClick) {
            step *= -1;
        }
        location.setX(location.getX()+step);
        return location;
    }

    private Location moveY(Location location, boolean rightClick, int stepInDegree) {
        double step = stepInDegree/100d;
        if(!rightClick) {
            step *= -1;
        }
        location.setY(location.getY()+step);
        return location;
    }

    private Location moveZ(Location location, boolean rightClick, int stepInDegree) {
        double step = stepInDegree/100d;
        if(!rightClick) {
            step *= -1;
        }
        location.setZ(location.getZ()+step);
        return location;
    }

    private void sendCopyMessage(Player player) {
        player.sendMessage("Armor stand copied to clippboard.");
    }

}