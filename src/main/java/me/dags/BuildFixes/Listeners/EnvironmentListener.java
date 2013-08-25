package me.dags.BuildFixes.Listeners;

import static me.dags.BuildFixes.BuildFixes.decayBlock;
import static me.dags.BuildFixes.BuildFixes.formBlock;
import static me.dags.BuildFixes.BuildFixes.mobBlock;
import static me.dags.BuildFixes.BuildFixes.multiWorlds;
import static me.dags.BuildFixes.BuildFixes.weatherBlock;
import me.dags.BuildFixes.WorldConfig.Worlds;

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

	@EventHandler(priority = EventPriority.HIGH)
	private void onWeatherChange(WeatherChangeEvent event) {
		if (!event.isCancelled()) {
			if (event.toWeatherState()) {
				if (multiWorlds) {
					event.setCancelled(Worlds.isCancelled("Weather",
							event.getWorld()));
				} else {
					event.setCancelled(weatherBlock);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockBurn(BlockBurnEvent event) {
		if (!event.isCancelled()) {
			if (multiWorlds) {
				event.setCancelled(Worlds.isCancelled("Decay", event.getBlock()
						.getWorld()));
			} else {
				event.setCancelled(decayBlock);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockFade(BlockFadeEvent event) {
		if (!event.isCancelled()) {
			if (multiWorlds) {
				event.setCancelled(Worlds.isCancelled("Decay", event.getBlock()
						.getWorld()));
			} else {
				event.setCancelled(decayBlock);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onLeavesDecay(LeavesDecayEvent event) {
		if (!event.isCancelled()) {
			if (multiWorlds) {
				event.setCancelled(Worlds.isCancelled("Decay", event.getBlock()
						.getWorld()));
			} else {
				event.setCancelled(decayBlock);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockForm(BlockFormEvent event) {
		if (!event.isCancelled()) {
			if (multiWorlds) {
				event.setCancelled(Worlds.isCancelled("Form", event.getBlock()
						.getWorld()));
			} else {
				event.setCancelled(formBlock);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockGrow(BlockGrowEvent event) {
		if (!event.isCancelled()) {
			if (multiWorlds) {
				event.setCancelled(Worlds.isCancelled("Form", event.getBlock()
						.getWorld()));
			} else {
				event.setCancelled(formBlock);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockSpread(BlockSpreadEvent event) {
		if (!event.isCancelled()) {
			if (multiWorlds) {
				event.setCancelled(Worlds.isCancelled("Form", event.getBlock()
						.getWorld()));
			} else {
				event.setCancelled(formBlock);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onMobSpawn(CreatureSpawnEvent event) {
		if (!event.isCancelled()) {
			if (!event.getSpawnReason().equals(SpawnReason.CUSTOM)) {
				if (multiWorlds) {
					event.setCancelled(Worlds.isCancelled("Mob", event
							.getEntity().getWorld()));
				} else {
					event.setCancelled(mobBlock);
				}
			}
		}
	}

}
