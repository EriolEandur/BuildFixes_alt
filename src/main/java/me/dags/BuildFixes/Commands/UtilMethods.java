package me.dags.BuildFixes.Commands;

import static me.dags.BuildFixes.BuildFixes.scd;
import static me.dags.BuildFixes.BuildFixes.ter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.dags.BuildFixes.BuildFixes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class UtilMethods {
	
	public static JavaPlugin instance = (JavaPlugin) BuildFixes.inst();

	public static void getStencils(Player p) throws IOException {
		
		String plugins = instance.getDataFolder().getParent();
		File folder = new File(plugins + "/VoxelSniper/stencillists");

		if (!folder.exists()) {
			p.sendMessage(ter + "VoxelSniper's stencilList folder cannot be found!");
			return;
		} else {
			String[] filenames = folder.list();
			List<String> names = new ArrayList<String>();
			for(String s : filenames) {
				String shorter = s.replace(".txt", "");
				names.add(shorter);
			}
			
			p.sendMessage(scd + "Available Stencil Lists:");
			p.sendMessage(ter + names.toString());
		}
		
	}

}
