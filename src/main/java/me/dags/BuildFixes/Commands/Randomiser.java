/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.Commands;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class Randomiser implements CommandExecutor {

    private final Map<OfflinePlayer, RandomiserConfig> configList = new HashMap<OfflinePlayer,RandomiserConfig>();
    
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("This command can only be run by a player");
            return true;
        } else {
            Player p = (Player) cs;
            if (c.equalsIgnoreCase("random")) {
                RandomiserConfig playerConfig =  getPlayerConfig(p);
                if (!cs.hasPermission("BuildFixes.randomiser")) {
                    cs.sendMessage("Sorry you don't have permission to use /random");
                    return true;
                }
                if(args.length<1) {
                    randomise(p.getLocation(), playerConfig);
                    cs.sendMessage("Randomised your surroundings.");
                    return true;
                }
                else if(args[0].equalsIgnoreCase("radius")) {
                    if(!checkArgs(cs, args,2)) return true;
                    playerConfig.setRadius(getInteger(cs, args[1]));
                    return true;
                }
                else if(args[0].equalsIgnoreCase("range")) {
                    if(!checkArgs(cs, args,3)) return true;
                    playerConfig.setDataValueRange(getInteger(cs, args[1]),
                                                   getInteger(cs, args[2]));
                    return true;
                }
                else if(args[0].equalsIgnoreCase("prob")) {
                    if(!checkArgs(cs, args,playerConfig.countDataValues()+1)) return true;
                    int[] newProbs = new int[playerConfig.countDataValues()];
                    for(int i=1; i<args.length;i++) {
                        newProbs[i-1] = getInteger(cs, args[i]);
                    }
                    playerConfig.setProbs(newProbs);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("material")) {
                    if(!checkArgs(cs, args,2)) return true;
                    String[] materials= new String[args.length-1];
                    for(int i=1; i<args.length;i++) {
                        materials[i-1] = args[i];
                    }
                    playerConfig.setMaterials(materials);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("show")) {
                    cs.sendMessage("randomiser tool configuration:");
                    cs.sendMessage("Radius: "+playerConfig.getRadius());
                    cs.sendMessage("Material: "+playerConfig.getMaterials());
                    cs.sendMessage("DataValueRange: "+playerConfig.getMinValue()+" "
                                                     +playerConfig.getMaxValue());
                    cs.sendMessage("Probabilities: "+playerConfig.getProbs());
                    return true;
                }
                else if(args[0].equalsIgnoreCase("help")) {
                    cs.sendMessage("Tool for randomising data values:");
                    cs.sendMessage("- Randomise your surroundings: /random");
                    cs.sendMessage("- Set affected radius:     /random radius <radius>");
                    cs.sendMessage("- Set placed data values: /random range <min> <max>");
                    cs.sendMessage("- Set affected materials:  /random materials <name>,[name]...");
                    cs.sendMessage("- Set probabillities:         /random probs <prob>, [prob]...");
                    cs.sendMessage("- Show configuration:      /random show");
                    return true;
                }
               else {
                    sendInvalidSubcommand(cs);
                    return true;
                }
            }
        }
        return false;
    }
    
    private RandomiserConfig getPlayerConfig(OfflinePlayer p) {
        for(OfflinePlayer search: configList.keySet()) {
            if(search.getUniqueId().equals(p.getUniqueId())) {
                return configList.get(search);
            }
        }
        RandomiserConfig newConfig = new RandomiserConfig();
        configList.put(p, newConfig);
        return newConfig;
    }
        
    private void randomise(Location center, RandomiserConfig pConfig) {
        int radius = pConfig.getRadius();
        for(int i = center.getBlockX()-radius; i<center.getBlockX()+radius; i++) {
            for(int j = center.getBlockY()-radius; j<center.getBlockY()+radius; j++) {
                for(int k = center.getBlockZ()-radius; k<center.getBlockZ()+radius;k++) {
                    Location loc = new Location(center.getWorld(),i,j,k);
                    if(center.distance(loc)<radius) {
                        Block block = loc.getBlock();
                        if(pConfig.isIn(block.getType())) {
                            block.setData(pConfig.randomDataValue());
                        }
                    }
                }
            }
        }
    }
    
    private int getInteger(CommandSender cs, String str) {
        try {
            return Integer.parseInt(str);
        }
        catch(NumberFormatException e) {
            sendNotANumberErrorMessage(cs);
            return -1;
        }
    }
    
    private boolean checkArgs(CommandSender cs, String[] args, int count) {
        if(args.length<count) {
            sendMissingArgumentErrorMessage(cs);
            return false;
        }
        return true;
    }
    
    private void sendMissingArgumentErrorMessage(CommandSender cs) {
        cs.sendMessage("You're missing arguments for this command.");
    }

    private void sendNotANumberErrorMessage(CommandSender cs) {
        cs.sendMessage("Not a number.");
   }

    private void sendInvalidSubcommand(CommandSender cs) {
        cs.sendMessage("Invalid subcommand.");
    }
    

}
