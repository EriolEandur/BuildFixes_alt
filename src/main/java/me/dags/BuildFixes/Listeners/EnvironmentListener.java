package me.dags.BuildFixes.Listeners;

import static me.dags.BuildFixes.BuildFixes.decayBlock;
import static me.dags.BuildFixes.BuildFixes.formBlock;
import static me.dags.BuildFixes.BuildFixes.weatherBlock;

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

	@EventHandler(priority = EventPriority.HIGH)
	private void onWeatherChange(WeatherChangeEvent event) {
		if (weatherBlock) {
			if (event.toWeatherState()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockBurn(BlockBurnEvent event) {
		if (decayBlock) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockFade(BlockFadeEvent event) {
		if (decayBlock) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onLeavesDecay(LeavesDecayEvent event) {
		if (decayBlock) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockForm(BlockFormEvent event) {
		if (formBlock) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockGrow(BlockGrowEvent event) {
		if (formBlock) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockSpread(BlockSpreadEvent event) {
		if (formBlock) {
			event.setCancelled(true);
		}
	}

}
