package me.dags.BuildFixes.Commands;

import me.dags.BuildFixes.Configuration.WorldConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;

import static me.dags.BuildFixes.BuildFixes.*;
import static me.dags.BuildFixes.Configuration.WorldConfig.getWorldConf;

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
            if (c.equalsIgnoreCase("vv")) {
                if (cs.hasPermission("BuildFixes.voxelviewer")
                        && getConf(p).schList()) {
                    try {
                        int i = 1;
                        String str = "null";
                        if (a.length == 1) {
                            if (isInt(a[0])) {
                                i = getInt(a[0]);
                            } else {
                                if (a[0].equalsIgnoreCase("stencillists")
                                        || a[0].equalsIgnoreCase("stencillist")
                                        || a[0].equalsIgnoreCase("list")) {
                                    str = "stencilLists";
                                } else {
                                    str = a[0];
                                }
                            }
                        }
                        if (a.length == 2) {
                            str = a[0];
                            i = getInt(a[1]);
                        }
                        UtilMethods.voxelViewer(p, str, i);
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
                        UtilMethods.schematicsViewer(p, str, i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    noPerm(p);
                    return true;
                }
            }
            if (c.equalsIgnoreCase("sl")) {
                if (p.hasPermission("BuildFixes.addstencils")
                        && getConf(p).stenGen()) {
                    if (a.length == 1) {
                        if (a[0].equalsIgnoreCase("save")) {
                            ListGenerator.saveList(p);
                            return true;
                        }
                    }
                    if (a.length >= 2) {
                        if (a[0].equalsIgnoreCase("create")) {
                            String name = a[1];
                            ListGenerator.createNewList(p, name);
                            return true;
                        }
                        if (a[0].equalsIgnoreCase("add")) {
                            ListGenerator.addToList(p, a);
                            return true;
                        }
                    }
                } else {
                    noPerm(p);
                    return true;
                }
            }
            if (c.equalsIgnoreCase("bf")) {
                if (a.length == 0) {
                    if (cs.hasPermission("BuildFixes.bfinfo")) {
                        BFMethods.bfInfo(p);
                        return true;
                    }
                } else {
                    if (a.length == 1) {
                        if (cs.hasPermission("BuildFixes.bfinfo")) {
                            if (a[0].equalsIgnoreCase("worlds")) {
                                BFMethods.listWorlds(p);
                                return true;
                            }
                            if (a[0].equalsIgnoreCase("version")) {
                                BFMethods.getVersion(p);
                                return true;
                            }
                        } else {
                            noPerm(p);
                            return true;
                        }
                    }
                    if (a.length == 2) {
                        if (a[0].equalsIgnoreCase("reload")) {
                            if (cs.hasPermission("BuildFixes.reload")) {
                                p.sendMessage(prim + "Reloading...");
                                BFMethods.reload(a);
                                p.sendMessage(prim + "Reload complete!");
                                return true;
                            } else {
                                noPerm(p);
                                return true;
                            }
                        } else {
                            noPerm(p);
                            return true;
                        }
                    }
                    if (a.length >= 3) {
                        if (a[0].equalsIgnoreCase("np")) {
                            if (cs.hasPermission("BuildFixes.npEdit")) {
                                if (a[1].equalsIgnoreCase("add")) {
                                    if (isInt(a[2])) {
                                        //BFMethods.npAdd(p, a);
                                        return true;
                                    } else {
                                        //p.sendMessage(ter + a[2] + " is not an integer!");
                                        return true;
                                    }
                                }
                                if (a[1].equalsIgnoreCase("remove")) {

                                }
                            } else {
                                //noPerm(p);
                                //return true;
                            }
                        }
                    }
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
