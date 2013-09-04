package me.dags.BuildFixes.Commands;

import static me.dags.BuildFixes.BuildFixes.prim;
import static me.dags.BuildFixes.BuildFixes.scd;
import static me.dags.BuildFixes.BuildFixes.ter;

import java.io.File;
import java.io.IOException;

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

		File folder = new File(getPluginsDir() + "/VoxelSniper/stencillists");

		if (!folder.exists()) {
			p.sendMessage(ter
					+ "VoxelSniper's stencilList folder cannot be found!");
			return;
		} else {
			String[] contents = folder.list();
			if (contents.length != 0) {
				boolean foundFiles = false;
				p.sendMessage(prim + "|--------[StencilList Search]--------|");
				p.sendMessage(scd + "Stencil Lists:");
				for (String s : contents) {
					if (s.endsWith(".txt")) {
						String shorter = s.replace(".txt", "");
						p.sendMessage(ter + shorter);
						foundFiles = true;
					}
				}
				if (!foundFiles) {
					p.sendMessage(ter + "-");
				}
				return;
			} else {
				p.sendMessage(ter + "Directory is empty!");
				return;
			}
		}

	}

	public static void getSchematics(Player p, String str) throws IOException {
		String ext = "";
		if (!str.equals("null")) {
			ext = "/" + str;
		}
		File folder = new File(getPluginsDir() + "/WorldEdit/schematics" + ext);
		if (!folder.exists()) {
			p.sendMessage(ter + "Folder cannot be found!");
			return;
		} else {
			String[] contents = folder.list();

			if (contents.length != 0) {
				boolean foundDir = false;
				boolean foundFiles = false;
				p.sendMessage(prim + "|--------[Schematics Search]--------|");
				p.sendMessage(scd + "SubDirectories:");
				for (String s : contents) {
					if (!s.contains(".")) {
						p.sendMessage(ter + s + "/");
						foundDir = true;
					}
				}
				if (!foundDir) {
					p.sendMessage(ter + "-");
				}

				p.sendMessage(scd + "Files:");
				for (String s : contents) {
					if (s.contains(".")) {
						p.sendMessage(ter + s);
						foundFiles = true;
					}
				}
				if (!foundFiles) {
					p.sendMessage(ter + "-");
				}

				return;
			} else {
				p.sendMessage(ter + "Directory is empty!");
				return;
			}
		}
	}

	private static String getPluginsDir() {
		return instance.getDataFolder().getParent();
	}

}
