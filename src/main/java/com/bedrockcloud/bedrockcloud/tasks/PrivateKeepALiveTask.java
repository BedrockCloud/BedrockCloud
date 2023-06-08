package com.bedrockcloud.bedrockcloud.tasks;

import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.server.query.api.Protocol;
import com.bedrockcloud.bedrockcloud.server.query.api.QueryException;
import com.bedrockcloud.bedrockcloud.server.query.api.QueryStatus;
import com.bedrockcloud.bedrockcloud.templates.Template;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import com.bedrockcloud.bedrockcloud.network.packets.KeepALivePacket;
import com.bedrockcloud.bedrockcloud.BedrockCloud;

/*
 * KeepALiveTask to check the status of a service
 */
public class PrivateKeepALiveTask extends Thread implements Runnable {

    PrivateGameServer gameServer = null;

    public PrivateKeepALiveTask(PrivateGameServer gameServer){
        this.gameServer = gameServer;
    }

    @Override
    public void run() {
        try {
            while (BedrockCloud.isRunning()) {
                final String servername = this.gameServer.getServerName();
                if (servername == null) {
                    this.interrupt();
                    return;
                }
                final PrivateGameServer gameServer = BedrockCloud.getPrivateGameServerProvider().getGameServer(servername);
                if (gameServer == null) {
                    this.interrupt();
                    return;
                }

                try {
                    try {
                        new QueryStatus.Builder("127.0.0.1")
                                .setProtocol(Protocol.UDP_FULL)
                                .setPort(gameServer.getServerPort())
                                .build()
                                .getStatus()
                                .toJson();
                    } catch (IllegalArgumentException ignored) {
                    }
                } catch (QueryException e) {
                    final Template group = gameServer.getTemplate();
                    if (gameServer.getAliveChecks() == 0) {
                        final KeepALivePacket packet = new KeepALivePacket();
                        gameServer.pushPacket(packet);
                    } else {
                        if (BedrockCloud.getGameServerProvider().existServer(servername)) {
                            if (gameServer.getAliveChecks() >= 10) {
                                gameServer.setAliveChecks(0);
                                String notifyMessage = MessageAPI.timedOut.replace("%service", servername);
                                BedrockCloud.getLogger().warning(notifyMessage);
                                CloudNotifyManager.sendNotifyCloud(notifyMessage);

                                try {
                                    ServiceHelper.killWithPID(gameServer);
                                    this.interrupt();
                                } catch (IOException ignored) {
                                }
                            }
                        }
                    }
                    if (gameServer.getAliveChecks() < 10) {
                        gameServer.setAliveChecks(gameServer.getAliveChecks() + 1);
                    }
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }
    }
}
