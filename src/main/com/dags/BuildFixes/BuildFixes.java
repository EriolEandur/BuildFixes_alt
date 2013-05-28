package com.dags.BuildFixes;

import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildFixes extends JavaPlugin {
	
	//FIXES MODULE
	public static boolean fixesModule;
	@SuppressWarnings("rawtypes")
	public static HashSet noPhysList = new HashSet();
	public static boolean doors;
	public static boolean logs;
	public static boolean lamps;
	public static boolean noPhysics;
	public static boolean eggBreak;
	//COMMANDS MODULE
	public static boolean commandsModule;
	public static boolean getCMD;
	//ENVIRONEMT MODULE
	public static boolean environmentModule;
	public static boolean weatherBlock;
	public static boolean decayBlock;
	public static boolean formBlock;
	
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
    	//ENABLE/DISABLE MODULES
    	fixesModule = getConfig().getBoolean("Modules.BuildFixes.Enable");
    	commandsModule = getConfig().getBoolean("Modules.Commands.Enable");
    	environmentModule = getConfig().getBoolean("Modules.Environment.Enable");
    	
    	doors = getConfig().getBoolean("Modules.BuildFixes.HalfDoors");
    	logs = getConfig().getBoolean("Modules.BuildFixes.SpecialLogs");
    	lamps = getConfig().getBoolean("Modules.BuildFixes.PlaceLamps");
    	noPhysics = getConfig().getBoolean("Modules.BuildFixes.NoPhysics");
    	eggBreak = getConfig().getBoolean("Modules.BuildFixes.DragonEggBlocking");
    	
    	getCMD = getConfig().getBoolean("Modules.Commands.GetItem");
    	
    	weatherBlock = getConfig().getBoolean("Modules.Environment.WeatherBlocking");
		decayBlock = getConfig().getBoolean("Modules.Environment.DecayBlocking");
		formBlock = getConfig().getBoolean("Modules.Environment.FormBlocking");
    	setupModules();
    }
    
    public void setupModules() {
    	if(fixesModule){
    		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        	setupNoPhysList();
    	}
    	if(commandsModule){
    		getCommand("get").setExecutor(new Commands());
    	}
    	if(environmentModule){
    		this.getServer().getPluginManager().registerEvents(new EnvironmentListener(), this);
    	}
    }
    
    public static void unregisterAll(Listener listener){
    	
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setupNoPhysList() {
        List NPlist = getConfig().getList("NoPhysicsList");
        for (Object o : NPlist) {
        	noPhysList.add(o);
        }
    }
    
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("BuildFixes.admin") 
				&& label.equalsIgnoreCase("bfmodules")){
			if(args.length == 0){
				sender.sendMessage(ChatColor.GREEN + "Enabled Modules:");
				if(fixesModule){
					sender.sendMessage(ChatColor.GRAY + "BuildFixes");
				}
				if(commandsModule){
					sender.sendMessage(ChatColor.GRAY + "Commands");
				}
				if(environmentModule){
					sender.sendMessage(ChatColor.GRAY + "Environment");
				}
				return true;
			}
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("help")){
					sender.sendMessage(ChatColor.GREEN + "/bfmodules [buildfixes], [commands], [environment]");
					return true;
				}
				if(args[0].equalsIgnoreCase("buildfixes")){
					if(fixesModule){
		    			this.getConfig().set("Modules.BuildFixes.Enable", false);
		    			this.saveConfig();
		    			fixesModule = false;
		    			sender.sendMessage(ChatColor.RED + "Disabled!");
		    			return true;
		    		}
		    		else{
		    			this.getConfig().set("Modules.BuildFixes.Enable", true);
		    			this.saveConfig();
		    			fixesModule = true;
		    			sender.sendMessage(ChatColor.GREEN + "Enabled!");
		    			return true;
		    		}
				}
				if(args[0].equalsIgnoreCase("commands")){
					if(commandsModule){
		    			this.getConfig().set("Modules.Commands.Enable", false);
		    			this.saveConfig();
		    			commandsModule = false;
		    			sender.sendMessage(ChatColor.RED + "Disabled!");
		    			return true;
		    		}
		    		else{
		    			this.getConfig().set("Modules.Commands.Enable", true);
		    			this.saveConfig();
		    			commandsModule = true;
		    			sender.sendMessage(ChatColor.GREEN + "Enabled!");
		    			return true;
		    		}
				}
				if(args[0].equalsIgnoreCase("environment")){
					if(environmentModule){
		    			this.getConfig().set("Modules.Environment.Enable", false);
		    			this.saveConfig();
		    			environmentModule = false;
		    			sender.sendMessage(ChatColor.RED + "Disabled!");
		    			return true;
		    		}
		    		else{
		    			this.getConfig().set("Modules.Environment.Enable", true);
		    			this.saveConfig();
		    			environmentModule = true;
		    			sender.sendMessage(ChatColor.GREEN + "Enabled!");
		    			return true;
		    		}
				}
			}
		}
	return false;
	}
}
