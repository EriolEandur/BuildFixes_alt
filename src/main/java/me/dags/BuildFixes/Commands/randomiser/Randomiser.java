/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.Commands.randomiser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dags.BuildFixes.BuildFixes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Eriol_Eandur
 */
public class Randomiser implements CommandExecutor {

    private final Map<OfflinePlayer, RandomiserConfig> configList = new HashMap<OfflinePlayer,RandomiserConfig>();
    
    private final static Set<Material> allowedMaterials = new HashSet<Material>();
    
    private static File configFile;
    
    private static final String configPathAllowedMaterials = "allowed Materials";
    
    private final JavaPlugin buildFixes;
    
    public Randomiser(JavaPlugin buildFixes) {
        configFile = new File(buildFixes.getDataFolder(), "randomiser.yml");
        this.buildFixes = buildFixes;
        loadAllowedMaterials();
    }
    
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("This command can only be run by a player");
            return true;
        } else {
            Player p = (Player) cs;
            if (c.equalsIgnoreCase("random")) {
                RandomiserConfig playerConfig =  getPlayerConfig(p);
                if(args.length>0 && (args[0].equalsIgnoreCase("allow") || args[0].equalsIgnoreCase("deny"))){
                    if(!cs.hasPermission("BuildFixes.randomiser.allowMaterials")){
                        sendNoPermissionErrorMessage(cs);
                        return true;
                    }
                    else {
                        if(args.length<2) {
                            sendMissingArgumentErrorMessage(cs);
                            return true;
                        }
                        else {
                            for(int i = 1; i<args.length;i++) {
                                setAllowed(args[i], args[0].equalsIgnoreCase("allow"));
                            }
                            sendMaterialsInfoMessage(cs);
                            saveAllowedMaterials();
                            return true;
                        }
                    }
                }
                if (!cs.hasPermission("BuildFixes.randomiser.user")) {
                    sendNoPermissionErrorMessage(cs);
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
                    sendRadiusSetMessage(cs);
                    sendInfoMessage(cs,playerConfig);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("range")) {
                    if(!checkArgs(cs, args,3)) return true;
                    playerConfig.setDataValueRange(getInteger(cs, args[1]),
                                                   getInteger(cs, args[2]));
                    sendRangeSetMessage(cs);
                    sendInfoMessage(cs,playerConfig);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("prob")) {
                    if(!checkArgs(cs, args,playerConfig.countDataValues()+1)) return true;
                    int[] newProbs = new int[playerConfig.countDataValues()];
                    for(int i=1; i<args.length;i++) {
                        newProbs[i-1] = getInteger(cs, args[i]);
                    }
                    playerConfig.setProbs(newProbs);
                    sendProbsSetMessage(cs);
                    sendInfoMessage(cs,playerConfig);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("material")) {
                    if(!checkArgs(cs, args,2)) return true;
                    String[] materials= new String[args.length-1];
                    for(int i=1; i<args.length;i++) {
                        materials[i-1] = args[i];
                    }
                    if(playerConfig.setMaterials(materials)) {
                        sendMaterialsSetMessage(cs);
                    }
                    else {
                        sendMaterialsErrorMessage(cs);
                    }
                    sendInfoMessage(cs,playerConfig);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("show")) {
                    sendInfoMessage(cs, playerConfig);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("showAllowed")) {
                    sendMaterialsInfoMessage(cs);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("help")) {
                    sendHelpMessage(cs);
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
    
    public static boolean isAllowed(Material mat) {
        return allowedMaterials.contains(mat);
    }
    
    private void setAllowed(String name, boolean allow) {
        Material material = Material.matchMaterial(name);
        if(material != null) {
            if(allow) {
                allowedMaterials.add(material);
            }
            else {
                allowedMaterials.remove(material);
            }
        }
    }
    
    public void saveAllowedMaterials() {
        FileConfiguration config = new YamlConfiguration();
        List<String> materialNames = new ArrayList<String>();
        for(Material mat : allowedMaterials) {
            materialNames.add(mat.name());
        }
        config.set(configPathAllowedMaterials, materialNames);
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(BuildFixes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public final void loadAllowedMaterials() {
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException ex) {
            Logger.getLogger(BuildFixes.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(BuildFixes.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        List<String> materialNames = config.getStringList(configPathAllowedMaterials);
        for(String name : materialNames) {
            Material material= Material.matchMaterial(name);
            if(material!=null) {
                allowedMaterials.add(material);
            } 
            else {
                Logger.getLogger(BuildFixes.class.getName()).log(Level.WARNING,"Material not found.");
            }
        }
    }
    
    private void sendMissingArgumentErrorMessage(CommandSender cs) {
        cs.sendMessage("You're missing arguments for this command.");
    }

    private void sendNoPermissionErrorMessage(CommandSender cs) {
        cs.sendMessage("Sorry you don't have permission.");
    }

    private void sendNotANumberErrorMessage(CommandSender cs) {
        cs.sendMessage("Not a number.");
   }

    private void sendInvalidSubcommand(CommandSender cs) {
        cs.sendMessage("Invalid subcommand.");
    }
    
    private void sendHelpMessage(CommandSender cs) {
        cs.sendMessage("Tool for randomising data values:");
        cs.sendMessage("- Randomise your surroundings: /random");
        cs.sendMessage("- Set affected radius:     /random radius <radius>");
        cs.sendMessage("- Set placed data values: /random range <min> <max>");
        cs.sendMessage("- Set affected materials:  /random material <name>,[name]...");
        cs.sendMessage("- Set probabilities:         /random prob <prob>, [prob]...");
        cs.sendMessage("- Show configuration:      /random show");
        cs.sendMessage("- Show allowed materials: /random showAllowed");
        cs.sendMessage("- Add allowed materials:   /random allow");
        cs.sendMessage("- Remove allowed materials: /random deny");
    }
    
    private void sendInfoMessage(CommandSender cs, RandomiserConfig playerConfig) {
                    cs.sendMessage("randomiser tool configuration:");
                    cs.sendMessage("Radius: "+playerConfig.getRadius());
                    cs.sendMessage("Material: "+playerConfig.getMaterials());
                    cs.sendMessage("DataValueRange: "+playerConfig.getMinValue()+" "
                                                     +playerConfig.getMaxValue());
                    cs.sendMessage("Probabilities: "+playerConfig.getProbs());
    }
    
    private void sendMaterialsInfoMessage(CommandSender cs) {
        String matList = "";
        for(Material material: allowedMaterials) {
            matList = matList+material.name()+" ";
        }
        cs.sendMessage("Allowed materials: "+ matList);
    }

    private void sendRadiusSetMessage(CommandSender cs) {
        cs.sendMessage("Radius set (max is 150).");
    }

    private void sendProbsSetMessage(CommandSender cs) {
        cs.sendMessage("Probabilities set.");
    }

    private void sendRangeSetMessage(CommandSender cs) {
        cs.sendMessage("Range of data value set.");
    }

    private void sendMaterialsSetMessage(CommandSender cs) {
        cs.sendMessage("Materials set.");
    }

    private void sendMaterialsErrorMessage(CommandSender cs) {
        cs.sendMessage("Some Materials were not found or are not allowed to be ranomised.");
    }

   

}
