package me.dags.BuildFixes.Commands;

import static me.dags.BuildFixes.BuildFixes.prim;
import static me.dags.BuildFixes.BuildFixes.scd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author dags_ <dags@dags.me>
 */

public class GetMethods {
    
    public static void getDoors(Player p) {
        if (p.hasPermission("BuildFixes.doors")) {
            p.getInventory().addItem(new ItemStack(Material.WOODEN_DOOR, 64));
            p.getInventory().addItem(new ItemStack(Material.IRON_DOOR_BLOCK, 64));
            p.sendMessage(prim + "Given doors!");
        } else {
            p.sendMessage(scd
                    + "Sorry, you do not have permission to use that Command!");
        }
    }

    public static void getEggs(Player p) {
        if (p.hasPermission("BuildFixes.eggs")) {
            p.getInventory().addItem(new ItemStack(Material.DRAGON_EGG));
            p.sendMessage(prim + "Given egg!");
        } else {
            p.sendMessage(scd
                    + "Sorry, you do not have permission to use that Command!");
        }
    }

    public static void getFurnaces(Player p) {
        if (p.hasPermission("BuildFixes.furnace")) {
            p.getInventory().addItem(new ItemStack(Material.BURNING_FURNACE, 64));
            p.sendMessage(prim + "Given furnaces!");
        } else {
            p.sendMessage(scd
                    + "Sorry, you do not have permission to use that Command!");
        }
    }

    public static void getGrass(Player p) {
        if (p.hasPermission("BuildFixes.grass")) {
            p.getInventory().addItem(new ItemStack((Material.LONG_GRASS), 64, (short) 1));
            p.sendMessage(prim + "Given grass!");
        } else {
            p.sendMessage(scd
                    + "Sorry, you do not have permission to use that Command!");
        }
    }

    public static void getHead(Player p, String s) {
        if (p.hasPermission("BuildFixes.heads")) {
            ItemStack is = new ItemStack(Material.SKULL_ITEM, 64, (short) 3);

            SkullMeta sm = (SkullMeta) is.getItemMeta();
            sm.setOwner(s);
            sm.setDisplayName(randomColor() + s);
            is.setItemMeta(sm.clone());

            p.getInventory().addItem(is);
            p.sendMessage(prim + "Given " + scd + s + "'s" + prim + " head!");
        } else {
            p.sendMessage(scd
                    + "Sorry, you do not have permission to use that Command!");
        }
    }

    public static void getLogs(Player p) {
        if (p.hasPermission("BuildFixes.logs")) {
            p.getInventory().addItem(new ItemStack(Material.LOG, 64, (short) 12));
            p.getInventory().addItem(new ItemStack(Material.LOG, 64, (short) 13));
            p.getInventory().addItem(new ItemStack(Material.LOG, 64, (short) 14));
            p.getInventory().addItem(new ItemStack(Material.LOG, 64, (short) 15));
            p.sendMessage(prim + "Given logs!");
        } else {
            p.sendMessage(scd
                    + "Sorry, you do not have permission to use that Command!");
        }
    }

    public static void getMushrooms(Player p) {
        if (p.hasPermission("BuildFixes.mushrooms")) {
            p.getInventory().addItem(new ItemStack(Material.HUGE_MUSHROOM_1, 64, (short) 0));
            p.sendMessage(prim + "Given mushrooms!");
        } else {
            p.sendMessage(scd
                    + "Sorry, you do not have permission to use that Command!");
        }
    }

    public static void getSlabs(Player p, String a) {
        if (p.hasPermission("BuildFixes.slabs")) {
            if (isInt(a)) {
                int dmg = Integer.valueOf(a);
                p.getInventory().addItem(new ItemStack(Material.DOUBLE_STEP, 64, (byte) dmg));
                p.sendMessage(prim + "Given slabs!");
            } else {
                p.sendMessage(scd + "/get slabs [0-16]");
            }
        } else {
            p.sendMessage(scd
                    + "Sorry, you do not have permission to use that Command!");
        }
    }
    
    public static void getArmor(Player p, String color) {
        if (p.hasPermission("BuildFixes.armor")) {
            ArrayList<ItemStack> armor = new ArrayList();
            armor.add(new ItemStack(Material.LEATHER_HELMET));
            armor.add(new ItemStack(Material.LEATHER_CHESTPLATE));
            armor.add(new ItemStack(Material.LEATHER_LEGGINGS));
            armor.add(new ItemStack(Material.LEATHER_BOOTS));
            try {
                java.awt.Color desiredcolor = java.awt.Color.decode(color);
                for (ItemStack is : armor) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
                    meta.setColor(org.bukkit.Color.fromRGB(desiredcolor.getRed(), desiredcolor.getGreen(), desiredcolor.getBlue()));
                    is.setItemMeta(meta);
                    p.getInventory().addItem(is);
                }
            } catch (NumberFormatException ex) {
                p.sendMessage(scd + "The color `" + color + "` is not valid");
            }
        } else {
            p.sendMessage(scd
                    + "Sorry, you do not have permission to use that Command!");
        }
    }

    private static boolean isInt(String str) {
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

    private static ChatColor randomColor() {
        List<ChatColor> list = new ArrayList<ChatColor>();
        for (ChatColor cc : ChatColor.values()) {
            if (!(cc.equals(ChatColor.BOLD)
                    || cc.equals(ChatColor.COLOR_CHAR)
                    || cc.equals(ChatColor.MAGIC)
                    || cc.equals(ChatColor.RESET)
                    || cc.equals(ChatColor.STRIKETHROUGH)
                    || cc.equals(ChatColor.UNDERLINE))) {
                list.add(cc);
            }
        }
        ChatColor c = list.get(getRandom(0, list.size() - 1));
        return c;
    }

    private static int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }

}
