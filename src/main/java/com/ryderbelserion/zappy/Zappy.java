package com.ryderbelserion.zappy;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.vital.paper.commands.modules.ModuleHandler;
import com.ryderbelserion.vital.paper.commands.modules.ModuleLoader;
import com.ryderbelserion.zappy.commands.BaseCommand;
import com.ryderbelserion.zappy.managers.ConfigManager;
import com.ryderbelserion.zappy.managers.WeatherStarter;
import com.ryderbelserion.zappy.modules.WeatherModule;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class Zappy extends JavaPlugin {

    public static Zappy getPlugin() {
        return JavaPlugin.getPlugin(Zappy.class);
    }

    private WeatherStarter weatherStarter;

    @Override
    public void onEnable() {
        ConfigManager.load(getDataFolder());

        this.weatherStarter = new WeatherStarter().start().apply(weatherStarter -> {
            final ModuleLoader moduleLoader = weatherStarter.getModuleLoader();

            List.of(
                    new WeatherModule()
            ).forEach(moduleLoader::addModule);

            moduleLoader.load(this);
        });

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new BaseCommand().registerPermission().literal().createBuilder();

            event.registrar().register(root.build(), "the base command for WeatherExtras");
        });
    }

    @Override
    public void onDisable() {
        if (this.weatherStarter != null) {
            this.weatherStarter.apply(weatherStarter -> {
                final ModuleLoader moduleLoader = weatherStarter.getModuleLoader();

                moduleLoader.getModules().forEach(ModuleHandler::disable);
            });
        }
    }

    public final WeatherStarter getWeatherStarter() {
        return this.weatherStarter;
    }
}