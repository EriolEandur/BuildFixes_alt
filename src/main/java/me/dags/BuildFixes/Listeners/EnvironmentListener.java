package me.dags.BuildFixes.Listeners;

import static me.dags.BuildFixes.Configuration.WorldConfig.getWorldConf;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * @author dags_ <dags@dags.me>
 */

public class EnvironmentListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onWeatherChange(WeatherChangeEvent event) {
        World w = event.getWorld();

        if (getWorldConf(w).weather()) {
            if (event.toWeatherState()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onBlockBurn(BlockBurnEvent event) {
        World w = event.getBlock().getWorld();

        event.setCancelled(w.getGameRuleValue("doFireTick").equals("false"));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onBlockFade(BlockFadeEvent event) {
        World w = event.getBlock().getWorld();

        if (event.getBlock().getType().equals(Material.FIRE)) {
            event.setCancelled(getWorldConf(w).decay());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onLeavesDecay(LeavesDecayEvent event) {
        World w = event.getBlock().getWorld();

        event.setCancelled(getWorldConf(w).decay());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onBlockForm(BlockFormEvent event) {
        World w = event.getBlock().getWorld();

        event.setCancelled(getWorldConf(w).form());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onBlockGrow(BlockGrowEvent event) {
        World w = event.getBlock().getWorld();

        event.setCancelled(getWorldConf(w).form());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onBlockSpread(BlockSpreadEvent event) {
        World w = event.getBlock().getWorld();

        event.setCancelled(getWorldConf(w).form());
    }

}
