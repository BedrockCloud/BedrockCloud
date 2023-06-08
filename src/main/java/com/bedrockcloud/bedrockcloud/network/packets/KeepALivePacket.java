package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class KeepALivePacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "KeepALivePacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        if (BedrockCloud.getGameServerProvider().existServer(serverName)) {
            final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
            gameServer.setAliveChecks(0);
        }
    }
}
