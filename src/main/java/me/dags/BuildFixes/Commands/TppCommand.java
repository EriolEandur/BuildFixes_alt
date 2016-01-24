/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.Commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Eriol_Eandur
 */
public class TppCommand implements CommandExecutor {

    JavaPlugin plugin;
    
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if(!(cs instanceof Player) && args.length<4) {
            cs.sendMessage(ChatColor.RED+"You need to be logged in to do this.");
            return true;
        }
        switch(args.length) {
            case 0:
                cs.sendMessage(ChatColor.RED+"Not enough arguments.");
                return true;
            case 1:
                if(!cs.hasPermission("BuildFixes.tpp.toplayer")) {
                    sendNoPerm(cs);
                    return true;
                }
                Player player = Bukkit.matchPlayer(args[0]).get(0);
                if(player==null) {
                    cs.sendMessage(ChatColor.RED+"Player not found.");
                    return true;
                }
                cs.sendMessage(ChatColor.GOLD+"Teleporting...");
                ((Player)cs).teleport(player.getLocation());
                return true;
            case 2:
                if(!cs.hasPermission("BuildFixes.tpp.other.toplayer")) {
                    sendNoPerm(cs);
                    return true;
                }
                player = Bukkit.matchPlayer(args[0]).get(0);
                if(player==null) {
                    cs.sendMessage(ChatColor.RED+"Player not found.");
                    return true;
                }
                Player target = Bukkit.matchPlayer(args[1]).get(0);
                if(target==null) {
                    cs.sendMessage(ChatColor.RED+"Player not found.");
                    return true;
                }
                player.sendMessage(ChatColor.GOLD+"Teleporting...");
                player.teleport(target.getLocation());
                return true;
            case 3:
                if(!cs.hasPermission("BuildFixes.tpp.tokoord")) {
                    sendNoPerm(cs);
                    return true;
                }
                ((Player)cs).sendMessage(ChatColor.GOLD+"Teleporting...");
                Bukkit.dispatchCommand(cs, "tp "+((Player)cs).getName()
                                                +" "+args[0]
                                                +" "+args[1]
                                                +" "+args[2]);
                return true;
            default:
                if(!cs.hasPermission("BuildFixes.tpp.other")) {
                    sendNoPerm(cs);
                    return true;
                }
                player = Bukkit.matchPlayer(args[0]).get(0);
                if(player==null) {
                    cs.sendMessage(ChatColor.RED+"Player not found.");
                    return true;
                }
                Bukkit.dispatchCommand(cs, "tp "+player.getName()
                                                +" "+args[1]
                                                +" "+args[2]
                                                +" "+args[3]);
                return true;
        }
    }
    
    private void sendNoPerm(CommandSender cs) {
        cs.sendMessage(ChatColor.RED+"You don't have permission to do this.");
    }
}
