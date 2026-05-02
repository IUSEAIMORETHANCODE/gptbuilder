# GPT Builder Plugin - Minecraft Building Assistant

A Spigot/Bukkit plugin that allows players to request building structures from an AI assistant powered by OpenRouter.

## Features

- 🤖 AI-powered building suggestions using OpenRouter API
- ⚡ Easy-to-use `/gpt <prompt>` command
- 🎨 Automatic building execution with block placement
- ⚙️ Fully configurable via config.yml
- 🔒 Permission-based access control

## Installation

1. **Prerequisites**
   - Java 16 or higher
   - Maven 3.6+
   - Spigot/Bukkit server (1.20.1 or compatible)

2. **Build the plugin**
   ```bash
   mvn clean package
   ```
   The compiled JAR will be in `target/GPTBuilderPlugin-1.0-SNAPSHOT.jar`

3. **Install the plugin**
   - Copy the JAR file to your server's `plugins/` folder
   - Restart your server

4. **Configure the plugin**
   - A `plugins/GPTBuilder/config.yml` file will be created on first run
   - Edit it and add your OpenRouter API key:
     ```yaml
     openrouter:
       api-key: "your-openrouter-api-key-here"
     ```

## Configuration

### config.yml

```yaml
openrouter:
  api-key: "your-openrouter-api-key-here"  # Your OpenRouter API key
  model: "openrouter/auto"                   # Model to use
  base-url: "https://openrouter.io/api/v1"  # OpenRouter API endpoint

building:
  timeout-seconds: 60                 # Timeout for API requests
  max-blocks-per-build: 5000          # Maximum blocks per build
  animation-enabled: true             # Enable build animation
  animation-speed: 20                 # Animation speed (ticks)

messages:
  building-started: "&6[GPT] Building started... This may take a moment."
  building-complete: "&a[GPT] Building complete!"
  building-error: "&c[GPT] Error during building: {error}"
  invalid-prompt: "&c[GPT] Please provide a prompt. Usage: /gpt <prompt>"
  no-permission: "&c[GPT] You don't have permission to use this command."
```

## Getting an OpenRouter API Key

1. Visit [OpenRouter.ai](https://openrouter.ai/)
2. Sign up for an account
3. Go to your API keys section
4. Create a new API key
5. Add it to the plugin's config.yml

## Usage

### In-Game Commands

```
/gpt <prompt>     - Request a building from GPT
/ai <prompt>      - Alias for /gpt
/build <prompt>   - Alias for /gpt
```

### Examples

```
/gpt make a highly detailed volcano
/gpt build a small cozy house
/gpt create a tall wizard tower
/gpt make a large castle with walls
/gpt build a jellyfish
```

## Permissions

- `gptbuilder.use` - Allows player to use the /gpt command (default: true)
- `gptbuilder.admin` - Admin access (default: OP)

## How It Works

1. Player types `/gpt <prompt>` in chat
2. Plugin sends the prompt to OpenRouter API with a system prompt configured for Minecraft building
3. AI responds with Minecraft commands (setblock, fill, etc.)
4. Plugin parses and executes the commands relative to the player's location
5. Structure is built in the world

## Technical Details

### Architecture

- **GPTBuilderPlugin**: Main plugin class
- **GPTCommand**: Command handler for /gpt
- **OpenRouterAPI**: API communication with OpenRouter
- **BuildingExecutor**: Parses and executes building commands

### Supported Commands

The plugin can parse and execute the following Minecraft commands from AI responses:
- `/setblock <x> <y> <z> <block>` - Place a single block
- `/fill <x1> <y1> <z1> <x2> <y2> <z2> <block>` - Fill an area with blocks

All coordinates are relative to the player's position (0 0 0 = player location).

## Troubleshooting

### "OpenRouter API key not configured"
- Make sure you've added your API key to config.yml
- Restart the server after editing config

### "Build exceeded maximum block limit"
- Increase `max-blocks-per-build` in config.yml
- Or ask the AI for a smaller structure

### API Errors
- Check your OpenRouter API key is valid
- Ensure you have credits available on OpenRouter
- Check the server logs for detailed error messages

## Development

### Building from Source

```bash
git clone <repository>
cd GPTBuilder
mvn clean package
```

### Project Structure

```
src/main/
├── java/com/gptbuilder/plugin/
│   ├── GPTBuilderPlugin.java      # Main plugin class
│   ├── commands/
│   │   └── GPTCommand.java        # /gpt command handler
│   ├── api/
│   │   └── OpenRouterAPI.java     # OpenRouter API integration
│   └── building/
│       └── BuildingExecutor.java  # Building execution logic
└── resources/
    ├── plugin.yml                 # Plugin manifest
    └── config.yml                 # Default configuration
```

## License

This plugin is provided as-is for use with Minecraft servers.

## Support

For issues or feature requests, please refer to the documentation or check the server logs for error messages.

## Credits

- Built for Spigot/Bukkit servers
- Powered by OpenRouter API
- Uses GSON for JSON parsing
