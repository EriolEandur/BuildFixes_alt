package me.dags.BuildFixes.WorldConfig;

import java.util.HashMap;
import java.util.HashSet;

import me.dags.BuildFixes.BuildFixes;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Worlds {

	public static HashMap<String, HashSet<String>> worlds = new HashMap<String, HashSet<String>>();
	public static JavaPlugin instance = (JavaPlugin) BuildFixes.inst();

	public static void multiWorld(World w) {
		HashSet<String> settings = new HashSet<String>();
		String world = w.getName();

		Config cfg = new Config(instance, w.getName());

		if (cfg.getWorldConfig().getBoolean("Modules.BuildFixes.Enable")) {
			if (cfg.getWorldConfig().getBoolean(
					"Modules.BuildFixes.DragonEggBlocking")) {
				settings.add("Eggs");
			}
			if (cfg.getWorldConfig().getBoolean("Modules.BuildFixes.HalfDoors")) {
				settings.add("Doors");
			}
			if (cfg.getWorldConfig().getBoolean("Modules.BuildFixes.NoPhysics")) {
				settings.add("NoPhys");
			}
			if (cfg.getWorldConfig().getBoolean(
					"Modules.BuildFixes.SpecialLogs")) {
				settings.add("Logs");
			}
		}
		if (cfg.getWorldConfig().getBoolean("Modules.Commands.Enable")) {
			if (cfg.getWorldConfig().getBoolean("Modules.Commands.GetItem")) {
				settings.add("GetCMD");
			}
			if (cfg.getWorldConfig().getBoolean("Modules.Commands.Fullbright")) {
				settings.add("FbCMD");
			}
		}
		if (cfg.getWorldConfig().getBoolean("Modules.Environment.Enable")) {
			if (cfg.getWorldConfig().getBoolean(
					"Modules.Environment.DecayBlocking")) {
				settings.add("Decay");
			}
			if (cfg.getWorldConfig().getBoolean(
					"Modules.Environment.FormBlocking")) {
				settings.add("Form");
			}
			if (cfg.getWorldConfig().getBoolean(
					"Modules.Environment.MobBlocking")) {
				settings.add("Mob");
			}
			if (cfg.getWorldConfig().getBoolean(
					"Modules.Environment.WeatherBlocking")) {
				settings.add("Weather");
			}
		}
		worlds.put(world, settings);
	}

	public static boolean isCancelled(String s, World w) {
		if (worlds.get(w.getName()).contains(s)) {
			return true;
		}
		return false;
	}
}
