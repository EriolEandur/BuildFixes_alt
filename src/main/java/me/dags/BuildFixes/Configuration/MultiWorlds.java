package me.dags.BuildFixes.Configuration;

import static me.dags.BuildFixes.BuildFixes.worldsCFG;

import java.util.ArrayList;
import java.util.List;

import me.dags.BuildFixes.BuildFixes;

import org.bukkit.World;
import org.bukkit.configuration.Configuration;
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

		ConfigUtil cu = new ConfigUtil(instance, w.getName());
		Configuration cfg = cu.getWorldConfig();

		boolean doors = false;
		boolean eggs = false;
		boolean nophys = false;
		boolean logs = false;
		boolean getCMD = false;
		boolean fbCMD = false;
		boolean decay = false;
		boolean form = false;
		boolean weather = false;
		boolean fire = false;
		boolean animal = false;
		boolean monster = false;

		if (cfg.getBoolean("Modules.BuildFixes.Enable")) {
			eggs = cfg.getBoolean("Modules.BuildFixes.DragonEggBlocking");
			doors = cfg.getBoolean("Modules.BuildFixes.HalfDoors");
			logs = cfg.getBoolean("Modules.BuildFixes.SpecialLogs");
			nophys = cfg.getBoolean("Modules.BuildFixes.NoPhysics");
		}
		if (cfg.getBoolean("Modules.Commands.Enable")) {
			getCMD = cfg.getBoolean("Modules.Commands.GetItem");
			fbCMD = cfg.getBoolean("Modules.Commands.Fullbright");
		}
		if (cfg.getBoolean("Modules.Environment.Enable")) {
			decay = cfg.getBoolean("Modules.Environment.DecayBlocking");
			form = cfg.getBoolean("Modules.Environment.FormBlocking");
			weather = cfg.getBoolean("Modules.Environment.WeatherBlocking");
			fire = cfg.getBoolean("Modules.Environment.FireSpreadBlocking");
			animal = cfg.getBoolean("Modules.Environment.AnimalBlocking");
			monster = cfg.getBoolean("Modules.Environment.MonsterBlocking");
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
		
		String value = String.valueOf(!fire);
		w.setGameRuleValue("doFireTick", value);
		
		System.out.print("[BuildFixes] is using MultiWorld settings for world: " + w.getName());
	}
}
