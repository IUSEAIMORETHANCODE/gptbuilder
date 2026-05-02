package com.gptbuilder.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import com.gptbuilder.plugin.commands.GPTCommand;

public class GPTBuilderPlugin extends JavaPlugin {

    private static GPTBuilderPlugin instance;
    private String apiKey;
    private String model;
    private String baseUrl;

    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config if it doesn't exist
        saveDefaultConfig();
        
        // Load configuration
        loadConfiguration();
        
        // Register command
        getCommand("gpt").setExecutor(new GPTCommand(this));
        
        getLogger().info("GPT Builder Plugin has been enabled!");
        
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-openrouter-api-key-here")) {
            getLogger().warning("WARNING: OpenRouter API key not configured! Please set it in config.yml");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("GPT Builder Plugin has been disabled!");
    }

    private void loadConfiguration() {
        apiKey = getConfig().getString("openrouter.api-key", "");
        model = getConfig().getString("openrouter.model", "openrouter/auto");
        baseUrl = getConfig().getString("openrouter.base-url", "https://openrouter.io/api/v1");
    }

    public static GPTBuilderPlugin getInstance() {
        return instance;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getModel() {
        return model;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public long getTimeout() {
        return getConfig().getLong("building.timeout-seconds", 60);
    }

    public int getMaxBlocksPerBuild() {
        return getConfig().getInt("building.max-blocks-per-build", 5000);
    }

    public boolean isAnimationEnabled() {
        return getConfig().getBoolean("building.animation-enabled", true);
    }

    public long getAnimationSpeed() {
        return getConfig().getLong("building.animation-speed", 20);
    }

    public String getMessage(String key) {
        String message = getConfig().getString("messages." + key, "");
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }
}
