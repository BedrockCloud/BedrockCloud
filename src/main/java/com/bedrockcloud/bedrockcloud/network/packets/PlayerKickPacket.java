package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class PlayerKickPacket extends DataPacket
{
    public String playerName;
    public String reason;
    
    @Override
    public String getPacketName() {
        return "PlayerKickPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String reason = jsonObject.get("reason").toString();
        if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            final CloudPlayer cloudPlayer = BedrockCloud.cloudPlayerProvider.getCloudPlayer(playerName);
            if (BedrockCloud.getProxyServerProvider().existServer(cloudPlayer.getCurrentProxy())) {
                final ProxyServer proxyServer = BedrockCloud.proxyServerProvider.getProxyServer(cloudPlayer.getCurrentProxy());
                final PlayerKickPacket playerKickPacket = new PlayerKickPacket();
                playerKickPacket.playerName = playerName;
                playerKickPacket.reason = reason.replace("§", "&");
                proxyServer.pushPacket(playerKickPacket);
            }
        } else {
            //this.getLogger().error(playerName + " is not connected with the StimoCloud!");
        }
        super.handle(jsonObject, clientRequest);
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        this.addValue("reason", this.reason);
        return super.encode();
    }
}
