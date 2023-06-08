package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import org.json.simple.JSONObject;

public class SendToHubPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "SendToHubPacket";
    }

    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
            final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
            final SendToHubPacket packet = new SendToHubPacket();
            packet.addValue("playerName", playerName);
            proxy.pushPacket(packet);
        }
    }
}
