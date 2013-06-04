package com.dags.BuildFixes;

import static com.dags.BuildFixes.BuildFixes.getCMD;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		} else {
			Player player = (Player) sender;
			if (label.equalsIgnoreCase("get")
					&& sender.hasPermission("BuildFixes.get") && getCMD) {
				if (args.length == 0) {
					player.sendMessage(ChatColor.GRAY
							+ "/get [doors], [egg], [furnace], [logs], [lamps], [slabs #]");
					return true;
				}
				if (args[0].equalsIgnoreCase("egg")
						&& sender.hasPermission("BuildFixes.eggs")) {
					player.getInventory().addItem(
							new ItemStack(122, 1, (byte) 0));
					return true;
				}
				if ((args[0].equalsIgnoreCase("doors") || args[0]
						.equalsIgnoreCase("door"))
						&& sender.hasPermission("BuildFixes.doors")) {
					player.getInventory().addItem(
							new ItemStack(64, 64, (byte) 0));
					player.getInventory().addItem(
							new ItemStack(71, 64, (byte) 0));
					return true;
				}
				if (args[0].equalsIgnoreCase("furnace")
						&& sender.hasPermission("BuildFixes.furnace")) {
					player.getInventory().addItem(
							new ItemStack(62, 64, (byte) 0));
					return true;
				}
				if ((args[0].equalsIgnoreCase("logs") || args[0]
						.equalsIgnoreCase("log"))
						&& sender.hasPermission("BuildFixes.logs")) {

					player.getInventory().addItem(
							new ItemStack(17, 64, (byte) 12));
					player.getInventory().addItem(
							new ItemStack(17, 64, (byte) 13));
					player.getInventory().addItem(
							new ItemStack(17, 64, (byte) 14));
					player.getInventory().addItem(
							new ItemStack(17, 64, (byte) 15));
					return true;
				}
				if (args[0].equalsIgnoreCase("lamps")
						|| args[0].equalsIgnoreCase("lamp")
						&& sender.hasPermission("BuildFixes.lamps")) {
					player.getInventory().addItem(
							new ItemStack(124, 64, (byte) 0));
					return true;
				}
				if ((args[0].equalsIgnoreCase("slabs") || args[0]
						.equalsIgnoreCase("slab"))
						&& sender.hasPermission("BuildFixes.slabs")) {
					if (args.length > 1) {
						String str = args[1];
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
							char c = str.charAt(i);
							if (c <= '/' || c >= ':') {
								return false;
							}
						}
						int dmg = Integer.valueOf(args[1]);
						player.getInventory().addItem(
								new ItemStack(43, 64, (byte) dmg));
						return true;
					} else {
						player.sendMessage(ChatColor.GRAY + "/get slabs [0-16]");
						return true;
					}
				}
			}
		}
		return false;
	}

}
