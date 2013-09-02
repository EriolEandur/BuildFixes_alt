package me.dags.BuildFixes.Configuration;

import static me.dags.BuildFixes.BuildFixes.worldsCFG;

import java.util.ArrayList;
import java.util.List;

import me.dags.BuildFixes.BuildFixes;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class MultiWorlds {

	public static JavaPlugin instance = (JavaPlugin) BuildFixes.inst();

	public static void multiWorld(World w) {
		List<Boolean> settings = new ArrayList<Boolean>();
		String world = w.getName();

		ConfigUtil cfg = new ConfigUtil(instance, w.getName());

		boolean doors = false;
		boolean eggs = false;
		boolean nophys = false;
		boolean logs = false;
		boolean getCMD = false;
		boolean fbCMD = false;
		boolean decay = false;
		boolean form = false;
		boolean weather = false;
		boolean animal = false;
		boolean monster = false;

		if (cfg.getWorldConfig().getBoolean("Modules.BuildFixes.Enable")) {
			eggs = cfg.getWorldConfig().getBoolean(
					"Modules.BuildFixes.DragonEggBlocking");
			doors = cfg.getWorldConfig().getBoolean(
					"Modules.BuildFixes.HalfDoors");
			logs = cfg.getWorldConfig().getBoolean(
					"Modules.BuildFixes.SpecialLogs");
			nophys = cfg.getWorldConfig().getBoolean(
					"Modules.BuildFixes.NoPhysics");
		}
		if (cfg.getWorldConfig().getBoolean("Modules.Commands.Enable")) {
			getCMD = cfg.getWorldConfig()
					.getBoolean("Modules.Commands.GetItem");
			fbCMD = cfg.getWorldConfig().getBoolean(
					"Modules.Commands.Fullbright");
		}
		if (cfg.getWorldConfig().getBoolean("Modules.Environment.Enable")) {
			decay = cfg.getWorldConfig().getBoolean(
					"Modules.Environment.DecayBlocking");
			form = cfg.getWorldConfig().getBoolean(
					"Modules.Environment.FormBlocking");
			weather = cfg.getWorldConfig().getBoolean(
					"Modules.Environment.WeatherBlocking");
			animal = cfg.getWorldConfig().getBoolean(
					"Modules.Environment.AnimalBlocking");
			monster = cfg.getWorldConfig().getBoolean(
					"Modules.Environment.MonsterBlocking");
		}

		settings.add(0, doors);
		settings.add(1, eggs);
		settings.add(2, logs);
		settings.add(3, nophys);

		settings.add(4, getCMD);
		settings.add(5, fbCMD);

		settings.add(6, decay);
		settings.add(7, form);
		settings.add(8, weather);

		worldsCFG.put(world, settings);
		w.setSpawnFlags(!monster, !animal);
	}
}
