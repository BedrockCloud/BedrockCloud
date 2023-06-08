package com.bedrockcloud.bedrockcloud.rest.handler.player;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerKickPacket;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerTextPacket;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerKickRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = parseQueryParams(query);

        if (queryParams.size() == 0) {
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "No arguments given!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        if (queryParams.get("player") == null) {
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Missing argument: 'player'");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        String player = queryParams.get("player");
        if (BedrockCloud.getCloudPlayerProvider().existsPlayer(player)) {

            final PlayerKickPacket playerKickPacket = new PlayerKickPacket();
            playerKickPacket.playerName = player;
            Objects.requireNonNull(playerKickPacket);
            playerKickPacket.reason = "§cYou was kicked.";
            BedrockCloud.getCloudPlayerProvider().getCloudPlayer(player).getProxy().pushPacket(playerKickPacket);

            JSONObject responseObj = new JSONObject();
            responseObj.put("success", "Player " + player + " was kicked successfully!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Player " + player + " isn't online!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = idx > 0 ? pair.substring(0, idx) : pair;
                String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }
}
