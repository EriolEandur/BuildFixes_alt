package me.dags.BuildFixes.MultiWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.dags.BuildFixes.BuildFixes;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Worlds {

	public static HashMap<String, List<Boolean>> worldsCFG = new HashMap<String, List<Boolean>>();
	public static JavaPlugin instance = (JavaPlugin) BuildFixes.inst();

	public static void multiWorld(World w) {
		List<Boolean> settings = new ArrayList<Boolean>();
		String world = w.getName();

		Config cfg = new Config(instance, w.getName());
		
		boolean doors = false;
		boolean eggs = false;
		boolean nophys = false;
		boolean logs = false;
		boolean getCMD = false;
		boolean fbCMD = false;
		boolean decay = false;
		boolean form = false;
		boolean mob = false;
		boolean weather = false;
		
		if (cfg.getWorldConfig().getBoolean("Modules.BuildFixes.Enable") ) {
			eggs = cfg.getWorldConfig().getBoolean(
					"Modules.BuildFixes.DragonEggBlocking");
			doors = cfg.getWorldConfig().getBoolean("Modules.BuildFixes.HalfDoors");
			logs = cfg.getWorldConfig().getBoolean(
					"Modules.BuildFixes.SpecialLogs");
			nophys = cfg.getWorldConfig().getBoolean("Modules.BuildFixes.NoPhysics");
		}
		if (cfg.getWorldConfig().getBoolean("Modules.Commands.Enable")) {
			getCMD = cfg.getWorldConfig().getBoolean("Modules.Commands.GetItem");
			fbCMD = cfg.getWorldConfig().getBoolean("Modules.Commands.Fullbright");
		}
		if (cfg.getWorldConfig().getBoolean("Modules.Environment.Enable")) {
			decay = cfg.getWorldConfig().getBoolean(
					"Modules.Environment.DecayBlocking");
			form = cfg.getWorldConfig().getBoolean(
					"Modules.Environment.FormBlocking");
			mob = cfg.getWorldConfig().getBoolean(
					"Modules.Environment.MobBlocking");
			weather = cfg.getWorldConfig().getBoolean(
					"Modules.Environment.WeatherBlocking");
		}
		
		settings.add(0, doors);
		settings.add(1, eggs);
		settings.add(2, logs);
		settings.add(3, nophys);
		
		settings.add(4, getCMD);
		settings.add(5, fbCMD);
		
		settings.add(6, decay);
		settings.add(7, form);
		settings.add(8, mob);
		settings.add(9, weather);
		worldsCFG.put(world, settings);
	}
}
