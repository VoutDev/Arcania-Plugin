package me.vout.core.arcania.listener;

import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.UUID;

public class WorldListener implements Listener {

    @EventHandler
    public void onWorldLoaded(WorldLoadEvent event) {
        UUID worldUUId = event.getWorld().getUID();
        ArcaniaProvider.getPlugin().getBlockTaggingService().loadWorldData(worldUUId);
    }
}
