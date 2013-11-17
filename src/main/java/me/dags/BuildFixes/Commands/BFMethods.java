package me.dags.BuildFixes.Commands;

import me.dags.BuildFixes.BuildFixes;
import me.dags.BuildFixes.Configuration.WorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import static me.dags.BuildFixes.BuildFixes.*;

/**
 * @author dags_ <dags@dags.me>
 */

public class BFMethods {

    public static void bfInfo(Player p) {
        p.sendMessage(ter + "/bf [worlds], [version]");
    }

    public static void listWorlds(Player p) {
        p.sendMessage(prim + "Worlds:");
        p.sendMessage(ter + worlds.keySet().toString());
    }

    public static void getVersion(Player p) {

        PluginDescriptionFile plug = BuildFixes.inst().getDescription();
        String pref = scd + "|---[";
        String suf = scd + "]---|";

        p.sendMessage(pref + prim + plug.getName() + suf);
        p.sendMessage(scd + "Version: " + prim + plug.getVersion());
        p.sendMessage(scd + "Author(s): " + prim + plug.getAuthors());

    }

    public static void reload(String[] a) {

        if (a[1].equalsIgnoreCase("-a")) {
            for (String s : worlds.keySet()) {
                reloadWorld(s);
            }
        } else {
            for (String s : worlds.keySet()) {
                if (s.equalsIgnoreCase(a[1])) {
                    reloadWorld(s);
                    break;
                }
            }
        }

    }

    public static void npAdd(Player p, String[] a) {

        int i = getInt(a[2]);
        boolean added = false;

        if (a[3].equalsIgnoreCase("-a")) {
            for (String s : worlds.keySet()) {
                WorldConfig wc = worlds.get(s);
                if (!wc.noPhysList().contains(i)) {
                    wc.noPhysList().add(i);
                    added = true;
                }
            }
        } else {
            for (String s : worlds.keySet()) {
                if (s.equalsIgnoreCase(a[3])) {
                    WorldConfig wc = worlds.get(s);
                    if (!wc.noPhysList().contains(i)) {
                        wc.noPhysList().add(i);
                        added = true;
                    }
                }
            }
        }

        if (added) {
            p.sendMessage(prim + i + " has been added!");
        } else {
            p.sendMessage(ter + i + " was not added.");
        }

    }

    // UTILS
    private static void reloadWorld(String s) {
        World w = Bukkit.getWorld(s);

        BuildFixes.log("Reloading config for world: " + w.getName());

        WorldConfig conf = new WorldConfig(w);
        BuildFixes.worlds.put(w.getName(), conf);
    }

    private static Integer getInt(String s) {
        int i;
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
