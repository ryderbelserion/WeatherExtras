package com.ryderbelserion.zappy.managers;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.paper.commands.modules.ModuleLoader;
import com.ryderbelserion.zappy.Zappy;
import com.ryderbelserion.zappy.managers.config.Config;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public class ZappyManager extends Vital {

    private final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Zappy.class);

    public ZappyManager() {}

    private ModuleLoader moduleLoader;

    public final ZappyManager start() {
        this.moduleLoader = new ModuleLoader();

        return this;
    }

    public final ZappyManager apply(final Consumer<ZappyManager> consumer) {
        consumer.accept(this);

        return this;
    }

    public final ModuleLoader getModuleLoader() {
        return this.moduleLoader;
    }

    @Override
    public @NotNull final File getDirectory() {
        return this.plugin.getDataFolder();
    }

    @Override
    public void saveResource(@NotNull final String fileName, final boolean replaceExisting) {
        this.plugin.saveResource(fileName, replaceExisting);
    }

    @Override
    public final boolean isAdventure() {
        return true;
    }

    @Override
    public ComponentLogger getLogger() {
        return this.plugin.getComponentLogger();
    }

    @Override
    public final boolean isLogging() {
        return ConfigManager.getConfig().getProperty(Config.verbose_logging);
    }
}