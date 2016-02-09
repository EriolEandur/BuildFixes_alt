/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.armorStandEditor;

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
public class ArmorStandEditorCommand implements CommandExecutor {

    private final static Map<OfflinePlayer, ArmorStandEditorConfig> configList = new HashMap<OfflinePlayer,ArmorStandEditorConfig>();
    
    public static final String PERMISSION ="BuildFixes.armorStandEditor";
    
    private final int maxStep = 100;
    
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
            if (c.equalsIgnoreCase("armor")) {
                ArmorStandEditorConfig playerConfig =  getPlayerConfig(p);
                if(args.length<1) {
                    sendInfoMessage(cs, playerConfig);
                }
                else {
                    if(args[0].equalsIgnoreCase("help")) {
                        sendHelpMessage(cs);
                        return true;
                    }
                    if(args[0].equals("+") || args[0].equals("-")) {
                        int stepInDegree = playerConfig.getRotationStep();
                        if(args[0].equals("+") && stepInDegree<maxStep) {
                            stepInDegree += 1;
                        }
                        else if(args[0].equals("-") && stepInDegree>2) {
                            stepInDegree -= 1;
                        }
                        playerConfig.setRotationStep(stepInDegree);
                        sendRotationStepMessage(cs,playerConfig.getRotationStep());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("parts")) {
                        sendPartsHelpMessage(cs);
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("place")) {
                        playerConfig.placeArmorStand(p.getLocation(),true);
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("clear")) {
                        playerConfig.clearCopiedArmorStand();
                        sendCopiedArmorStandClearedMessage(cs);
                        return true;
                    }
                    try {
                        int step = Integer.parseInt(args[0]);
                        if(step>maxStep) step = maxStep;
                        if(step<1) step = 1;
                        playerConfig.setRotationStep(step);
                        sendRotationStepMessage(cs,playerConfig.getRotationStep());
                        return true;
                    }
                    catch(NumberFormatException e) {}
                    ArmorStandEditorMode editorMode = ArmorStandEditorMode.getEditorMode(args[0]);
                    if(editorMode == null) {
                        sendInvalidSubcommand(cs);
                    }
                    else {
                        playerConfig.setEditorMode(editorMode);
                        if(args.length>1) {
                            ArmorStandPart part = ArmorStandPart.getPart(args[1]);
                            if(part!=null) {
                                playerConfig.setPart(part);
                            }
                        }
                        sendInfoMessage(cs, playerConfig);
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public static ArmorStandEditorConfig getPlayerConfig(OfflinePlayer p) {
        for(OfflinePlayer search: configList.keySet()) {
            if(search.getUniqueId().equals(p.getUniqueId())) {
                return configList.get(search);
            }
        }
        ArmorStandEditorConfig newConfig = new ArmorStandEditorConfig();
        configList.put(p, newConfig);
        return newConfig;
    }
        
    private void sendNoPermissionErrorMessage(CommandSender cs) {
        cs.sendMessage("Sorry, you don't have permission.");
    }

    private void sendInvalidSubcommand(CommandSender cs) {
        cs.sendMessage("Invalid subcommand.");
    }
    
    private void sendHelpMessage(CommandSender cs) {
        cs.sendMessage("Tool for editing armor stands:");
        cs.sendMessage("- Show help about parts:    /armor parts");
        cs.sendMessage("- Select x-movement mode:   /armor mx");
        cs.sendMessage("- Select y-movement mode:   /armor my");
        cs.sendMessage("- Select z-movement mode:   /armor mz");
        cs.sendMessage("- Select move left/right:   /armor mo");
        cs.sendMessage("- Select turn mode:         /armor t");
        cs.sendMessage("- Select x-rotation mode:   /armor x [part]");
        cs.sendMessage("- Select y-rotation mode:   /armor y [part]");
        cs.sendMessage("- Select z-rotation mode:   /armor z [part]");
        cs.sendMessage("- Select view rotation mode:/armor r [part]");
        cs.sendMessage("- Switch size mode:         /armor s");
        cs.sendMessage("- Switch visibility mode:   /armor v");
        cs.sendMessage("- Switch marker mode:       /armor ma");
        cs.sendMessage("- Switch arms mode:         /armor a");
        cs.sendMessage("- Switch base plate mode:   /armor b");
        cs.sendMessage("- Select place mode:        /armor p");
        cs.sendMessage("- Select copy mode:         /armor c");
        cs.sendMessage("- Increase rot/move step:   /armor +");
        cs.sendMessage("- Decrease rot/move step:   /armor -");
        cs.sendMessage("- Stet rot/move step:       /armor #");
        cs.sendMessage("- Place copied armor stand: /armor place");
        cs.sendMessage("- Clear copied armor stand: /armor clear");
    }
    
    private void sendInfoMessage(CommandSender cs, ArmorStandEditorConfig playerConfig) {
                    cs.sendMessage("armor stand editor mode: ");
                    switch(playerConfig.getEditorMode()) {
                        case HAND:
                            cs.sendMessage("   -> remove/place item in hand");
                            break;
                        case XROTATE:
                            cs.sendMessage("   -> rotate " + playerConfig.getPart().getPartName()+" along x-Axis");
                            break;
                        case YROTATE:
                            cs.sendMessage("   -> rotate " + playerConfig.getPart().getPartName()+" along y-axis");
                            break;
                        case ZROTATE:
                            cs.sendMessage("   -> rotate " + playerConfig.getPart().getPartName()+" along z-axis");
                            break;
                        case ROTATE:
                            cs.sendMessage("   -> rotate " + playerConfig.getPart().getPartName()+" along your view direction");
                            break;
                        case MOVE:
                            cs.sendMessage("   -> move to left/right");
                            break;
                        case TURN:
                            cs.sendMessage("   -> turn full armor stand");
                            break;
                        case XMOVE:
                            cs.sendMessage("   -> move along x-axis");
                            break;
                        case YMOVE:
                            cs.sendMessage("   -> move along y-axis");
                            break;
                        case ZMOVE:
                            cs.sendMessage("   -> move along z-axis");
                            break;
                        case SIZE:
                            cs.sendMessage("   -> switch size");
                            break;
                        case VISIBLE:
                            cs.sendMessage("   -> switch visibility");
                            break;
                        case BASE:
                            cs.sendMessage("   -> switch base plate");
                            break;
                        case MARKER:
                            cs.sendMessage("   -> switch collision box");
                            break;
                        case ARMS:
                            cs.sendMessage("   -> switch arms");
                            break;
                        case PASTE:
                            cs.sendMessage("   -> paste armor stand");
                            break;
                        case COPY:
                            cs.sendMessage("   -> copy armor stand");
                            break;
                    }
    }

    private void sendRotationStepMessage(CommandSender cs, int rotationStep) {
        cs.sendMessage("    -> Set rot/move step to "+rotationStep+"degree/percent");
    }

    private void sendCopiedArmorStandClearedMessage(CommandSender cs) {
        cs.sendMessage("armor stand clippboard was cleared.");
    }

    private void sendPartsHelpMessage(CommandSender cs) {
        cs.sendMessage("parts arguments of an armor stand:");
        cs.sendMessage("    -> h  head");
        cs.sendMessage("    -> la  left arm");
        cs.sendMessage("    -> ra  right arm");
        cs.sendMessage("    -> ll  left leg");
        cs.sendMessage("    -> rl  right leg");
        cs.sendMessage("    -> b   body");
    }
    
}
