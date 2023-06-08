package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class ListServerRequestPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "ListServerRequestPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final ListServerResponsePacket listServerResponsePacket = new ListServerResponsePacket();
        listServerResponsePacket.type = 1;
        listServerResponsePacket.requestId = jsonObject.get("requestId").toString();
        final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(jsonObject.get("serverName").toString());
        gameServer.pushPacket(listServerResponsePacket);
    }
}
