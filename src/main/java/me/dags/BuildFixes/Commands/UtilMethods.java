package me.dags.BuildFixes.Commands;

import me.dags.BuildFixes.BuildFixes;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static me.dags.BuildFixes.BuildFixes.*;

/**
 * @author dags_ <dags@dags.me>
 */

public class UtilMethods {

    public static JavaPlugin instance = (JavaPlugin) BuildFixes.inst();

    public static void voxelViewer(Player p, String str, Integer i) throws IOException {
        String ext = "";
        if (!str.equals("null")) {
            ext = "/" + str;
        }
        File folder = new File(getPluginsDir() + "/VoxelSniper/" + ext);
        if (!folder.exists()) {
            p.sendMessage(ter + "Folder cannot be found!");
        } else {
            File[] contents = folder.listFiles();
            Arrays.sort(contents);

            if (contents.length != 0) {
                boolean foundDir = false;
                boolean foundFiles = false;

                StringBuilder text = new StringBuilder();
                text.append(scd + "SubDirectories:" + "\n");
                for (File f : contents) {
                    if (f.isDirectory()) {
                        text.append(ter + f.getName() + "/" + "\n");
                        foundDir = true;
                    }
                }
                if (!foundDir) {
                    text.append(ter + "-" + "\n");
                }

                text.append(scd + "Files:" + "\n");
                for (File f : contents) {
                    if (f.isFile()) {
                        if (f.getName().endsWith(".txt")
                                || f.getName().endsWith(".vstencil")) {
                            text.append(ter + f.getName() + "\n");
                            foundFiles = true;
                        }
                    }
                }
                if (!foundFiles) {
                    text.append(ter + "-" + "\n");
                }


                String header = (prim + "|--------[Voxel Viewer]--------|");
                chatPages(p, text, i, header);
            } else {
                p.sendMessage(ter + "Directory is empty!");
            }
        }
    }

    public static void schematicsViewer(Player p, String str, Integer i) throws IOException {
        String ext = "";
        if (!str.equals("null")) {
            ext = "/" + str;
        }
        File folder = new File(getPluginsDir() + "/WorldEdit/schematics" + ext);
        if (!folder.exists()) {
            p.sendMessage(ter + "Folder cannot be found!");
        } else {
            String[] contents = folder.list();
            Arrays.sort(contents);

            if (contents.length != 0) {
                boolean foundDir = false;
                boolean foundFiles = false;

                StringBuilder text = new StringBuilder();

                text.append(scd + "SubDirectories:" + "\n");
                for (String s : contents) {
                    if (!s.contains(".")) {
                        text.append(ter + s + "/" + "\n");
                        foundDir = true;
                    }
                }
                if (!foundDir) {
                    text.append(ter + "-" + "\n");
                }

                text.append(scd + "Files:" + "\n");
                for (String s : contents) {
                    if (s.contains(".")) {
                        text.append(ter + s + "\n");
                        foundFiles = true;
                    }
                }
                if (!foundFiles) {
                    text.append(ter + "-" + "\n");
                }

                String header = (prim + "|--------[Schematics Search]--------|");
                chatPages(p, text, i, header);
            } else {
                p.sendMessage(ter + "Directory is empty!");
            }
        }
    }

    private static void chatPages(Player p, StringBuilder text, Integer i, String header) {
        ChatPage page = ChatPaginator.paginate(text.toString(), i, ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH, 16);
        p.sendMessage(header + " [" + page.getPageNumber() + "/" + page.getTotalPages() + "]");
        p.sendMessage(page.getLines());
    }

    private static String getPluginsDir() {
        return instance.getDataFolder().getParent();
    }

    public static void getVersion(Player p) {
        PluginDescriptionFile plug = BuildFixes.inst().getDescription();
        String pref = scd + "|---[";
        String suf = scd + "]---|";

        p.sendMessage(pref + prim + plug.getName() + suf);
        p.sendMessage(scd + "Version: " + prim + plug.getVersion());
        p.sendMessage(scd + "Author(s): " + prim + plug.getAuthors());
    }

}
