package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import java.util.Objects;
import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class PlayerMovePacket extends DataPacket
{
    public String playerName;
    public String toServer;
    
    @Override
    public String getPacketName() {
        return "PlayerMovePacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String server = jsonObject.get("toServer").toString();
        if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            final CloudPlayer cloudPlayer = BedrockCloud.cloudPlayerProvider.getCloudPlayer(playerName);
            if (BedrockCloud.getProxyServerProvider().existServer(cloudPlayer.getCurrentProxy())) {
                final GameServer gameServer = BedrockCloud.gameServerProvider.getGameServer(server);
                if (gameServer != null) {
                    if (gameServer.getPlayerCount() < gameServer.getTemplate().getMaxPlayers()) {
                        if (gameServer.getState() == 1) {
                            final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
                            playerTextPacket.playerName = playerName;
                            Objects.requireNonNull(playerTextPacket);
                            playerTextPacket.type = 0;
                            playerTextPacket.value = "§bCloud §8| §cThe Server is running!";
                            BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerTextPacket);
                        } else {
                            final ProxyServer proxyServer = BedrockCloud.proxyServerProvider.getProxyServer(cloudPlayer.getCurrentProxy());
                            final PlayerMovePacket playerMovePacket = new PlayerMovePacket();
                            playerMovePacket.playerName = playerName;
                            playerMovePacket.toServer = server;
                            proxyServer.pushPacket(playerMovePacket);
                        }
                    } else {
                        final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
                        playerTextPacket.playerName = playerName;
                        Objects.requireNonNull(playerTextPacket);
                        playerTextPacket.type = 0;
                        playerTextPacket.value = "§bCloud §8| §cThe Server is full!";
                        BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerTextPacket);
                    }
                } else {
                    final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
                    playerTextPacket.playerName = playerName;
                    Objects.requireNonNull(playerTextPacket);
                    playerTextPacket.type = 0;
                    playerTextPacket.value = "§bCloud §8| §4The Server is not registered!";
                    BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerTextPacket);
                }
            }
        } else {
            //this.getLogger().error(playerName + " is not connected with the StimoCloud!");
        }
        super.handle(jsonObject, clientRequest);
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        this.addValue("toServer", this.toServer);
        return super.encode();
    }
}
