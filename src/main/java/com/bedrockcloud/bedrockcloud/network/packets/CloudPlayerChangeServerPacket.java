package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import org.json.simple.JSONObject;

public class CloudPlayerChangeServerPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "CloudPlayerChangeServerPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String server = jsonObject.get("server").toString();
        if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(server);
            BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).setCurrentServer(server);
            gameServer.getTemplate().removePlayer(BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName));
            gameServer.getTemplate().addPlayer(BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName), server);
        }
        super.handle(jsonObject, clientRequest);
    }
}
