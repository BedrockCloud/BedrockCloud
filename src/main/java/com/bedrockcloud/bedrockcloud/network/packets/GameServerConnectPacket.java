package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.config.Config;
import com.bedrockcloud.bedrockcloud.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.tasks.KeepALiveTask;
import com.bedrockcloud.bedrockcloud.tasks.PrivateKeepALiveTask;
import org.json.simple.JSONObject;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameServerConnectPacket extends DataPacket
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        final String serverName = jsonObject.get("serverName").toString();
        final String serverPort = jsonObject.get("serverPort").toString();
        final String serverPid = jsonObject.get("serverPid").toString();
        final boolean isPrivate = Boolean.parseBoolean(jsonObject.get("isPrivate").toString());
        if (!isPrivate) {
            final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
            gameServer.setSocket(clientRequest.getSocket());
            gameServer.pid = Integer.parseInt(serverPid);

            Config config = new Config("./archive/server-pids/" + serverName + ".json", Config.JSON);
            config.set("pid", Integer.parseInt(serverPid));
            config.save();

            gameServer.setAliveChecks(0);

            gameServer.task = new KeepALiveTask(gameServer);
            gameServer.task.setName(gameServer.getServerName());
            service.scheduleAtFixedRate(gameServer.task, 0, 1, TimeUnit.SECONDS);

            final VersionInfoPacket versionInfoPacket = new VersionInfoPacket();
            gameServer.pushPacket(versionInfoPacket);
            for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
                final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
                final RegisterServerPacket packet = new RegisterServerPacket();
                packet.addValue("serverPort", serverPort);
                packet.addValue("serverName", serverName);
                proxy.pushPacket(packet);
            }

            String notifyMessage = MessageAPI.startedMessage.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            gameServer.getTemplate().addServer(gameServer.getTemplate(), serverName);
            gameServer.setConnected(true);
        } else {
            final PrivateGameServer gameServer = BedrockCloud.getPrivateGameServerProvider().getGameServer(serverName);
            gameServer.setSocket(clientRequest.getSocket());
            gameServer.pid = Integer.parseInt(serverPid);

            Config config = new Config("./archive/server-pids/" + serverName + ".json", Config.JSON);
            config.set("pid", Integer.parseInt(serverPid));
            config.save();

            gameServer.setAliveChecks(0);

            gameServer.task = new PrivateKeepALiveTask(gameServer);
            gameServer.task.setName(gameServer.getServerName());
            service.scheduleAtFixedRate(gameServer.task, 0, 1, TimeUnit.SECONDS);

            final VersionInfoPacket versionInfoPacket = new VersionInfoPacket();
            gameServer.pushPacket(versionInfoPacket);
            for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
                final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
                final RegisterServerPacket packet = new RegisterServerPacket();
                packet.addValue("serverPort", serverPort);
                packet.addValue("serverName", serverName);
                proxy.pushPacket(packet);
            }

            String notifyMessage = MessageAPI.startedMessage.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            gameServer.getTemplate().addServer(gameServer.getTemplate(), serverName);
            gameServer.setConnected(true);

            if (BedrockCloud.getCloudPlayerProvider().existsPlayer(gameServer.getServerOwner())) {
                BedrockCloud.getCloudPlayerProvider().getCloudPlayer(gameServer.getServerOwner()).setHasPrivateServer(true);

                final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
                playerTextPacket.playerName = gameServer.getServerOwner();
                Objects.requireNonNull(playerTextPacket);
                playerTextPacket.type = 0;
                playerTextPacket.value = BedrockCloud.prefix + "§aYou will be transferred in §e3 seconds§7.";
                BedrockCloud.getCloudPlayerProvider().getCloudPlayer(getServerName()).getProxy().pushPacket(playerTextPacket);
            }

            Timer timer = new Timer(true);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    PlayerMovePacket pk = new PlayerMovePacket();
                    pk.playerName = gameServer.getServerOwner();
                    pk.toServer = pk.getServerName();
                    for (final ProxyServer proxy : BedrockCloud.getProxyServerProvider().getProxyServerMap().values()) {
                        proxy.pushPacket(pk);
                    }
                    this.cancel();
                }
            };
            timer.schedule(task, 3*1000);
        }
    }
}