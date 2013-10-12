package me.dags.BuildFixes.Commands;

import static me.dags.BuildFixes.BuildFixes.prim;
import static me.dags.BuildFixes.BuildFixes.scd;
import static me.dags.BuildFixes.BuildFixes.ter;
import static me.dags.BuildFixes.Configuration.WorldConfig.getWorldConf;

import java.io.IOException;

import me.dags.BuildFixes.BuildFixes;
import me.dags.BuildFixes.Configuration.WorldConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author dags_ <dags@dags.me>
 */

public class Commands implements CommandExecutor {

    private WorldConfig getConf(Player p) {
        return getWorldConf(p.getWorld());
    }

    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] a) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("This command can only be run by a player");
            return true;
        } else {
            Player p = (Player) cs;

            if (c.equalsIgnoreCase("get")) {
                if (cs.hasPermission("BuildFixes.get")
                        && getConf(p).getCmd()) {
                    if (a.length == 0) {
                        p.sendMessage(ter
                                + "/get [doors], [egg], [furnaces], [grass], [head], [logs], [mushrooms], [slabs #]");
                        return true;
                    }
                    if ((a[0].equalsIgnoreCase("doors") || a[0]
                            .equalsIgnoreCase("door"))) {
                        GetMethods.getDoors(p);
                        return true;
                    }
                    if (a[0].equalsIgnoreCase("eggs")
                            || a[0].equalsIgnoreCase("egg")) {
                        GetMethods.getEggs(p);
                        return true;
                    }
                    if (a[0].equalsIgnoreCase("furnaces")
                            || a[0].equalsIgnoreCase("furnace")) {
                        GetMethods.getFurnaces(p);
                        return true;
                    }
                    if (a[0].equalsIgnoreCase("grass")) {
                        GetMethods.getGrass(p);
                        return true;
                    }
                    if (a[0].equalsIgnoreCase("head")) {
                        if (a.length == 2) {
                            GetMethods.getHead(p, a[1]);
                            return true;
                        } else {
                            p.sendMessage(scd + "/get head <PlayerName>");
                            return true;
                        }
                    }
                    if ((a[0].equalsIgnoreCase("logs") || a[0]
                            .equalsIgnoreCase("log"))) {
                        GetMethods.getLogs(p);
                        return true;
                    }
                    if ((a[0].equalsIgnoreCase("mushrooms") || a[0]
                            .equalsIgnoreCase("mushroom"))) {
                        GetMethods.getMushrooms(p);
                        return true;
                    }
                    if ((a[0].equalsIgnoreCase("slabs") || a[0]
                            .equalsIgnoreCase("slab"))) {
                        if (a.length == 2) {
                            GetMethods.getSlabs(p, a[1]);
                            return true;
                        } else {
                            p.sendMessage(scd + "/get slabs <#number>");
                            return true;
                        }
                    }
                    if ((a[0].equalsIgnoreCase("armor"))) {
                        if (a.length == 2) {
                            GetMethods.getArmor(p, a[1]);
                            return true;
                        } else {
                            p.sendMessage(scd + "/get armor <hexcode>");
                            return true;
                        }
                    }
                } else {
                    noPerm(p);
                    return true;
                }
            }
            if (c.equalsIgnoreCase("fbt")) {
                if (cs.hasPermission("BuildFixes.fullbright")
                        && getConf(p).fbCmd()) {
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
                } else {
                    noPerm(p);
                    return true;
                }
            }
            if (c.equalsIgnoreCase("stencillist")) {
                if (cs.hasPermission("BuildFixes.stencillist")
                        && getConf(p).stenList()) {
                    try {
                        int i = 1;
                        if (a.length == 1) {
                            i = getInt(a[0]);
                        }
                        UtilMethods.getStencils(p, i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    noPerm(p);
                    return true;
                }
            }
            if (c.equalsIgnoreCase("schlist")) {
                if (cs.hasPermission("BuildFixes.schlist")
                        && getConf(p).schList()) {
                    try {
                        int i = 1;
                        String str = "null";
                        if (a.length == 1) {
                            if (isInt(a[0])) {
                                i = getInt(a[0]);
                            } else {
                                str = a[0];
                            }
                        }
                        if (a.length == 2) {
                            str = a[0];
                            i = getInt(a[1]);
                        }
                        UtilMethods.getSchematics(p, str, i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    noPerm(p);
                    return true;
                }
            }
            if (c.equalsIgnoreCase("bf")) {
                if (cs.hasPermission("BuildFixes.bfinfo")
                        && getConf(p).bfInfo()) {
                    if (a.length == 0) {
                        p.sendMessage(ter + "/bf [worlds], [version]");
                        return true;
                    }
                    if (a.length == 1) {
                        if (a[0].equalsIgnoreCase("worlds")) {
                            p.sendMessage(prim + "Worlds:");
                            p.sendMessage(ter + BuildFixes.worlds.keySet().toString());
                            return true;
                        }
                        if (a[0].equalsIgnoreCase("version")) {
                            UtilMethods.getVersion(p);
                            return true;
                        }
                    }
                } else {
                    noPerm(p);
                    return true;
                }
            }
        }
        return false;
    }

    private void noPerm(Player p) {
        p.sendMessage(scd
                + "Sorry, that feature is disabled, or you don't have permission to use it!");
    }

    private static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static Integer getInt(String s) {
        int i = 1;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            i = 1;
        }

        if (i <= 0) {
            i = 1;
        }
        return i;
    }

}
