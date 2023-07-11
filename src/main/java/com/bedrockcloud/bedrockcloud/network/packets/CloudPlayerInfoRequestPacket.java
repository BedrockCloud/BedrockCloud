package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import org.json.simple.JSONObject;

public class CloudPlayerInfoRequestPacket extends DataPacket {
    @Override
    public String getPacketName() {
        return "CloudPlayerInfoRequestPacket";
    }

    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final CloudPlayerInfoResponsePacket cloudPlayerInfoResponsePacket = new CloudPlayerInfoResponsePacket();
        cloudPlayerInfoResponsePacket.type = 1;
        cloudPlayerInfoResponsePacket.requestId = jsonObject.get("requestId").toString();

        GameServer server;
        boolean success;
        if (jsonObject.get("playerName") == null || jsonObject.get("serverInfoName") == null) {
            success = false;
        } else
            success = BedrockCloud.getCloudPlayerProvider().getCloudPlayer(jsonObject.get("playerName").toString()) != null;

        CloudPlayer cloudPlayer;

        cloudPlayerInfoResponsePacket.success = success;
        if (success) {
            if ((cloudPlayer = BedrockCloud.getCloudPlayerProvider().getCloudPlayer(jsonObject.get("playerName").toString())) != null) {
                cloudPlayerInfoResponsePacket.name = cloudPlayer.getPlayerName();
                cloudPlayerInfoResponsePacket.address = cloudPlayer.getAddress();
                cloudPlayerInfoResponsePacket.xuid = cloudPlayer.getXuid();
                cloudPlayerInfoResponsePacket.currentServer = cloudPlayer.getCurrentServer();
                cloudPlayerInfoResponsePacket.currentProxy = cloudPlayer.getCurrentProxy();
            }
        }
        final boolean isPrivate = Boolean.parseBoolean(jsonObject.get("isPrivate").toString());

        if (!isPrivate) {
            BedrockCloud.getGameServerProvider().getGameServer(jsonObject.get("serverInfoName").toString()).pushPacket(cloudPlayerInfoResponsePacket);
        } else {
            BedrockCloud.getPrivateGameServerProvider().getGameServer(jsonObject.get("serverInfoName").toString()).pushPacket(cloudPlayerInfoResponsePacket);
        }
    }
}