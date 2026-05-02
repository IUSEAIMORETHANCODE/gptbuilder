package com.gptbuilder.plugin.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gptbuilder.plugin.GPTBuilderPlugin;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.ClassicHttpResponse;

public class OpenRouterAPI {
    
    private static final Gson gson = new Gson();
    private static final String SYSTEM_PROMPT = """
        You are a Minecraft building assistant. When given a building request, respond with exact Minecraft block placement commands.
        
        Format your response as a series of commands like:
        /setblock <x> <y> <z> <block>
        /fill <x1> <y1> <z1> <x2> <y2> <z2> <block>
        
        Always provide relative coordinates starting from 0 0 0. Be creative and build realistic structures.
        The player's location will be used as the base coordinate (0 0 0).
        """;

    public static String sendBuildRequest(String prompt) throws Exception {
        GPTBuilderPlugin plugin = GPTBuilderPlugin.getInstance();
        String apiKey = plugin.getApiKey();
        String model = plugin.getModel();
        String baseUrl = plugin.getBaseUrl();

        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-openrouter-api-key-here")) {
            throw new Exception("OpenRouter API key not configured!");
        }

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(baseUrl + "/chat/completions");

        // Set headers
        httpPost.setHeader("Authorization", "Bearer " + apiKey);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("HTTP-Referer", "https://github.com/gptbuilder/minecraft");
        httpPost.setHeader("X-Title", "GPT Builder");

        // Build request body
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model);
        requestBody.addProperty("temperature", 0.7);
        requestBody.addProperty("max_tokens", 2000);

        JsonArray messages = new JsonArray();
        
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", SYSTEM_PROMPT);
        messages.add(systemMessage);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        messages.add(userMessage);

        requestBody.add("messages", messages);

        httpPost.setEntity(new StringEntity(gson.toJson(requestBody)));

        try (ClassicHttpResponse response = (ClassicHttpResponse) httpClient.executeOpen(null, httpPost, null)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            
            if (response.getCode() != 200) {
                throw new Exception("OpenRouter API error: " + response.getCode() + " - " + responseBody);
            }

            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            
            if (jsonResponse.has("choices") && jsonResponse.getAsJsonArray("choices").size() > 0) {
                JsonObject choice = jsonResponse.getAsJsonArray("choices").get(0).getAsJsonObject();
                String content = choice.getAsJsonObject("message").get("content").getAsString();
                return content;
            } else {
                throw new Exception("Invalid response from OpenRouter API");
            }
        }
    }
}
