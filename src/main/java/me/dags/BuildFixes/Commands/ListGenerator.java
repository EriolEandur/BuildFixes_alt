package me.dags.BuildFixes.Commands;

import me.dags.BuildFixes.BuildFixes;
import me.dags.BuildFixes.Commands.SteniclManager.StencilList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.dags.BuildFixes.BuildFixes.prim;
import static me.dags.BuildFixes.BuildFixes.scd;

/**
 * @author dags_ <dags@dags.me>
 */

public class ListGenerator {

    private static HashMap<String, StencilList> stencilLists = new HashMap<String, StencilList>();

    public static void createNewList(Player p, String s) {
        if (!s.contains("/")) {
            StencilList sl = new StencilList();
            sl.setName(s);

            stencilLists.put(p.getName(), sl);
            p.sendMessage(prim + "Initialised new StencilList: " + scd + sl.getName());
            p.sendMessage(prim + "Stencils are ready to be added!");
        } else {
            p.sendMessage(scd + "Saving to sub-folders is not supported!");
        }
    }

    public static void saveList(Player p) {
        if (stencilLists.containsKey(p.getName())) {
            StencilList sl = stencilLists.get(p.getName());
            sl.writeToFile();

            p.sendMessage(prim + "Your stencil list has been saved!");
            p.sendMessage(scd + "Name: " + prim + sl.getName());
            p.sendMessage(scd + "Entries: " + prim + sl.getStencils().size());
        } else {
            p.sendMessage(scd + "You haven't created a new Stencil List yet!");
        }
    }

    public static void addToList(Player p, String[] s) {
        if (stencilLists.containsKey(p.getName())) {
            StencilList sl = stencilLists.get(p.getName());

            for (String path : s) {
                if (!path.equals("add")) {
                    for (String match : findMatches(path)) {
                        sl.addStencil(match);
                    }
                }
            }
            stencilLists.put(p.getName(), sl);
            p.sendMessage(prim + "Your list currently has "
                    + scd + sl.getStencils().size() + prim + " stencils!");
            p.sendMessage(scd + "Changes ready to be saved!");
        } else {
            p.sendMessage(scd + "You haven't created a new Stencil List yet!");
        }
    }

    public static List<String> findMatches(String s) {
        List<String> matches = new ArrayList<String>();
        String[] path = getFilePath(s);

        File folder = new File(getVoxelDir() + "/stencils/" + path[0]);

        if (folder.exists()) {
            for (File f : folder.listFiles()) {
                if (f.getPath().contains(path[1])) {
                    if (f.getName().endsWith(".vstencil")) {
                        matches.add(path[0] + f.getName().replace(".vstencil", ""));
                    }
                }
            }
        }
        return matches;
    }

    private static String getVoxelDir() {
        return BuildFixes.inst().getDataFolder().getParent() + "/VoxelSniper";
    }

    private static String[] getFilePath(String s) {
        String[] path = new String[2];

        if (s.contains("/")) {
            StringBuilder sb = new StringBuilder();
            String[] split = s.split("/");
            int length = split.length - 1;

            int i = 0;
            while (i < length) {
                sb.append(split[i] + "/");
                i++;
            }

            path[0] = sb.toString();

            if (path.length == 1) {
                path[1] = s.replace("/", "");
            } else {
                path[1] = split[length];
            }
        } else {
            path[0] = s;
            path[1] = s;
        }
        return path;
    }
}
