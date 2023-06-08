package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.server.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.templates.Template;

import java.io.IOException;

import org.json.simple.JSONObject;

public class ProxyServerDisconnectPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "ProxyServerDisconnectPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(serverName);
        if (proxyServer != null) {
            final Template template = proxyServer.getTemplate();
            try {
                ServiceHelper.killWithPID(proxyServer);
            } catch (IOException ignored) {
            }
        }
    }
}
