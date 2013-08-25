package me.dags.BuildFixes;

import java.util.HashSet;
import java.util.List;

import me.dags.BuildFixes.Commands.Commands;
import me.dags.BuildFixes.Listeners.BlockListener;
import me.dags.BuildFixes.Listeners.EnvironmentListener;
import me.dags.BuildFixes.WorldConfig.Config;
import me.dags.BuildFixes.WorldConfig.Worlds;

import org.bukkit.Bukkit;
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
	public static boolean doors;
	public static boolean logs;
	public static boolean noPhysics;
	public static boolean eggBreak;
	public static boolean commandsModule;
	public static boolean getCMD;
	public static boolean fbCMD;
	public static boolean environmentModule;
	public static boolean weatherBlock;
	public static boolean decayBlock;
	public static boolean formBlock;
	public static boolean mobBlock;

	public static ChatColor prim = ChatColor.DARK_AQUA;
	public static ChatColor scd = ChatColor.DARK_PURPLE;
	public static ChatColor ter = ChatColor.GRAY;

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
		configValues();
		setupModules();
	}

	@Override
	public void onDisable() {
		noPhysList.clear();
		Worlds.worlds.clear();
	}

	public void setupConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void configValues() {
		multiWorlds = getConfig().getBoolean("MultiWorldSupport.Enable");
		fixesModule = getConfig().getBoolean("Modules.BuildFixes.Enable");
		commandsModule = getConfig().getBoolean("Modules.Commands.Enable");
		environmentModule = getConfig()
				.getBoolean("Modules.Environment.Enable");
		doors = getConfig().getBoolean("Modules.BuildFixes.HalfDoors");
		logs = getConfig().getBoolean("Modules.BuildFixes.SpecialLogs");
		noPhysics = getConfig().getBoolean("Modules.BuildFixes.NoPhysics");
		eggBreak = getConfig().getBoolean(
				"Modules.BuildFixes.DragonEggBlocking");
		getCMD = getConfig().getBoolean("Modules.Commands.GetItem");
		fbCMD = getConfig().getBoolean("Modules.Commands.Fullbright");
		decayBlock = getConfig()
				.getBoolean("Modules.Environment.DecayBlocking");
		formBlock = getConfig().getBoolean("Modules.Environment.FormBlocking");
		mobBlock = getConfig().getBoolean("Modules.Environment.MobBlocking");
		weatherBlock = getConfig().getBoolean(
				"Modules.Environment.WeatherBlocking");
	}

	public void setupModules() {
		if (multiWorlds) {
			loadWorlds();
		}
		if (fixesModule) {
			this.getServer().getPluginManager()
					.registerEvents(new BlockListener(), this);
			setupNoPhysList();
		}
		if (commandsModule) {
			getCommand("get").setExecutor(new Commands());
			getCommand("fbt").setExecutor(new Commands());
			getCommand("bftool").setExecutor(new Commands());
		}
		if (environmentModule) {
			this.getServer().getPluginManager()
					.registerEvents(new EnvironmentListener(), this);
		}
	}

	private void loadWorlds() {
		for (World w : Bukkit.getServer().getWorlds()) {
			worldDefaults(w);
		}
		System.out.print("BuildFixes is running in MultiWorld mode!");
	}

	private void worldDefaults(World w) {
		Config cfg = new Config(this, w.getName());
		
		cfg.getWorldConfig().options().copyDefaults(true);
		cfg.saveWorldConfig();

		Worlds.multiWorld(w);
		System.out.print("BuildFixes found world: " + w.getName());
	}

	private void setupNoPhysList() {
		List<Integer> NPlist = getConfig().getIntegerList("NoPhysicsList");
		for (Integer i : NPlist) {
			noPhysList.add(i);
		}
	}
}
