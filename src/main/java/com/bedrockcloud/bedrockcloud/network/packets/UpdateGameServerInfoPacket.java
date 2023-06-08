package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import org.json.simple.JSONObject;

public class UpdateGameServerInfoPacket extends DataPacket
{
    public final int TYPE_UPDATE_PLAYER_COUNT = 0;
    public final int TYPE_UPDATE_STATE_MODE = 1;
    
    @Override
    public String getPacketName() {
        return "UpdateGameServerInfoPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final GameServer serverName = BedrockCloud.getGameServerProvider().getGameServer(jsonObject.get("serverName").toString());
        final int type = Integer.parseInt(String.valueOf(jsonObject.get("type")));
        final String value = jsonObject.get("value").toString();
        if (type == 0) {
            serverName.setPlayerCount(Integer.parseInt(value));
        }
        else if (type == 1) {
            serverName.state = Integer.parseInt(value);
        }
        super.handle(jsonObject, clientRequest);
    }
}