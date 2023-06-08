package com.bedrockcloud.bedrockcloud.server.privategameserver;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.manager.FileManager;
import com.bedrockcloud.bedrockcloud.manager.PushPacketManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.packets.GameServerDisconnectPacket;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerTextPacket;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.properties.ServerProperties;
import com.bedrockcloud.bedrockcloud.server.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.server.serviceKiller.ServiceKiller;
import com.bedrockcloud.bedrockcloud.tasks.PrivateKeepALiveTask;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;

import java.io.*;
import java.net.*;
import java.util.Objects;

public class PrivateGameServer
{
    private final Template template;
    private final String serverName;
    private final int serverPort;
    public int pid;
    public int state;
    private int playerCount;
    private int aliveChecks;
    private DatagramSocket socket;
    public final String temp_path = "./templates/";
    public final String servers_path = "./temp/";
    public String serverOwner = null;
    public PrivateKeepALiveTask task = null;

    public PrivateGameServer(final Template template, String serverOwner) {
        this.template = template;
        this.aliveChecks = 0;
        this.serverName = template.getName() + "-" + FileManager.getFreeNumber("./temp/" + template.getName());
        this.serverPort = PortValidator.getNextPrivateServerPort(this);
        this.playerCount = 0;
        this.state = 0;
        this.pid = -1;

        ServiceKiller.killPid(this);

        BedrockCloud.getPrivateGameServerProvider().addGameServer(this);
        this.copyServer();
        this.serverOwner = serverOwner;
        try {
            this.startServer();
        } catch (InterruptedException e) {
            BedrockCloud.getLogger().exception(e);
        }
    }

    public PrivateKeepALiveTask getTask() {
        return task;
    }

    public int getPid() {
        return this.pid;
    }
    
    public String getServerName() {
        return this.serverName;
    }

    public String getServerOwner(){
        return this.serverOwner;
    }
    
    public int getServerPort() {
        return this.serverPort;
    }
    
    public int getAliveChecks() {
        return this.aliveChecks;
    }
    
    public void setAliveChecks(final int aliveChecks) {
        this.aliveChecks = aliveChecks;
    }
    
    public void startServer() throws InterruptedException {
        final File server = new File("./temp/" + this.serverName);
        if (server.exists()) {
            final ProcessBuilder builder = new ProcessBuilder(new String[0]);

            String notifyMessage = MessageAPI.startMessage.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);
            try {
                builder.command("/bin/sh", "-c", "screen -X -S " + this.serverName + " kill").start();
            } catch (Exception e) {
                BedrockCloud.getLogger().exception(e);
            }

            try {
                builder.command("/bin/sh", "-c", "screen -dmS " + this.serverName + " ../../bin/php7/bin/php PocketMine-MP.phar").directory(new File("./temp/" + this.serverName)).start();
            } catch (Exception e) {
                BedrockCloud.getLogger().exception(e);
            }
        } else {
            String notifyMessage = MessageAPI.startFailed.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            if (BedrockCloud.getCloudPlayerProvider().existsPlayer(getServerOwner())) {
                final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
                playerTextPacket.playerName = getServerOwner();
                Objects.requireNonNull(playerTextPacket);
                playerTextPacket.type = 0;
                playerTextPacket.value = BedrockCloud.prefix + "§cYour server can't be started, please try again.";
                BedrockCloud.getCloudPlayerProvider().getCloudPlayer(getServerName()).getProxy().pushPacket(playerTextPacket);
            }
        }
    }
    
    public void copyServer() {
        final File src = new File("./templates/" + this.template.getName() + "/");
        final File dest = new File("./temp/" + this.serverName);
        FileManager.copy(src, dest);
        final File global_plugins = new File("./local/plugins/pocketmine");
        final File dest_plugs = new File("./temp/" + this.serverName + "/plugins/");
        FileManager.copy(global_plugins, dest_plugs);
        final File file = new File("./local/versions/pocketmine");
        final File dest_lib = new File("./temp/" + this.serverName + "/");
        FileManager.copy(file, dest_lib);

        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServerProperties.createProperties(this);
    }
    
    public Template getTemplate() {
        return this.template;
    }
    
    public void stopServer() {
        String notifyMessage = MessageAPI.stopMessage.replace("%service", this.serverName);
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        final GameServerDisconnectPacket packet = new GameServerDisconnectPacket();
        packet.addValue("reason", "Server Stopped");
        this.pushPacket(packet);
        try {
            ServiceHelper.killWithPID(this);
        } catch (IOException ignored) {
        }
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    public void setSocket(final DatagramSocket socket) {
        this.socket = socket;
    }

    public void pushPacket(final DataPacket cloudPacket) {
        PushPacketManager.pushPacket(cloudPacket, this);
    }
    
    public int getPlayerCount() {
        return this.playerCount;
    }
    
    public void setPlayerCount(final int v) {
        this.playerCount = v;
    }
    
    public int getState() {
        return this.state;
    }
    
    @Override
    public String toString() {
        return "PrivateGameServer{template=" + this.template + ", serverName='" + this.serverName + '\'' + ", serverPort=" + this.serverPort + ", playerCount=" + this.playerCount + ", aliveChecks=" + this.aliveChecks + ", socket=" + this.socket + ", temp_path='" + "./templates/" + '\'' + ", servers_path='" + "./temp/" + '\'' + '}';
    }
}
