package com.ryderbelserion.zappy;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.vital.paper.commands.modules.ModuleHandler;
import com.ryderbelserion.vital.paper.commands.modules.ModuleLoader;
import com.ryderbelserion.zappy.commands.BaseCommand;
import com.ryderbelserion.zappy.managers.ConfigManager;
import com.ryderbelserion.zappy.managers.ZappyManager;
import com.ryderbelserion.zappy.modules.WeatherModule;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class Zappy extends JavaPlugin {

    public static Zappy getPlugin() {
        return JavaPlugin.getPlugin(Zappy.class);
    }

    private ZappyManager zappy;

    @Override
    public void onEnable() {
        ConfigManager.load(getDataFolder());

        this.zappy = new ZappyManager().start().apply(zappy -> {
            final ModuleLoader moduleLoader = zappy.getModuleLoader();

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
        if (this.zappy != null) {
            this.zappy.apply(zappy -> {
                final ModuleLoader moduleLoader = zappy.getModuleLoader();

                moduleLoader.getModules().forEach(ModuleHandler::disable);
            });
        }
    }

    public final ZappyManager getZappy() {
        return this.zappy;
    }
}