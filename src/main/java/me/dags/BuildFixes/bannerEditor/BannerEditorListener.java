/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.bannerEditor;

import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Eriol_Eandur
 */
public class BannerEditorListener implements Listener {
    
    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if(player.hasPermission(BannerEditorCommand.PERMISSION) 
                && player.getItemInHand().getType().equals(Material.STICK)) {
            BlockState state = event.getClickedBlock().getState();
            if(state instanceof Banner) {
                Banner banner = (Banner) state;
                BannerEditorConfig config = BannerEditorCommand.getPlayerConfig(event.getPlayer());
                BannerEditorMode mode = config.getEditorMode();
                int patternId = config.getPatternId();
                switch(mode) {
                    case LIST:
                        sendBannerInfoMessage(player,banner);
                        break;
                    case PATTERN:
                        if(patternId>0 
                                && patternId<=banner.numberOfPatterns()) {
                            banner.setPattern(patternId-1,
                                    new Pattern(banner.getPattern(patternId-1).getColor(),
                                                (PatternType) cycle(banner.getPattern(patternId-1).getPattern(),
                                                                    event.getAction())));
                            banner.update(true, false);
                        }
                        else if(patternId==0) {
                            sendNoPattern(player);
                        }
                        else {
                            sendInvalidPatternId(player,patternId);
                        }
                        break;
                    case COLOR:
                        if(patternId>0 
                                && patternId<=banner.numberOfPatterns()) {
                            banner.setPattern(patternId-1,
                                    new Pattern((DyeColor) cycle(banner.getPattern(patternId-1).getColor(),
                                                                 event.getAction()),
                                                banner.getPattern(patternId-1).getPattern()));
                            banner.update(true, false);
                        }
                        else if(patternId == 0) {
                            banner.setBaseColor((DyeColor) cycle(banner.getBaseColor(),
                                                                 event.getAction()));
                            banner.update(true, false);
                        }
                        else {
                            sendInvalidPatternId(player,patternId);
                        }
                        break;
                    case ADD:
                        banner.addPattern(new Pattern(DyeColor.WHITE,PatternType.CIRCLE_MIDDLE));
                        banner.update(true, false);
                        break;
                    case REMOVE:
                        if(patternId>0 
                                && patternId<banner.numberOfPatterns()) {
                            banner.removePattern(patternId-1);
                        }
                        else {
                            sendInvalidPatternId(player, patternId);
                        }
                        break;
                }
                event.setCancelled(true);
            }
        }
    }

    private PatternType cycle(PatternType current, Action direction) {
        PatternType[] types = PatternType.values();
        int ordinal = current.ordinal();
        return (PatternType) cycle(types, ordinal, direction);
    }
    
    private DyeColor cycle(DyeColor current, Action direction) {
        DyeColor[] types = DyeColor.values();
        int ordinal = current.ordinal();
        return (DyeColor) cycle(types, ordinal, direction);
    }
    
    private Object cycle(Object[] types, int ordinal, Action direction) {
        if(direction.equals(Action.LEFT_CLICK_BLOCK)) {
            ordinal++;
            if(ordinal==types.length) {
                ordinal = 0;
            }
        }
        else {
            ordinal--;
            if(ordinal<0) {
                ordinal = types.length-1;
            }
        }
        return types[ordinal];
    }

    private void sendInvalidPatternId(Player player, int id) {
        player.sendMessage("This banner doesn't have "+ id + " patterns.");
    }
    
    private void sendBannerInfoMessage(Player player, Banner banner) {
        List<Pattern> patterns = banner.getPatterns();
        player.sendMessage("Base color (ID 0): "+ banner.getBaseColor().toString());
        int id = 1;
        for(Pattern pattern: patterns) {
            player.sendMessage("ID "+ id+": "+ pattern.getPattern().toString()+" - "
                                           + pattern.getColor().toString());
            id++;
        }
    }
    
    private void sendNoPattern(Player player) {
        player.sendMessage("You can change the color of the base banner only.");
    }

}