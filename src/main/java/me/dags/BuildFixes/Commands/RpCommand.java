/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.Commands;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
public class RpCommand implements CommandExecutor {

    public static final String PERMISSION ="BuildFixes.resourcePack";
    
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("This command can only be run by a player");
            return true;
        }
        if(!((Player)cs).hasPermission(PERMISSION)) {
            sendNoPermissionErrorMessage(cs);
            return true;
        }
        if(args.length<1) {
            sendNotEnoughArgumentsMessage(cs);
            return true;
        }
        String url = "";
        if(args[0].toLowerCase().startsWith("e")) {
            url = "http://www.mcmiddleearth.com/content/Eriador.zip";
        } 
        else if(args[0].toLowerCase().startsWith("g")) {
            url = "http://www.mcmiddleearth.com/content/Gondor.zip";
        } 
        else if(args[0].toLowerCase().startsWith("l")) {
            url = "http://www.mcmiddleearth.com/content/Lothlorien.zip";
        }
        else if(args[0].toLowerCase().startsWith("r")) {
            url = "http://www.mcmiddleearth.com/content/Rohan.zip";
        }
        else if(args[0].toLowerCase().startsWith("mori")) {
            url = "http://www.mcmiddleearth.com/content/Moria.zip";
        }
        else if(args[0].toLowerCase().startsWith("mord")) {
            url = "http://www.mcmiddleearth.com/content/Mordor.zip";
        }
        Logger.getGlobal().info("test");
        try {
            URLConnection connection= new URL(url).openConnection();
            String type = connection.getContentType();
            if(type.startsWith("text/html")) {
                sendRPNotFoundMessage(cs);
                return true;
            }
        }
        catch(IOException e) {
            return true;
        }
        ((Player)cs).setResourcePack(url);
        return true;
    }

    private void sendNoPermissionErrorMessage(CommandSender cs) {
        cs.sendMessage("You don't have permission to do this.");
    }

    private void sendNotEnoughArgumentsMessage(CommandSender cs) {
        cs.sendMessage("Not enough arguments.");
    }

    private void sendRPNotFoundMessage(CommandSender cs) {
        cs.sendMessage("Resource pack not found. Current RPs are: Gondor, Rohan, Eriador, Lothlorien. Possibly Moria and Mordor are added sometime.");
    }
    
}
