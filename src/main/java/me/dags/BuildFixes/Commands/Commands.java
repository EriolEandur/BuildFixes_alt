package me.dags.BuildFixes.Commands;

import static me.dags.BuildFixes.BuildFixes.fbCMD;
import static me.dags.BuildFixes.BuildFixes.getCMD;
import static me.dags.BuildFixes.BuildFixes.prim;
import static me.dags.BuildFixes.BuildFixes.scd;
import static me.dags.BuildFixes.BuildFixes.ter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class Commands implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmd, String c, String[] a) {
		if (!(cs instanceof Player)) {
			cs.sendMessage("This command can only be run by a player");
			return true;
		} else {
			Player p = (Player) cs;
			if (c.equalsIgnoreCase("get")) {
				if(cs.hasPermission("BuildFixes.get") && getCMD) {
					if (a.length == 0) {
						p.sendMessage(ChatColor.GRAY
								+ "/get [doors], [egg], [furnaces], [grass], [logs], [mushrooms], [slabs #]");
						return true;
					}
					if ((a[0].equalsIgnoreCase("doors") || a[0]
							.equalsIgnoreCase("door"))) {
						getMethods.getDoors(p);
						return true;
					}
					if (a[0].equalsIgnoreCase("eggs")
							|| a[0].equalsIgnoreCase("egg")) {
						getMethods.getEggs(p);
						return true;
					}
					if (a[0].equalsIgnoreCase("furnaces")
							|| a[0].equalsIgnoreCase("furnace")) {
						getMethods.getFurnaces(p);
						return true;
					}
					if (a[0].equalsIgnoreCase("grass")) {
						getMethods.getGrass(p);
						return true;
					}
					if ((a[0].equalsIgnoreCase("logs") || a[0]
							.equalsIgnoreCase("log"))) {
						getMethods.getLogs(p);
						return true;
					}
					if ((a[0].equalsIgnoreCase("mushrooms") || a[0]
							.equalsIgnoreCase("mushroom"))) {
						getMethods.getMushrooms(p);
						return true;
					}
					if ((a[0].equalsIgnoreCase("slabs") || a[0]
							.equalsIgnoreCase("slab"))) {
						getMethods.getSlabs(p, a[1]);
						return true;
					}
				} else {
					p.sendMessage(scd
							+ "Sorry, that feature is disabled, or you don't have permission to use it!");
					return true;
				}
			}
			if (c.equalsIgnoreCase("fbt")){
				if(cs.hasPermission("BuildFixes.fullbright") && fbCMD) {
					if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
						p.removePotionEffect(PotionEffectType.NIGHT_VISION);
						p.sendMessage(ter + "Fullbright off!");
						return true;
					} else {
						p.addPotionEffect(new PotionEffect(
								PotionEffectType.NIGHT_VISION, 72000, 1));
						p.sendMessage(prim + "Fullbright on!");
						return true;
					}
				} else{
					p.sendMessage(scd
							+ "Sorry, that feature is disabled, or you don't have permission to use it!");
					return true;
				}
			}
		}
		return false;
	}

}
