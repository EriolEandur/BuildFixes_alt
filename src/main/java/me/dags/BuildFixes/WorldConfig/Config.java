package me.dags.BuildFixes.WorldConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.dags.BuildFixes.BuildFixes;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {

	private FileConfiguration worldContents = null;
	private File worldFile = null;
	private final String fileName;
	private final JavaPlugin plugin;

	public static Plugin instance = BuildFixes.inst();

	public Config(JavaPlugin plugin, String name) {
		if (plugin == null)
			throw new IllegalArgumentException("Plugin cannot be null!");
		if (!plugin.isInitialized())
			throw new IllegalArgumentException("Plugin must be initialized!");
		this.plugin = plugin;
		this.fileName = name;
		File dataFolder = plugin.getDataFolder();
		if (dataFolder == null)
			throw new IllegalStateException();
		this.worldFile = new File(plugin.getDataFolder(), "worlds"
				+ File.separator + name + ".yml");
	}

	public void reloadWorlds() {
		if (worldFile == null) {
			worldFile = new File(instance.getDataFolder(), "worlds"
					+ File.separator + fileName + ".yml");
			Bukkit.broadcastMessage("Generating new config file for "
					+ fileName);
		}
		worldContents = YamlConfiguration.loadConfiguration(worldFile);

		InputStream defConfigStream = plugin.getResource("defaults.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			worldContents.setDefaults(defConfig);
		}
	}

	public FileConfiguration getWorldConfig() {
		if (worldContents == null) {
			reloadWorlds();
		}
		return worldContents;
	}

	public void saveWorldConfig() {
		if (worldContents == null || worldFile == null) {
			return;
		}
		try {
			getWorldConfig().save(worldFile);
		} catch (IOException ex) {
			instance.getLogger().log(Level.SEVERE,
					"Could not save config to " + worldFile, ex);
		}
	}

}
