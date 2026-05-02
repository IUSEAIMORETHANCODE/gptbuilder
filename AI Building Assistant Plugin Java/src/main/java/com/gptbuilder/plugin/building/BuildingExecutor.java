package com.gptbuilder.plugin.building;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.gptbuilder.plugin.GPTBuilderPlugin;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildingExecutor {
    
    private static final Pattern SETBLOCK_PATTERN = Pattern.compile(
        "/setblock\\s+([\\d\\-~^]+)\\s+([\\d\\-~^]+)\\s+([\\d\\-~^]+)\\s+([\\w_]+)(?:\\s+(.*))?",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern FILL_PATTERN = Pattern.compile(
        "/fill\\s+([\\d\\-~^]+)\\s+([\\d\\-~^]+)\\s+([\\d\\-~^]+)\\s+([\\d\\-~^]+)\\s+([\\d\\-~^]+)\\s+([\\d\\-~^]+)\\s+([\\w_]+)(?:\\s+(.*))?",
        Pattern.CASE_INSENSITIVE
    );

    public static void executeBuildCommands(Player player, String commands) throws Exception {
        Location baseLocation = player.getLocation();
        int blockCount = 0;
        int maxBlocks = GPTBuilderPlugin.getInstance().getMaxBlocksPerBuild();

        // Split commands by newline and process each
        String[] lines = commands.split("\n");
        for (String line : lines) {
            line = line.trim();
            
            if (line.isEmpty() || !line.startsWith("/")) {
                continue;
            }

            // Try to match setblock command
            Matcher setblockMatcher = SETBLOCK_PATTERN.matcher(line);
            if (setblockMatcher.find()) {
                int x = parseCoordinate(setblockMatcher.group(1), (int) baseLocation.getX());
                int y = parseCoordinate(setblockMatcher.group(2), (int) baseLocation.getY());
                int z = parseCoordinate(setblockMatcher.group(3), (int) baseLocation.getZ());
                String blockName = setblockMatcher.group(4);

                setBlock(baseLocation.getWorld().getBlockAt(baseLocation.getWorld().getName(),
                    baseLocation.getBlockX() + x, baseLocation.getBlockY() + y, baseLocation.getBlockZ() + z),
                    blockName);

                blockCount++;
                if (blockCount > maxBlocks) {
                    throw new Exception("Build exceeded maximum block limit of " + maxBlocks);
                }
                continue;
            }

            // Try to match fill command
            Matcher fillMatcher = FILL_PATTERN.matcher(line);
            if (fillMatcher.find()) {
                int x1 = parseCoordinate(fillMatcher.group(1), (int) baseLocation.getX());
                int y1 = parseCoordinate(fillMatcher.group(2), (int) baseLocation.getY());
                int z1 = parseCoordinate(fillMatcher.group(3), (int) baseLocation.getZ());
                int x2 = parseCoordinate(fillMatcher.group(4), (int) baseLocation.getX());
                int y2 = parseCoordinate(fillMatcher.group(5), (int) baseLocation.getY());
                int z2 = parseCoordinate(fillMatcher.group(6), (int) baseLocation.getZ());
                String blockName = fillMatcher.group(7);

                fillArea(baseLocation, baseLocation.getBlockX() + x1, baseLocation.getBlockY() + y1,
                    baseLocation.getBlockZ() + z1, baseLocation.getBlockX() + x2, 
                    baseLocation.getBlockY() + y2, baseLocation.getBlockZ() + z2, blockName);

                // Count blocks in fill area
                blockCount += Math.abs((x2 - x1 + 1) * (y2 - y1 + 1) * (z2 - z1 + 1));
                if (blockCount > maxBlocks) {
                    throw new Exception("Build exceeded maximum block limit of " + maxBlocks);
                }
            }
        }
    }

    private static int parseCoordinate(String coord, int baseCoord) {
        if (coord.startsWith("~")) {
            String offset = coord.substring(1);
            if (offset.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(offset);
        }
        return Integer.parseInt(coord) - baseCoord;
    }

    private static void setBlock(Block block, String blockName) {
        try {
            Material material = Material.valueOf(blockName.toUpperCase());
            block.setType(material);
        } catch (IllegalArgumentException e) {
            // If block name is invalid, use stone as default
            block.setType(Material.STONE);
        }
    }

    private static void fillArea(Location baseLocation, int x1, int y1, int z1, int x2, int y2, int z2, String blockName) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        try {
            Material material = Material.valueOf(blockName.toUpperCase());
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Block block = baseLocation.getWorld().getBlockAt(x, y, z);
                        block.setType(material);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            // If block name is invalid, use stone as default
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Block block = baseLocation.getWorld().getBlockAt(x, y, z);
                        block.setType(Material.STONE);
                    }
                }
            }
        }
    }
}
