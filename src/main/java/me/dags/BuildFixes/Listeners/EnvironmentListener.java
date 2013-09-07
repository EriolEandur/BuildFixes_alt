package me.dags.BuildFixes.Listeners;

import static me.dags.BuildFixes.BuildFixes.worldsCFG;

import me.dags.BuildFixes.BuildFixes;
import me.dags.BuildFixes.Configuration.ConfigUtil;
import me.dags.BuildFixes.Configuration.Global;
import me.dags.BuildFixes.Configuration.MultiWorlds;

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
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class EnvironmentListener implements Listener {
	
	@EventHandler
	private void onWorldLoad(WorldLoadEvent event) {
		World w = event.getWorld();
		if (!worldsCFG.containsKey(w)) {			
			if (BuildFixes.multiWorlds) {
				JavaPlugin bf = (JavaPlugin) BuildFixes.inst();
				ConfigUtil cfg = new ConfigUtil(bf, w.getName());
				cfg.getWorldConfig().options().copyDefaults(true);
				cfg.saveWorldConfig();
				MultiWorlds.multiWorld(w);
			} else {
				Global.config(w);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onWeatherChange(WeatherChangeEvent event) {
		String w = event.getWorld().getName();
		
		if (worldsCFG.containsKey(w) &&  worldsCFG.get(w).get(8)) {
			if (event.toWeatherState()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockBurn(BlockBurnEvent event) {
		if (event.getBlock().getWorld().getGameRuleValue("doFireTick")
				.equals("false")) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockFade(BlockFadeEvent event) {
		String w = event.getBlock().getWorld().getName();
		
		if (event.getBlock().getTypeId() != 51
				&& worldsCFG.containsKey(w)) {
			event.setCancelled(worldsCFG.get(w).get(6));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onLeavesDecay(LeavesDecayEvent event) {
		String w = event.getBlock().getWorld().getName();
		
		if (worldsCFG.containsKey(w)) {
			event.setCancelled(worldsCFG.get(w).get(6));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockForm(BlockFormEvent event) {
		String w = event.getBlock().getWorld().getName();
		
		if (worldsCFG.containsKey(w)) {
			event.setCancelled(worldsCFG.get(w).get(7));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockGrow(BlockGrowEvent event) {
		String w = event.getBlock().getWorld().getName();
		
		if (worldsCFG.containsKey(w)) {
			event.setCancelled(worldsCFG.get(w).get(7));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockSpread(BlockSpreadEvent event) {
		String w = event.getBlock().getWorld().getName();
		
		if (event.getBlock().getTypeId() != 51) {
			if (worldsCFG.containsKey(w)) {
				event.setCancelled(worldsCFG.get(w).get(7));
			}
		}
	}

}
