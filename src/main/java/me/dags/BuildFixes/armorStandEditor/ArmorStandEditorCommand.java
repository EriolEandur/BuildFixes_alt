/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.armorStandEditor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
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
                    if(args[0].equalsIgnoreCase("files")) {
                        int page=1;
                        boolean numberFound = false;
                        if(args.length>1) {
                            try {
                                if(args.length>2) {
                                    page = Integer.parseInt(args[2]);
                                }
                                else {
                                    page = Integer.parseInt(args[1]);
                                    numberFound = true;
                                }
                            } catch (NumberFormatException ex) {}
                        }
                        if((args.length>1 && !numberFound) || args.length==3) {
                            showFiles(cs, playerConfig, args[1],page);
                        }
                        else {
                            showFiles(cs, playerConfig, "",page);
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("delete")) {
                        if(args.length>1) {
                            if(playerConfig.deleteFile(args[1])) {
                                sendFileDeletedMessage(cs);
                            }
                            else {
                                sendDeleteErrorMessage(cs);
                            }
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("save")) {
                        if(args.length>2) {
                            String description = args[2];
                            for(int i = 3;i<args.length;i++) {
                                description = description + " " + args[i];
                            }
                            try {
                                if(playerConfig.saveArmorStand(args[1],description)) {
                                    sendSavedMessage(cs);
                                }
                                else {
                                    sendExistsMessage(cs);
                                }
                            } catch (IOException ex) {
                                sendIOErrorMessage(cs);
                            }
                        }
                        else
                        {
                            sendNotEnoughArgumentsMessage(cs);
                        }
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
                        if(editorMode.equals(ArmorStandEditorMode.PASTE) && args.length>1) {
                            try {
                                if(playerConfig.loadArmorStand(args[1])) {
                                    sendLoadedMessage(cs);
                                }
                                else {
                                    sendFileNotFoundMessage(cs);
                                }
                            } catch (IOException ex) {
                                sendIOErrorMessage(cs);
                            } catch (InvalidConfigurationException ex) {
                                sendIOErrorMessage(cs);
                            }
                        }
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
        
    private void showFiles(CommandSender cs, ArmorStandEditorConfig playerConfig, String folder, int page) {
        File[] files = playerConfig.getFiles(folder);
        int maxPage=(files.length-1)/10+1;
        if(maxPage<1) {
            maxPage = 1;
        }
        if(page>maxPage) {
            page = maxPage;
        }
        sendHeaderMessage(cs, folder, page, maxPage);
        if(!folder.equals("")) {
            sendClickableMessage((Player) cs, "   ..","/armor files");
        }
        for(int i = (page-1)*10; i<files.length && i<(page-1)*10+10;i++) {
                //backward order: int i = files.length-1-(page-1)*10; i >= 0 && i > files.length-1-(page-1)*10-10; i--) {
            sendEntryMessage(cs, folder, files[i], playerConfig.getDescription(files[i]));
        }
    }
    
    private void sendHeaderMessage(CommandSender cs, String folder, int page, int maxPage) {
        cs.sendMessage("Saved armor stand files "
                       +(!folder.equals("")?"in folder "+ folder+" ":"")+"[page " +page+"/"+maxPage+"]");
    }

    private void sendEntryMessage(CommandSender cs, String folder, File file, String description) {
        String name;
        String command;
        if(file.isDirectory()) {
            name = file.getName();
            command = "/armor files "+folder+"/"+name;
        }
        else {
            name = file.getName().substring(0, file.getName().lastIndexOf('.'));
            command = "/armor p "+folder+"/"+name;
        }
        while(name.length()<15) {
            name = name.concat(" ");
        }
        sendClickableMessage((Player)cs, "   "+name+description, command);
    }
    
    private void sendNoPermissionErrorMessage(CommandSender cs) {
        cs.sendMessage("Sorry, you don't have permission.");
    }

    private void sendInvalidSubcommand(CommandSender cs) {
        cs.sendMessage("Invalid subcommand.");
    }
    
    private void sendHelpMessage(CommandSender cs) {
        cs.sendMessage("Tool for editing armor stands:");
        cs.sendMessage("- Show the current mode:    /armor");
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
        cs.sendMessage("- Select paste mode:        /armor p [filename]");
        cs.sendMessage("- Select copy mode:         /armor c");
        cs.sendMessage("- Increase rot/move step:   /armor +");
        cs.sendMessage("- Decrease rot/move step:   /armor -");
        cs.sendMessage("- Stet rot/move step:       /armor #");
        cs.sendMessage("- Place copied armor stand: /armor place");
        cs.sendMessage("- Clear copied armor stand: /armor clear");
        cs.sendMessage("- Save copied armor stand:  /armor save <filename> <description>");
        cs.sendMessage("- List saved armor stand:   /armor files [folder]");
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

    private void sendNotEnoughArgumentsMessage(CommandSender cs) {
        cs.sendMessage("Not enough arguments: /armor save <filename> <description>");
    }

    private void sendSavedMessage(CommandSender cs) {
        cs.sendMessage("Armor stand saved.");
    }

    private void sendFileNotFoundMessage(CommandSender cs) {
        cs.sendMessage("File not found.");
    }

    private void sendLoadedMessage(CommandSender cs) {
        cs.sendMessage("Armor stand loaded.");
    }

    private static void sendClickableMessage(Player sender, String message, String onClickCommand) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw "+ sender.getName()+" "
                    +"{ text:\""+message+"\", "
                      +"clickEvent:{ action:run_command,"
                                   + "value:\""+ onClickCommand +"\"}}");
    }

    private void sendExistsMessage(CommandSender cs) {
        cs.sendMessage("File already exists. Delete first.");
    }

    private void sendIOErrorMessage(CommandSender cs) {
        cs.sendMessage("IO error. Nothing was saved.");
    }

    private void sendFileDeletedMessage(CommandSender cs) {
        cs.sendMessage("File deleted.");
    }

    private void sendDeleteErrorMessage(CommandSender cs) {
        cs.sendMessage("File not found or directory not empty.");
    }
        

}
