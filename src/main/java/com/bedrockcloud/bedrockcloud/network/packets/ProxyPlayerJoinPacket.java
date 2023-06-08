package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import org.json.simple.JSONObject;

public class ProxyPlayerJoinPacket extends DataPacket
{
    private String playerName;
    
    @Override
    public String getPacketName() {
        return "ProxyPlayerJoinPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playername = jsonObject.get("playerName").toString();
        final String serverName = jsonObject.get("joinedServer").toString();
        BedrockCloud.getCloudPlayerProvider().addCloudPlayer(new CloudPlayer(jsonObject.get("playerName").toString().toLowerCase(), jsonObject.get("address").toString(), jsonObject.get("uuid").toString(), jsonObject.get("xuid").toString(), jsonObject.get("currentServer").toString(), jsonObject.get("currentProxy").toString()));
        if (BedrockCloud.getGameServerProvider().existServer(serverName)) {
            final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
            final ProxyPlayerJoinPacket packet = new ProxyPlayerJoinPacket();

            gameServer.getTemplate().addPlayer(BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName), serverName);

            packet.playerName = playername;
            gameServer.pushPacket(packet);
        }
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        return super.encode();
    }
}
