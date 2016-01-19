/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.bannerEditor;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class BannerEditorCommand implements CommandExecutor {

    private final static Map<OfflinePlayer, BannerEditorConfig> configList = new HashMap<OfflinePlayer,BannerEditorConfig>();
    
    public static final String PERMISSION ="BuildFixes.bannerEditor";
    
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("This command can only be run by a player");
            return true;
        }
        if(!((Player)cs).hasPermission(PERMISSION)) {
            sendNoPermissionErrorMessage(cs);
            return true;
        } else {
            Player p = (Player) cs;
            if (c.equalsIgnoreCase("banner")) {
                BannerEditorConfig playerConfig =  getPlayerConfig(p);
                if(args.length<1) {
                    sendInfoMessage(cs, playerConfig);
                }
                else {
                    if(args[0].equalsIgnoreCase("help")) {
                        sendHelpMessage(cs);
                        return true;
                    }
                    int id = getInteger(cs, args[0]);
                    if(id>-1) {
                        playerConfig.setPatternId(id);
                        sendInfoMessage(cs, playerConfig);
                        return true;
                    }
                    BannerEditorMode editorMode = BannerEditorMode.getEditorMode(args[0]);
                    if(editorMode == null) {
                        sendInvalidSubcommand(cs);
                    }
                    else {
                        playerConfig.setEditorMode(editorMode);
                        sendInfoMessage(cs, playerConfig);
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public static BannerEditorConfig getPlayerConfig(OfflinePlayer p) {
        for(OfflinePlayer search: configList.keySet()) {
            if(search.getUniqueId().equals(p.getUniqueId())) {
                return configList.get(search);
            }
        }
        BannerEditorConfig newConfig = new BannerEditorConfig();
        configList.put(p, newConfig);
        return newConfig;
    }
        
    private int getInteger(CommandSender cs, String str) {
        try {
            return Integer.parseInt(str);
        }
        catch(NumberFormatException e) {
            return -1;
        }
    }
    
    
    private void sendNoPermissionErrorMessage(CommandSender cs) {
        cs.sendMessage("Sorry you don't have permission.");
    }

    private void sendInvalidSubcommand(CommandSender cs) {
        cs.sendMessage("Invalid subcommand.");
    }
    
    private void sendHelpMessage(CommandSender cs) {
        cs.sendMessage("Tool for editing banners:");
        cs.sendMessage("- Select edited pattern:          /banner <patternId>");
        cs.sendMessage("- Select change pattern mode: /banner p(attern)");
        cs.sendMessage("- Select change color mode:    /banner c(olor)");
        cs.sendMessage("- Select add pattern mode:     /banner a(dd)");
        cs.sendMessage("- Select list patterns mode:    /banner l(ist)");
    }
    
    private void sendInfoMessage(CommandSender cs, BannerEditorConfig playerConfig) {
                    cs.sendMessage("banner editor mode: ");
                    switch(playerConfig.getEditorMode()) {
                        case LIST:
                            cs.sendMessage("   -> list patterns");
                            break;
                        case PATTERN:
                            cs.sendMessage("   -> change pattern "+ playerConfig.getPatternId());
                            break;
                        case COLOR:
                            cs.sendMessage("   -> change color of pattern "+ playerConfig.getPatternId());
                            break;
                        case ADD:
                            cs.sendMessage("   -> add pattern");
                            break;
                        case REMOVE:
                            cs.sendMessage("   -> remove pattern "+ playerConfig.getPatternId());
                            break;
                    }
    }
    
}
