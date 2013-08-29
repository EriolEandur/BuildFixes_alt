package me.dags.BuildFixes.Listeners;

import static me.dags.BuildFixes.BuildFixes.decayBlock;
import static me.dags.BuildFixes.BuildFixes.formBlock;
import static me.dags.BuildFixes.BuildFixes.mobBlock;
import static me.dags.BuildFixes.BuildFixes.multiWorlds;
import static me.dags.BuildFixes.BuildFixes.weatherBlock;
import static me.dags.BuildFixes.MultiWorld.Worlds.worldsCFG;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * 
 * @author dags_ <dags@dags.me>
 */

public class EnvironmentListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	private void onWeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			if (multiWorlds) {
				event.setCancelled(worldsCFG.get(event.getWorld().getName()).get(9));
			} else {
				event.setCancelled(weatherBlock);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	private void onBlockBurn(BlockBurnEvent event) {
		if (multiWorlds) {
			event.setCancelled(worldsCFG.get(event.getBlock().getWorld().getName()).get(6));
		} else {
			event.setCancelled(decayBlock);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	private void onBlockFade(BlockFadeEvent event) {
		if (multiWorlds) {
			event.setCancelled(worldsCFG.get(event.getBlock().getWorld().getName()).get(6));
		} else {
			event.setCancelled(decayBlock);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	private void onLeavesDecay(LeavesDecayEvent event) {
		if (multiWorlds) {
			event.setCancelled(worldsCFG.get(event.getBlock().getWorld().getName()).get(6));
		} else {
			event.setCancelled(decayBlock);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	private void onBlockForm(BlockFormEvent event) {
		if (multiWorlds) {
			event.setCancelled(worldsCFG.get(event.getBlock().getWorld().getName()).get(7));
		} else {
			event.setCancelled(formBlock);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	private void onBlockGrow(BlockGrowEvent event) {
		if (multiWorlds) {
			event.setCancelled(worldsCFG.get(event.getBlock().getWorld().getName()).get(7));
		} else {
			event.setCancelled(formBlock);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	private void onBlockSpread(BlockSpreadEvent event) {
		if (multiWorlds) {
			event.setCancelled(worldsCFG.get(event.getBlock().getWorld().getName()).get(7));
		} else {
			event.setCancelled(formBlock);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	private void onMobSpawn(CreatureSpawnEvent event) {
		if (!event.getSpawnReason().equals(SpawnReason.CUSTOM)) {
			if (multiWorlds) {
				event.setCancelled(worldsCFG.get(event.getEntity().getWorld().getName()).get(8));
			} else {
				event.setCancelled(mobBlock);
			}
		}
	}

}
