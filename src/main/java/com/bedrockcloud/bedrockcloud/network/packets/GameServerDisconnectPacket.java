package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.manager.FileManager;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.server.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.templates.Template;

import java.io.File;
import java.io.IOException;

import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class GameServerDisconnectPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "GameServerDisconnectPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final boolean isPrivate = Boolean.parseBoolean(jsonObject.get("isPrivate").toString());

        if (!isPrivate) {
            final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
            gameServer.setAliveChecks(0);
            final Template template = gameServer.getTemplate();
            for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
                final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
                final UnregisterServerPacket packet = new UnregisterServerPacket();
                packet.addValue("serverName", serverName);
                proxy.pushPacket(packet);
            }

            try {
                FileManager.deleteServer(new File("./temp/" + serverName), serverName, gameServer.getTemplate().getStatic());
            } catch (NullPointerException ex) {
                BedrockCloud.getLogger().exception(ex);
            }

            String notifyMessage = MessageAPI.stoppedMessage.replace("%service", gameServer.getServerName());
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);
        } else {
            final PrivateGameServer gameServer = BedrockCloud.getPrivateGameServerProvider().getGameServer(serverName);
            gameServer.setAliveChecks(0);
            final Template template = gameServer.getTemplate();
            for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
                final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
                final UnregisterServerPacket packet = new UnregisterServerPacket();
                packet.addValue("serverName", serverName);
                proxy.pushPacket(packet);
            }

            try {
                FileManager.deleteServer(new File("./temp/" + serverName), serverName, gameServer.getTemplate().getStatic());
            } catch (NullPointerException ex) {
                BedrockCloud.getLogger().exception(ex);
            }

            String notifyMessage = MessageAPI.stoppedMessage.replace("%service", gameServer.getServerName());
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);
        }
    }
}
