package me.dags.BuildFixes.Listeners;

import static me.dags.BuildFixes.BuildFixes.worldsCFG;

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
 * 
 * @author dags_ <dags@dags.me>
 */

public class EnvironmentListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onWeatherChange(WeatherChangeEvent event) {
		if (worldsCFG.get(event.getWorld().getName()).get(8)) {
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
		if (event.getBlock().getTypeId() != 51) {
			event.setCancelled(worldsCFG.get(
					event.getBlock().getWorld().getName()).get(6));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onLeavesDecay(LeavesDecayEvent event) {
		event.setCancelled(worldsCFG.get(event.getBlock().getWorld().getName())
				.get(6));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockForm(BlockFormEvent event) {
		event.setCancelled(worldsCFG.get(event.getBlock().getWorld().getName())
				.get(7));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockGrow(BlockGrowEvent event) {
		event.setCancelled(worldsCFG.get(event.getBlock().getWorld().getName())
				.get(7));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockSpread(BlockSpreadEvent event) {
		if (event.getBlock().getTypeId() != 51) {
			event.setCancelled(worldsCFG.get(
					event.getBlock().getWorld().getName()).get(7));
		}
	}

}
