package com.bedrockcloud.bedrockcloud.rest.handler.player;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class PlayerInfoRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (BedrockCloud.getCloudPlayerProvider().getCloudPlayerMap().size() == 0) {
            String response = "No players online!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

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

        JSONObject playerObj = new JSONObject();
        if (BedrockCloud.getCloudPlayerProvider().existsPlayer(queryParams.get("player"))) {
            playerObj.put("name", queryParams.get("player"));
            playerObj.put("currentServer", BedrockCloud.getCloudPlayerProvider().getCloudPlayer(queryParams.get("player")).getCurrentServer());
            playerObj.put("xuid", BedrockCloud.getCloudPlayerProvider().getCloudPlayer(queryParams.get("player")).getXuid());
            playerObj.put("ip", BedrockCloud.getCloudPlayerProvider().getCloudPlayer(queryParams.get("player")).getAddress());
            playerObj.put("currentProxy", BedrockCloud.getCloudPlayerProvider().getCloudPlayer(queryParams.get("player")).getCurrentProxy());
            //playerObj.put("hasPrivateServer", BedrockCloud.getCloudPlayerProvider().getCloudPlayer(queryParams.get("player")).isHasPrivateServer());
        } else {
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Player " + queryParams.get("player") + " don't exists!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        String response = playerObj.toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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
