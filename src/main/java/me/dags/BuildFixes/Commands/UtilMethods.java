package me.dags.BuildFixes.Commands;

import static me.dags.BuildFixes.BuildFixes.prim;
import static me.dags.BuildFixes.BuildFixes.scd;
import static me.dags.BuildFixes.BuildFixes.ter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import me.dags.BuildFixes.BuildFixes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

/**
 * @author dags_ <dags@dags.me>
 */

public class UtilMethods {

    public static JavaPlugin instance = (JavaPlugin) BuildFixes.inst();

    public static void getStencils(Player p, Integer i) throws IOException {

        File folder = new File(getPluginsDir() + "/VoxelSniper/stencilLists");

        if (!folder.exists()) {
            p.sendMessage(ter
                    + "VoxelSniper's StencilList folder cannot be found!");
        } else {
            String[] contents = folder.list();
            Arrays.sort(contents);
            if (contents.length != 0) {
                boolean foundFiles = false;

                StringBuilder text = new StringBuilder();
                text.append(scd + "Stencil Lists:" + "\n");

                for (String s : contents) {
                    if (s.endsWith(".txt")) {
                        String shorter = s.replace(".txt", "");
                        text.append(ter + shorter + "\n");
                        foundFiles = true;
                    }
                }
                if (!foundFiles) {
                    text.append(ter + "-" + "\n");
                }

                String header = (prim + "|--------[StencilList Search]--------|");
                chatPages(p, text, i, header);
            } else {
                p.sendMessage(ter + "Directory is empty!");
            }
        }

    }

    public static void getSchematics(Player p, String str, Integer i) throws IOException {
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
