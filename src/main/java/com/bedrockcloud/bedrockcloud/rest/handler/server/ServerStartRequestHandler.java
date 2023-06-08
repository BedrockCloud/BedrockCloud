package com.bedrockcloud.bedrockcloud.rest.handler.server;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ServerStartRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = parseQueryParams(query);

        if (queryParams.size() == 0){
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "No arguments given!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        if (queryParams.get("template") == null){
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Missing argument: 'template'");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        if (queryParams.get("count") == null){
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Missing argument: 'count'");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        if (!Utils.isNumeric(queryParams.get("count"))){
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Argument 'count' must be a number!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        String template = queryParams.get("template");
        int count = Integer.parseInt(queryParams.get("count"));

        if (BedrockCloud.getTemplateProvider().existsTemplate(template)){
            if (!BedrockCloud.getTemplateProvider().isTemplateRunning(BedrockCloud.getTemplateProvider().getTemplate(template))){
                JSONObject responseObj = new JSONObject();
                responseObj.put("error", "The Template " + template + " isn't running!");

                String response = responseObj.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                if (count <= 0){
                    JSONObject responseObj = new JSONObject();
                    responseObj.put("error", "Argument 'count' must be higher then '0'!");

                    String response = responseObj.toString();
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    final Template group = BedrockCloud.getTemplateProvider().getTemplate(template);
                    for (int i = 0; i < count; ++i) {
                        if (group.getType() == GroupAPI.POCKETMINE_SERVER) {
                            new GameServer(group);
                        } else {
                            if (group.getType() == GroupAPI.PROXY_SERVER) {
                                new ProxyServer(group);
                            }
                        }
                    }
                    JSONObject responseObj = new JSONObject();
                    responseObj.put("success", count + " servers from the template " + template + " were started successfully!");

                    String response = responseObj.toString();
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        } else {
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Template " + template + " don't exists!");

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
