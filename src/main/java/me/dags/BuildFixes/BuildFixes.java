package me.dags.BuildFixes;

import java.util.HashSet;
import java.util.List;

import me.dags.BuildFixes.Commands.Commands;
import me.dags.BuildFixes.Listeners.BlockListener;
import me.dags.BuildFixes.Listeners.EnvironmentListener;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class BuildFixes extends JavaPlugin {

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

	public static ChatColor prim = ChatColor.DARK_AQUA;
	public static ChatColor scd = ChatColor.DARK_PURPLE;
	public static ChatColor ter = ChatColor.GRAY;

	@Override
	public void onEnable() {
		setupConfig();
		configValues();
	}

	@Override
	public void onDisable() {
		noPhysList.clear();
	}

	public void setupConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void configValues() {
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
		weatherBlock = getConfig().getBoolean(
				"Modules.Environment.WeatherBlocking");
		decayBlock = getConfig()
				.getBoolean("Modules.Environment.DecayBlocking");
		formBlock = getConfig().getBoolean("Modules.Environment.FormBlocking");
		setupModules();
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
		}
		if (environmentModule) {
			this.getServer().getPluginManager()
					.registerEvents(new EnvironmentListener(), this);
		}
	}

	private void setupNoPhysList() {
		List<Integer> NPlist = getConfig().getIntegerList("NoPhysicsList");
		for (Integer i : NPlist) {
			noPhysList.add(i);
		}
	}
}
