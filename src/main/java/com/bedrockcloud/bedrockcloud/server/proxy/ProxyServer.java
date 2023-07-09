package com.bedrockcloud.bedrockcloud.server.proxy;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.config.Config;
import com.bedrockcloud.bedrockcloud.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.manager.FileManager;
import com.bedrockcloud.bedrockcloud.manager.PushPacketManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.properties.ServerProperties;
import com.bedrockcloud.bedrockcloud.server.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.server.serviceKiller.ServiceKiller;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;

import java.io.*;

import com.bedrockcloud.bedrockcloud.network.packets.ProxyServerDisconnectPacket;

import java.net.*;
import java.time.Duration;
import java.time.Instant;

public class ProxyServer
{
    private final Template template;
    private final String serverName;
    private int serverPort;
    private DatagramSocket socket;
    public final String temp_path = "./templates/";
    public final String servers_path = "./temp/";
    public int pid;
    public int socketPort;
    private boolean isConnected = false;

    public ProxyServer(final Template template) {
        this.template = template;
        this.serverName = template.getName() + "-" + FileManager.getFreeNumber("./temp/" + template.getName());
        this.serverPort = PortValidator.getNextProxyServerPort(this);
        this.pid = -1;
        this.socketPort = -1;

        ServiceKiller.killPid(this);

        BedrockCloud.getProxyServerProvider().addProxyServer(this);
        this.copyServer();
        try {
            this.startServer();
        }
        catch (InterruptedException e) {
            BedrockCloud.getLogger().exception(e);
        }
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public Template getTemplate() {
        return this.template;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    public void setSocket(final DatagramSocket socket) {
        this.socket = socket;
    }

    public int getServerPort() {
        return this.serverPort;
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
                builder.command("/bin/sh", "-c", "screen -dmS " + this.serverName + " java -jar WaterdogPE.jar").directory(new File("./temp/" + this.serverName)).start();
            } catch (Exception e) {
                BedrockCloud.getLogger().exception(e);
            }

            waitForConnection(10);
        } else {
            String notifyMessage = MessageAPI.startFailed.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);
        }
    }

    private void waitForConnection(int timeoutSeconds) {
        Instant startTime = Instant.now();
        Duration timeoutDuration = Duration.ofSeconds(timeoutSeconds);

        while (Instant.now().isBefore(startTime.plus(timeoutDuration))) {
            if (this.isConnected()) {
                return;
            } else {
                FileManager.deleteServer(new File("./temp/" + serverName), serverName, this.getTemplate().getStatic());
                String errorMessage = MessageAPI.startFailed.replace("%service", serverName);
                CloudNotifyManager.sendNotifyCloud(errorMessage);
                BedrockCloud.getLogger().error(errorMessage);
            }
        }
    }

    public void stopServer() {
        String notifyMessage = MessageAPI.stopMessage.replace("%service", this.serverName);
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        final ProxyServerDisconnectPacket packet = new ProxyServerDisconnectPacket();
        packet.addValue("reason", "Proxy Stopped");
        this.pushPacket(packet);
    }

    public void pushPacket(final DataPacket cloudPacket) {
        PushPacketManager.pushPacket(cloudPacket, this);
    }

    public void copyServer() {
        final File src = new File("./templates/" + this.template.getName() + "/");
        final File dest = new File("./temp/" + this.serverName);
        FileManager.copy(src, dest);
        final File global_plugins = new File("./local/plugins/waterdogpe");
        final File dest_plugs = new File("./temp/" + this.serverName + "/plugins/");
        FileManager.copy(global_plugins, dest_plugs);
        final File file = new File("./local/versions/waterdogpe");
        final File dest_lib = new File("./temp/" + this.serverName + "/");
        FileManager.copy(file, dest_lib);
        final Config config = new Config("./temp/" + this.serverName + "/cloud.yml", Config.YAML);
        config.set("name", this.serverName);
        config.save();

        ServerProperties.createProperties(this);
    }
}
