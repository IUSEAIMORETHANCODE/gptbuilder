package com.gptbuilder.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.gptbuilder.plugin.GPTBuilderPlugin;
import com.gptbuilder.plugin.api.OpenRouterAPI;
import com.gptbuilder.plugin.building.BuildingExecutor;

public class GPTCommand implements CommandExecutor {
    
    private final GPTBuilderPlugin plugin;

    public GPTCommand(GPTBuilderPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        // Check if sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("gptbuilder.use")) {
            player.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }

        // Check if prompt is provided
        if (args.length == 0) {
            player.sendMessage(plugin.getMessage("invalid-prompt"));
            return true;
        }

        // Combine all arguments into a single prompt
        StringBuilder promptBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            promptBuilder.append(args[i]);
            if (i < args.length - 1) {
                promptBuilder.append(" ");
            }
        }
        String prompt = promptBuilder.toString();

        // Send message to player
        player.sendMessage(plugin.getMessage("building-started"));

        // Run asynchronously to avoid blocking the server
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Get AI response
                String buildCommands = OpenRouterAPI.sendBuildRequest(prompt);
                
                // Execute building on main thread
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    try {
                        BuildingExecutor.executeBuildCommands(player, buildCommands);
                        player.sendMessage(plugin.getMessage("building-complete"));
                    } catch (Exception e) {
                        player.sendMessage(plugin.getMessage("building-error")
                            .replace("{error}", e.getMessage()));
                        plugin.getLogger().severe("Error executing build: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    player.sendMessage(plugin.getMessage("building-error")
                        .replace("{error}", e.getMessage()));
                    plugin.getLogger().severe("Error communicating with OpenRouter: " + e.getMessage());
                });
            }
        });

        return true;
    }
}
