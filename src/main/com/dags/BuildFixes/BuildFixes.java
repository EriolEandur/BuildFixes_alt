package com.dags.BuildFixes;

import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildFixes extends JavaPlugin {

	@SuppressWarnings("rawtypes")
	public static HashSet noPhysList = new HashSet();
	public static boolean doors;
	public static boolean logs;
	public static boolean lamps;
	public static boolean noPhysics;
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
        setupNoPhysList();
        saveConfig();
    }
    
    public void registerEvents() {
    	doors = getConfig().getBoolean("Settings.HalfDoors");
    	logs = getConfig().getBoolean("Settings.SpecialLogs");
    	lamps = getConfig().getBoolean("Settings.PlaceLamps");
    	noPhysics = getConfig().getBoolean("Settings.NoPhysics");
    	eggBreak = getConfig().getBoolean("Settings.DragonEggBlocking");
    	this.getServer().getPluginManager().registerEvents(new EventListener(), this);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setupNoPhysList() {
        List NPlist = getConfig().getList("NoPhysicsList");
        for (Object o : NPlist) {
        	noPhysList.add(o);
        }
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
			if(label.equalsIgnoreCase("get")
					&& sender.hasPermission("BuildFixes.get")
					){
				if(args.length == 0){
					player.sendMessage(ChatColor.GRAY + "/get [doors], [egg], [furnace], [logs], [slabs #]");
					return true;
				}
				if(args[0].equalsIgnoreCase("egg")&& sender.hasPermission("BuildFixes.eggs")){
					player.getInventory().addItem(new ItemStack(122, 1, (byte) 0));
					return true;
				}
				if((args[0].equalsIgnoreCase("doors")
						|| args[0].equalsIgnoreCase("door"))
						&& sender.hasPermission("BuildFixes.doors")){
					player.getInventory().addItem(new ItemStack(64, 64, (byte) 0));
					player.getInventory().addItem(new ItemStack(71, 64, (byte) 0));
					return true;
				}
				if(args[0].equalsIgnoreCase("furnace")
						&& sender.hasPermission("BuildFixes.furnace")){
					player.getInventory().addItem(new ItemStack(62, 64, (byte) 0));
					return true;
				}
				if((args[0].equalsIgnoreCase("logs")
						|| args[0].equalsIgnoreCase("log"))
						&& sender.hasPermission("BuildFixes.logs")){
					if(logs){
						player.getInventory().addItem(new ItemStack(17, 64, (byte) 12 ));
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
				if((args[0].equalsIgnoreCase("slabs")
						|| args[0].equalsIgnoreCase("slab"))
						&& sender.hasPermission("BuildFixes.slabs")){
					if(args.length > 1){
		    			String str = args[1];
		    			if (str == null) {
		    				return false;
		    			}
		    			int length = str.length();
		    			if (length == 0) {
		    				return false;
		    			}
		    			int i = 0;
		    			if (str.charAt(0) == '-') {
		    				if (length == 1) {
		    					return false;
		    				}
		    				i = 1;
		    			}
		    			for (; i < length; i++) {
		    				char c = str.charAt(i);
		    				if (c <= '/' || c >= ':') {
		    					return false;
		    				}
		    			}
						int dmg = Integer.valueOf(args[1]);
						player.getInventory().addItem(new ItemStack(43, 64, (byte) dmg));
						return true;
					}
					else{
						player.sendMessage(ChatColor.GRAY + "/get slabs [0-16]");
						return true;
					}
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
					if(args[0].equalsIgnoreCase("doors")){
						if(doors){
			    			this.getConfig().set("Settings.HalfDoors", false);
			    			this.saveConfig();
			    			doors = false;
			    			sender.sendMessage(ChatColor.RED + "Disabled!");
			    			return true;
			    		}
			    		else{
			    			this.getConfig().set("Settings.HalfDoors", true);
			    			this.saveConfig();
			    			doors = true;
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
					if(args[0].equalsIgnoreCase("nophysics")){
						if(noPhysics){
			    			this.getConfig().set("Settings.NoPhysics", false);
			    			this.saveConfig();
			    			noPhysics = false;
			    			sender.sendMessage(ChatColor.RED + "Disabled!");
			    			return true;
			    		}
			    		else{
			    			this.getConfig().set("Settings.NoPhysics", true);
			    			this.saveConfig();
			    			noPhysics = true;
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
