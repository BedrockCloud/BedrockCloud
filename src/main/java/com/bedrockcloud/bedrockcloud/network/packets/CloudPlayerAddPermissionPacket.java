package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class CloudPlayerAddPermissionPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "CloudPlayerAddPermissionPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String permission = jsonObject.get("permission").toString();
        for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
            final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
            final CloudPlayerAddPermissionPacket packet = new CloudPlayerAddPermissionPacket();
            packet.addValue("playerName", playerName);
            packet.addValue("permission", permission);
            proxy.pushPacket(packet);
        }
    }
}
