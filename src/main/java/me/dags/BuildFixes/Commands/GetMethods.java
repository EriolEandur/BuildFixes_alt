package me.dags.BuildFixes.Commands;

import static me.dags.BuildFixes.BuildFixes.prim;
import static me.dags.BuildFixes.BuildFixes.scd;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class GetMethods {

	public static void getDoors(Player p) {
		if (p.hasPermission("BuildFixes.doors")) {
			p.getInventory().addItem(new ItemStack(64, 64, (byte) 0));
			p.getInventory().addItem(new ItemStack(71, 64, (byte) 0));
			p.sendMessage(prim + "Given doors!");
			return;
		} else {
			p.sendMessage(scd
					+ "Sorry, you do not have permission to use that Command!");
			return;
		}
	}

	public static void getEggs(Player p) {
		if (p.hasPermission("BuildFixes.eggs")) {
			p.getInventory().addItem(new ItemStack(122, 1, (byte) 0));
			p.sendMessage(prim + "Given egg!");
			return;
		} else {
			p.sendMessage(scd
					+ "Sorry, you do not have permission to use that Command!");
			return;
		}
	}

	public static void getFurnaces(Player p) {
		if (p.hasPermission("BuildFixes.furnace")) {
			p.getInventory().addItem(new ItemStack(62, 64, (byte) 0));
			p.sendMessage(prim + "Given furnaces!");
			return;
		} else {
			p.sendMessage(scd
					+ "Sorry, you do not have permission to use that Command!");
			return;
		}

	}

	public static void getGrass(Player p) {
		if (p.hasPermission("BuildFixes.grass")) {
			p.getInventory().addItem(new ItemStack(31, 64, (byte) 0));
			p.sendMessage(prim + "Given grass!");
			return;
		} else {
			p.sendMessage(scd
					+ "Sorry, you do not have permission to use that Command!");
			return;
		}
	}

	public static void getLogs(Player p) {
		if (p.hasPermission("BuildFixes.logs")) {
			p.getInventory().addItem(new ItemStack(17, 64, (byte) 12));
			p.getInventory().addItem(new ItemStack(17, 64, (byte) 13));
			p.getInventory().addItem(new ItemStack(17, 64, (byte) 14));
			p.getInventory().addItem(new ItemStack(17, 64, (byte) 15));
			p.sendMessage(prim + "Given logs!");
			return;
		} else {
			p.sendMessage(scd
					+ "Sorry, you do not have permission to use that Command!");
			return;
		}
	}

	public static void getMushrooms(Player p) {
		if (p.hasPermission("BuildFixes.mushrooms")) {
			p.getInventory().addItem(new ItemStack(99, 64, (byte) 0));
			p.sendMessage(prim + "Given mushrooms!");
			return;
		} else {
			p.sendMessage(scd
					+ "Sorry, you do not have permission to use that Command!");
			return;
		}
	}

	public static void getSlabs(Player p, String a) {
		if (p.hasPermission("BuildFixes.slabs")) {
			if (isInt(a)) {
				int dmg = Integer.valueOf(a);
				p.getInventory().addItem(new ItemStack(43, 64, (byte) dmg));
				p.sendMessage(prim + "Given slabs!");
				return;
			} else {
				p.sendMessage(scd + "/get slabs [0-16]");
				return;
			}
		} else {
			p.sendMessage(scd
					+ "Sorry, you do not have permission to use that Command!");
			return;
		}
	}

	public static boolean isInt(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char ch = str.charAt(i);
			if (ch <= '/' || ch >= ':') {
				return false;
			}
		}
		return true;
	}

}
