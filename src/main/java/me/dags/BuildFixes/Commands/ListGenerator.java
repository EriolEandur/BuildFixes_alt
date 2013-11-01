package me.dags.BuildFixes.Commands;

import me.dags.BuildFixes.BuildFixes;
import me.dags.BuildFixes.Commands.StencilListBuilder.StencilListBuilder;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.dags.BuildFixes.BuildFixes.prim;
import static me.dags.BuildFixes.BuildFixes.scd;
import static me.dags.BuildFixes.BuildFixes.ter;

/**
 * @author dags_ <dags@dags.me>
 */

public class ListGenerator {

    public static HashMap<String, StencilListBuilder> stencilLists = new HashMap<String, StencilListBuilder>();

    public static void createNewList(Player p, String s) {
        if (!s.contains("/")) {
            StencilListBuilder sl = new StencilListBuilder();
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
            StencilListBuilder sl = stencilLists.get(p.getName());

            if (sl.writeToFile()) {
                p.sendMessage(prim + "Your stencil list has been saved!");
                p.sendMessage(scd + "Name: " + prim + sl.getName());
                p.sendMessage(scd + "Stencils: " + prim + sl.getStencils().size());

                stencilLists.remove(p.getName());
            } else {
                p.sendMessage(ter + "A StencilList called " + scd + sl.getName() + ter + " already exists!");
                p.sendMessage(ter + "Overwriting existing stencilLists is currently blocked. " +
                        "The server owner must first delete the existing list if you wish to continue!");
            }
        } else {
            p.sendMessage(ter + "You haven't created a new Stencil List yet!");
        }
    }

    public static void addToList(Player p, String[] s) {
        if (stencilLists.containsKey(p.getName())) {
            StencilListBuilder sl = stencilLists.get(p.getName());

            p.sendMessage(ter + "Adding matches...");
            for (String path : s) {
                if (!path.equals("add")) {
                    for (String match : findMatches(p, path)) {
                        sl.addStencil(match);
                    }
                }
            }
            stencilLists.put(p.getName(), sl);

            String st = " stencils";
            if (sl.getStencils().size() == 1) {
                st = " stencil";
            }

            p.sendMessage(prim + "Your list currently has "
                    + scd + sl.getStencils().size() + prim + st + "!");
        } else {
            p.sendMessage(ter + "You haven't created a new Stencil List yet!");
        }
    }

    public static List<String> findMatches(Player p, String s) {
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
        if (matches.isEmpty()) {
            p.sendMessage(scd + "No matches found for: " + ter + s);
        } else {
            String m = " matches ";
            if (matches.size() == 1) {
                m = " match ";
            }
            p.sendMessage(scd + matches.size() + prim + m + "found for: " + ter + s);
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
            path[0] = "";
            path[1] = s;
        }
        return path;
    }
}
