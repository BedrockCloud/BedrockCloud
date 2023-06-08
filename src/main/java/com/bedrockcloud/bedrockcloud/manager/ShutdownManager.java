package com.bedrockcloud.bedrockcloud.manager;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.server.serviceHelper.ServiceHelper;

import java.io.IOException;

public class ShutdownManager {

    public static void shutdown(){
        try {
            if (BedrockCloud.networkManager.datagramSocket != null && !BedrockCloud.networkManager.datagramSocket.isClosed()) {
                BedrockCloud.networkManager.datagramSocket.close();
                BedrockCloud.getLogger().warning("CloudSocket was closed.");
            }
        } catch (Exception ignored){}

        for (final String templateName : BedrockCloud.getTemplateProvider().templateMap.keySet()) {
            BedrockCloud.getTemplateProvider().removeRunningGroup(templateName);
        }

        for (final GameServer gameServer : BedrockCloud.getGameServerProvider().gameServerMap.values()) {
            gameServer.stopServer();

            gameServer.getSocket().disconnect();
            gameServer.getSocket().close();
        }

        for (final PrivateGameServer gameServer : BedrockCloud.getPrivateGameServerProvider().gameServerMap.values()) {
            gameServer.stopServer();

            gameServer.getSocket().disconnect();
            gameServer.getSocket().close();
        }

        for (final String proxy : BedrockCloud.getProxyServerProvider().proxyServerMap.keySet()) {
            final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(proxy);
            proxyServer.stopServer();

            proxyServer.getSocket().disconnect();
            proxyServer.getSocket().close();
        }
        try {
            BedrockCloud.getLogger().info("§cPlease wait until the cloud has been stopped.");
            Thread.sleep(3000L);
        } catch (Exception ignored){}
    }
}
