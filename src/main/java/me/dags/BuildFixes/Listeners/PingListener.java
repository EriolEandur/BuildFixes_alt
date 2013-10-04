package me.dags.BuildFixes.Listeners;

import me.dags.BuildFixes.BuildFixes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * @author dags_ <dags@dags.me>
 */

public class PingListener implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        if (BuildFixes.bfMotd) {
            event.setMotd(BuildFixes.motd);
        }
    }

}
