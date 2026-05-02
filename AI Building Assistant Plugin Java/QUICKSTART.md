# Quick Start Guide

## Step 1: Get Your OpenRouter API Key

1. Go to https://openrouter.ai
2. Sign up for an account
3. Navigate to API Keys section
4. Generate a new API key
5. Copy the key

## Step 2: Build the Plugin

Run this command in the plugin directory:

```bash
mvn clean package
```

The compiled JAR will be at: `target/GPTBuilderPlugin-1.0-SNAPSHOT.jar`

## Step 3: Install on Your Server

1. Copy `GPTBuilderPlugin-1.0-SNAPSHOT.jar` to your server's `plugins/` folder
2. Restart your server
3. The config file will be auto-generated at `plugins/GPTBuilder/config.yml`

## Step 4: Configure the API Key

Open `plugins/GPTBuilder/config.yml` and replace:

```yaml
openrouter:
  api-key: "your-openrouter-api-key-here"
```

with your actual API key:

```yaml
openrouter:
  api-key: "sk-or-v1-xxxxxxxxxxxxxxxxxxxx"
```

## Step 5: Restart Server

Restart your Spigot/Bukkit server.

## Step 6: Start Building!

In Minecraft, type:

```
/gpt make a highly detailed volcano
```

The AI will generate commands to build the structure in your world!

## Examples

Try these prompts:

- `/gpt build a small cozy house`
- `/gpt create a tall wizard tower with spiral stairs`
- `/gpt make a jellyfish`
- `/gpt build a large castle with walls and towers`
- `/gpt make a detailed tree`
- `/gpt create a futuristic building`
- `/gpt build a small farm with crops`

## Tips

1. **Be specific**: The more details in your prompt, the better the result
2. **Start small**: Begin with small structures to test the plugin
3. **Check logs**: Server logs show detailed information about builds
4. **Permissions**: By default, all players have access. Use `/permissions` to restrict if needed
5. **Block limit**: Default is 5000 blocks per build. Increase in config if needed

## API Selection

The plugin uses `openrouter/auto` by default, which automatically selects the best available model.

You can change to specific models:
- `openrouter/auto` - Automatic (default, recommended)
- `gpt-4` - OpenAI's GPT-4
- `gpt-3.5-turbo` - OpenAI's GPT-3.5
- Or any other model available on OpenRouter

Just edit the config:

```yaml
openrouter:
  model: "gpt-4"
```

## Troubleshooting

### Plugin doesn't load
- Check that your Java version is 16+
- Check server logs for errors
- Verify the JAR is in the plugins folder

### Command doesn't work
- Restart the server (config changes require restart)
- Check player has permission: `gptbuilder.use`
- Make sure you typed the command correctly: `/gpt <prompt>`

### API errors
- Verify your API key is correct in config.yml
- Check you have credits on OpenRouter
- Ensure internet connection is working
- Check server logs for detailed error messages

## Support

Check the full README.md for more information and advanced configuration options.
