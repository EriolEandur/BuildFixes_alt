package me.dags.BuildFixes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import me.dags.BuildFixes.Commands.Commands;
import me.dags.BuildFixes.Configuration.ConfigUtil;
import me.dags.BuildFixes.Configuration.Global;
import me.dags.BuildFixes.Configuration.MultiWorlds;
import me.dags.BuildFixes.Listeners.BlockListener;
import me.dags.BuildFixes.Listeners.EnvironmentListener;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class BuildFixes extends JavaPlugin {

	private static Plugin plugin;

	public static boolean multiWorlds;
	public static boolean fixesModule;
	public static HashSet<Integer> noPhysList = new HashSet<Integer>();
	public static boolean commandsModule;
	public static boolean environmentModule;

	public static ChatColor prim = ChatColor.DARK_AQUA;
	public static ChatColor scd = ChatColor.DARK_PURPLE;
	public static ChatColor ter = ChatColor.GRAY;
	
	public static HashMap<String, List<Boolean>> worldsCFG = new HashMap<String, List<Boolean>>();

	public BuildFixes() {
		super();
		plugin = this;
	}

	public static Plugin inst() {
		return plugin;
	}

	@Override
	public void onEnable() {
		setupConfig();
		enableModules();
		setupModules();
		loadWorldSettings();
	}

	@Override
	public void onDisable() {
		noPhysList.clear();
		worldsCFG.clear();
	}

	public void setupConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void enableModules() {
		multiWorlds = getConfig().getBoolean("MultiWorldSupport.Enable");
		fixesModule = getConfig().getBoolean("Modules.BuildFixes.Enable");
		commandsModule = getConfig().getBoolean("Modules.Commands.Enable");
		environmentModule = getConfig().getBoolean("Modules.Environment.Enable");
	}

	public void setupModules() {
		if (fixesModule) {
			this.getServer().getPluginManager()
					.registerEvents(new BlockListener(), this);
			setupNoPhysList();
		}
		if (commandsModule) {
			getCommand("get").setExecutor(new Commands());
			getCommand("fbt").setExecutor(new Commands());
			getCommand("stencillist").setExecutor(new Commands());
			getCommand("schlist").setExecutor(new Commands());
			getCommand("bfworlds").setExecutor(new Commands());
			getCommand("bfversion").setExecutor(new Commands());
		}
		if (environmentModule) {
			this.getServer().getPluginManager()
					.registerEvents(new EnvironmentListener(), this);
		}
	}

	private void loadWorldSettings() {
		getServer().getScheduler().runTask(this, new Runnable() {
        	public void run() {
        		for (World w : getServer().getWorlds()) {
        			worldDefaults(w);
        		}
        	}
		});
	}

	private void worldDefaults(World w) {
		if(multiWorlds){
			ConfigUtil cfg = new ConfigUtil(this, w.getName());

			cfg.getWorldConfig().options().copyDefaults(true);
			cfg.saveWorldConfig();

			MultiWorlds.multiWorld(w);
			System.out.print("[BuildFixes] is using MultiWorld settings for world: " + w.getName());
		} else {
			Global.config(w);
			System.out.print("[BuildFixes] is using Global settings for world: " + w.getName());
		}
	}

	private void setupNoPhysList() {
		List<Integer> NPlist = getConfig().getIntegerList("NoPhysicsList");
		for (Integer i : NPlist) {
			noPhysList.add(i);
		}
	}
}
