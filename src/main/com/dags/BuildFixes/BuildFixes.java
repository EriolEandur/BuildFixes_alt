package com.dags.BuildFixes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildFixes extends JavaPlugin {
	
	public static boolean logs;
	public static boolean lamps;
	public static boolean eggBreak;
	
	
    @Override
    public void onEnable(){
    	registerEvents();
    	setupConfig();
    }

    @Override
    public void onDisable() {
    }
    
    public void setupConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
    
    public void registerEvents() {
    	logs = getConfig().getBoolean("Settings.SpecialLogs");
    	lamps = getConfig().getBoolean("Settings.PlaceLamps");
    	eggBreak = getConfig().getBoolean("Settings.DragonEggBlocking");
    	this.getServer().getPluginManager().registerEvents(new EventListener(), this);
    }
    
  //GIVE SPECIAL LOGS
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		}
		else{
			Player player = (Player) sender;
			if(label.equalsIgnoreCase("logs")
					&& sender.hasPermission("BuildFixes.logs")){
				if(logs){
					player.getInventory().addItem( new ItemStack(17, 64, (byte) 12 ));
					player.getInventory().addItem(new ItemStack(17, 64, (byte) 13));
					player.getInventory().addItem(new ItemStack(17, 64, (byte) 14));
					player.getInventory().addItem(new ItemStack(17, 64, (byte) 15));
					return true;
				}
				else{
					sender.sendMessage(ChatColor.GRAY + "Feature disabled!");
					return true;
				}
			}
		//ADMIN COMMANDS
			if(sender.hasPermission("BuildFixes.admin") 
					&& label.equalsIgnoreCase("fix")){
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("help")){
						sender.sendMessage(ChatColor.GREEN + "[logs], [lamps], [eggs]");
						return true;
					}
					if(args[0].equalsIgnoreCase("logs")){
						if(logs){
			    			this.getConfig().set("Settings.SpecialLogs", false);
			    			this.saveConfig();
			    			logs = false;
			    			sender.sendMessage(ChatColor.RED + "Disabled!");
			    			return true;
			    		}
			    		else{
			    			this.getConfig().set("Settings.SpecialLogs", true);
			    			this.saveConfig();
			    			logs = true;
			    			sender.sendMessage(ChatColor.GREEN + "Enabled!");
			    			return true;
			    		}
					}
					if(args[0].equalsIgnoreCase("lamps")){
						if(lamps){
			    			this.getConfig().set("Settings.PlaceLamps", false);
			    			this.saveConfig();
			    			lamps = false;
			    			sender.sendMessage(ChatColor.RED + "Disabled!");
			    			return true;
			    		}
			    		else{
			    			this.getConfig().set("Settings.PlaceLamps", true);
			    			this.saveConfig();
			    			lamps = true;
			    			sender.sendMessage(ChatColor.GREEN + "Enabled!");
			    			return true;
			    		}
					}
					if(args[0].equalsIgnoreCase("eggs")){
						if(eggBreak){
			    			this.getConfig().set("Settings.DragonEggBlocking", false);
			    			this.saveConfig();
			    			eggBreak = false;
			    			sender.sendMessage(ChatColor.RED + "Disabled!");
			    			return true;
			    		}
			    		else{
			    			this.getConfig().set("Settings.DragonEggBlocking", true);
			    			this.saveConfig();
			    			eggBreak = true;
			    			sender.sendMessage(ChatColor.GREEN + "Enabled!");
			    			return true;
			    		}
					}
				}
				else{
					sender.sendMessage(ChatColor.GRAY + "Incorrect use! See /fix help");
	    			return true;
				}
			}
		}
		return false;
	}
}
