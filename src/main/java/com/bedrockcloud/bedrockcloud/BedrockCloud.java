package com.bedrockcloud.bedrockcloud;

import com.bedrockcloud.bedrockcloud.command.CommandRegeistry;
import com.bedrockcloud.bedrockcloud.console.shutdown.ShutdownThread;
import com.bedrockcloud.bedrockcloud.network.packetRegistry.PacketRegistry;
import com.bedrockcloud.bedrockcloud.player.CloudPlayerProvider;
import com.bedrockcloud.bedrockcloud.config.Config;
import com.bedrockcloud.bedrockcloud.rest.App;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServerProvider;
import com.bedrockcloud.bedrockcloud.server.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.tasks.RestartAllTask;
import com.bedrockcloud.bedrockcloud.command.CommandManager;
import com.bedrockcloud.bedrockcloud.network.NetworkManager;
import com.bedrockcloud.bedrockcloud.network.handler.PacketHandler;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServerProvider;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServerProvider;
import com.bedrockcloud.bedrockcloud.templates.TemplateProvider;
import com.bedrockcloud.bedrockcloud.console.Logger;

import java.net.URISyntaxException;
import java.net.Socket;
import java.util.Timer;

public class BedrockCloud
{
    public static TemplateProvider templateProvider;
    public static GameServerProvider gameServerProvider;
    public static PrivateGameServerProvider privategameServerProvider;
    public static ProxyServerProvider proxyServerProvider;
    public static CloudPlayerProvider cloudPlayerProvider;
    public static PacketHandler packetHandler;
    public static NetworkManager networkManager;
    public static CommandManager commandManager;
    private static Socket socket;
    private static App app;
    protected static boolean running;

    public final static String prefix = "§l§bCloud §r§8» §r";
    
    public static Logger getLogger() {
        return new Logger();
    }
    
    public static Socket getSocket() {
        return BedrockCloud.socket;
    }
    
    public static void setSocket(final Socket sockets) {
        BedrockCloud.socket = sockets;
    }

    public BedrockCloud() {
        running = true;
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

        this.initProvider();

        CommandRegeistry.registerCommands();
        BedrockCloud.networkManager = new NetworkManager((int) getConfig().getDouble("port"));

        if (BedrockCloud.getConfig().getBoolean("rest-enabled", true)) {
            app = new App();
        } else {
            BedrockCloud.getLogger().warning("§cRestAPI is currently disabled. You can enable it in your cloud config.");
        }

        getTemplateProvider().loadTemplates();

        final Timer ttime = new Timer();
        if (getConfig().getBoolean("auto-restart-cloud", false)) {
            ttime.schedule(new RestartAllTask(), 1000L, 1000L);
        }

        ServiceHelper.startAllProxies();
        ServiceHelper.startAllServers();
        BedrockCloud.networkManager.start();
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        BedrockCloud.running = running;
    }
    
    private void initProvider() {
        BedrockCloud.commandManager = new CommandManager();
        BedrockCloud.templateProvider = new TemplateProvider();
        BedrockCloud.gameServerProvider = new GameServerProvider();
        BedrockCloud.privategameServerProvider = new PrivateGameServerProvider();
        BedrockCloud.proxyServerProvider = new ProxyServerProvider();
        BedrockCloud.cloudPlayerProvider = new CloudPlayerProvider();
        BedrockCloud.packetHandler = new PacketHandler();
        PacketRegistry.registerPackets();
        BedrockCloud.commandManager.start();
    }
    
    public static VersionInfo getVersion() {
        return CloudStarter.class.isAnnotationPresent(VersionInfo.class) ? CloudStarter.class.getAnnotation(VersionInfo.class) : null;
    }
    
    public static TemplateProvider getTemplateProvider() {
        return BedrockCloud.templateProvider;
    }
    
    public static ProxyServerProvider getProxyServerProvider() {
        return BedrockCloud.proxyServerProvider;
    }
    
    public static Config getConfig() {
        return new Config("./local/config.yml", Config.YAML);
    }

    public static Config getTemplateConfig() {
        return new Config("./templates/config.json", Config.JSON);
    }
    
    public static PacketHandler getPacketHandler() {
        return BedrockCloud.packetHandler;
    }
    
    public static GameServerProvider getGameServerProvider() {
        return BedrockCloud.gameServerProvider;
    }

    public static PrivateGameServerProvider getPrivateGameServerProvider() {
        return BedrockCloud.privategameServerProvider;
    }
    
    public static CloudPlayerProvider getCloudPlayerProvider() {
        return BedrockCloud.cloudPlayerProvider;
    }

    public static String getCloudPath(){
        try {
            String path = BedrockCloud.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            String fullPath = path.substring(path.lastIndexOf("/") + 1);
            return path.replace(fullPath, "");
        } catch (NullPointerException | URISyntaxException e){
            BedrockCloud.getLogger().exception(e);
            return "";
        }
    }

    public static void printCloudInfos() {
        BedrockCloud.getLogger().command("Used Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes");
        BedrockCloud.getLogger().command("Free Memory   : " + Runtime.getRuntime().freeMemory() + " bytes");
        BedrockCloud.getLogger().command("Total Memory  : " + Runtime.getRuntime().totalMemory() + " bytes");
        BedrockCloud.getLogger().command("Max Memory    : " + Runtime.getRuntime().maxMemory() + " bytes");
    }
}