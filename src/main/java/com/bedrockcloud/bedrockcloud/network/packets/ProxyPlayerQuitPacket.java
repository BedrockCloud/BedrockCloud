package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.server.serviceKiller.ServiceKiller;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class ProxyPlayerQuitPacket extends DataPacket
{
    private String playerName;
    
    @Override
    public String getPacketName() {
        return "ProxyPlayerQuitPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playername = jsonObject.get("playerName").toString();
        final String serverName = jsonObject.get("leftServer").toString();

        if (BedrockCloud.getGameServerProvider().existServer(serverName)) {
            final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
            final ProxyPlayerQuitPacket packet = new ProxyPlayerQuitPacket();
            packet.playerName = playername;
            gameServer.pushPacket(packet);
        } else if (BedrockCloud.getPrivateGameServerProvider().existServer(serverName)){
            final PrivateGameServer gameServer = BedrockCloud.getPrivateGameServerProvider().getGameServer(serverName);
            final ProxyPlayerQuitPacket packet = new ProxyPlayerQuitPacket();
            packet.playerName = playername;
            gameServer.pushPacket(packet);
        }

        final CloudPlayer cloudPlayer = BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playername);
        if (cloudPlayer.isHasPrivateServer()){
            for (PrivateGameServer server : BedrockCloud.getPrivateGameServerProvider().getGameServerMap().values()) {
                if (server.getServerOwner().equals(cloudPlayer.getPlayerName())){
                    ServiceKiller.killPid(server);
                }
            }
        }

        final GameServer gS = BedrockCloud.getGameServerProvider().getGameServer(serverName);
        final GameServer pgS = BedrockCloud.getGameServerProvider().getGameServer(serverName);
        final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(serverName);
        gS.getTemplate().removePlayer(cloudPlayer);
        pgS.getTemplate().removePlayer(cloudPlayer);
        proxyServer.getTemplate().removePlayer(cloudPlayer);

        BedrockCloud.getCloudPlayerProvider().removeCloudPlayer(cloudPlayer);
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        return super.encode();
    }
}
