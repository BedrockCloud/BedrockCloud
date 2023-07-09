package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.config.Config;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.files.json.json;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.tasks.KeepALiveTask;
import com.bedrockcloud.bedrockcloud.templates.Template;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.net.Socket;

import org.json.simple.JSONObject;

public class ProxyServerConnectPacket extends DataPacket implements Loggable
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final String pid = jsonObject.get("proxyPid").toString();

        this.getLogger().info("Proxy '" + serverName + "' is registered!");

        Config config = new Config("./archive/server-pids/" + serverName + ".json", Config.JSON);
        config.set("pid", Integer.parseInt(pid));
        config.save();

        final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(serverName);
        proxyServer.pid = Integer.parseInt(pid);
        final Object socketPort = jsonObject.get("socketPort");
        proxyServer.setSocket(clientRequest.getSocket());
        for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
            final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
            final RegisterServerPacket packet = new RegisterServerPacket();
            for (GameServer server : BedrockCloud.getGameServerProvider().gameServerMap.values()) {
                if (server != null) {
                    packet.addValue("serverPort", server.getServerPort());
                    packet.addValue("serverName", server.getServerName());
                    proxy.pushPacket(packet);
                }
            }
        }
        proxyServer.setConnected(true);
    }
}
