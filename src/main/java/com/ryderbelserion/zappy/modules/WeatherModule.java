package com.ryderbelserion.zappy.modules;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.vital.paper.commands.modules.ModuleHandler;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import com.ryderbelserion.zappy.Zappy;
import com.ryderbelserion.zappy.managers.ConfigManager;
import com.ryderbelserion.zappy.managers.config.Config;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WeatherModule extends ModuleHandler {

    private final Zappy plugin = Zappy.getPlugin();

    private final SettingsManager config = ConfigManager.getConfig();

    private final Map<UUID, ScheduledTask> tasks = new HashMap<>();

    @Override
    public final String getName() {
        return "Weather Module";
    }

    @Override
    public final boolean isEnabled() {
        return this.config.getProperty(Config.world_time) != -1;
    }

    @Override
    public void enable() {
        final List<String> worlds = this.config.getProperty(Config.worlds);

        worlds.forEach(key -> {
            String[] pair = key.split(";");

            final World world = this.plugin.getServer().getWorld(pair[0]);

            if (world == null || world.getEnvironment() == World.Environment.NETHER || world.getEnvironment() == World.Environment.THE_END) return;

            final UUID uuid = world.getUID();

            this.tasks.putIfAbsent(uuid, new FoliaRunnable(this.plugin.getServer().getGlobalRegionScheduler()) {
                @Override
                public void run() {
                    // If it's not enabled at any time for any reason, we cancel.
                    if (!isEnabled()) {
                        final ScheduledTask task = tasks.get(world.getUID());

                        if (task != null) {
                            task.cancel();
                        }

                        tasks.remove(world.getUID());

                        return;
                    }

                    // If the weather is clear, set a storm.
                    if (world.isClearWeather()) {
                        world.setStorm(true);
                    }
                }
            }.runAtFixedRate(this.plugin, 0, this.config.getProperty(Config.world_time)));
        });
    }

    @Override
    public void reload() {
        if (!isEnabled()) {
            disable();

            return;
        }

        // Purge old tasks
        this.tasks.values().forEach(ScheduledTask::cancel);
        this.tasks.clear();

        // Re-populate, the config value might've been changed.
        enable();
    }

    @Override
    public void disable() {
        this.tasks.values().forEach(ScheduledTask::cancel);
        this.tasks.clear();
    }

    @EventHandler
    public void onWeatherChangeEvent(WeatherChangeEvent event) {
        final World world = event.getWorld();

        final UUID uuid = world.getUID();

        final ScheduledTask task = this.tasks.get(uuid);

        if (task == null || task.isCancelled()) return;

        //todo() do any checks needed, if we have issues with other plugins.
    }
}