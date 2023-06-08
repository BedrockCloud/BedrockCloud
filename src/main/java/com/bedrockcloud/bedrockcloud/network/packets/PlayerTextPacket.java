package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class PlayerTextPacket extends DataPacket
{
    public String playerName;
    public int type;
    public String value;
    public final int TYPE_MESSAGE = 0;
    public final int TYPE_TITLE = 1;
    public final int TYPE_POPUP = 2;
    public final int TYPE_TIP = 3;
    public final int TYPE_ACTIONBAR = 4;
    
    @Override
    public String getPacketName() {
        return "PlayerTextPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final int type = (Integer) jsonObject.get("type");
        final String value = jsonObject.get("value").toString();
        if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            final CloudPlayer cloudPlayer = BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName);
            final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
            playerTextPacket.playerName = cloudPlayer.getPlayerName();
            playerTextPacket.type = type;
            playerTextPacket.value = value;
            cloudPlayer.getProxy().pushPacket(playerTextPacket);
        }
        super.handle(jsonObject, clientRequest);
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        this.addValue("type", this.type);
        this.addValue("value", this.value);
        return super.encode();
    }
}
