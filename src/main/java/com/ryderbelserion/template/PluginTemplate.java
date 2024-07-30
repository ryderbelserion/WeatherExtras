package com.ryderbelserion.template;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginTemplate extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Starting up...");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down...");
    }
}