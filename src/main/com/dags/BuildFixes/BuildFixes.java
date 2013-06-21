package com.dags.BuildFixes;

import java.util.HashSet;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildFixes extends JavaPlugin {

	// FIXES MODULE
	public static boolean fixesModule;
	@SuppressWarnings("rawtypes")
	public static HashSet noPhysList = new HashSet();
	public static boolean doors;
	public static boolean logs;
	public static boolean noPhysics;
	public static boolean eggBreak;
	// COMMANDS MODULE
	public static boolean commandsModule;
	public static boolean getCMD;
	// ENVIRONEMT MODULE
	public static boolean environmentModule;
	public static boolean weatherBlock;
	public static boolean decayBlock;
	public static boolean formBlock;

	@Override
	public void onEnable() {
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
		// ENABLE/DISABLE MODULES
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
		}
		if (environmentModule) {
			this.getServer().getPluginManager()
					.registerEvents(new EnvironmentListener(), this);
		}
	}

	public static void unregisterAll(Listener listener) {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setupNoPhysList() {
		List NPlist = getConfig().getList("NoPhysicsList");
		for (Object o : NPlist) {
			noPhysList.add(o);
		}
	}
}
