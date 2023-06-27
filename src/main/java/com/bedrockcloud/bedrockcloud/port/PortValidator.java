package com.bedrockcloud.bedrockcloud.port;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServerProvider;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.templates.Template;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public final class PortValidator {
    private static final int PORTS_BOUNCE_CLOUD = 30000;
    private static final int PORTS_BOUNCE_PROXY = 19132;
    private static final int PORTS_BOUNCE = 40000;

    public static int getFreeCloudPort() {
        var port = PORTS_BOUNCE_CLOUD;
        while (isCloudPortUsed(port) || isCloudPortUsed(port+1)) {
            port++;
        }

        return port;
    }

    public static int getNextServerPort(GameServer server) {
        var port = PORTS_BOUNCE;
        while (isPortUsed(port) || isPortUsed(port+1)) {
            port++;
        }
        return port;
    }

    public static int getNextPrivateServerPort(PrivateGameServer server) {
        var port = PORTS_BOUNCE;
        while (isPortUsed(port) || isPortUsed(port+1)) {
            port++;
        }
        return port;
    }

    public static int getNextProxyServerPort(ProxyServer server) {
        var port = PORTS_BOUNCE_PROXY;
        while (isPortUsed(port) || isPortUsed(port+1)) {
            port++;
        }
        return port;
    }

    public static int getNextLobbyServerPort(GameServer server) {
        var port = PORTS_BOUNCE_PROXY;
        while (isPortUsed(port) || isPortUsed(port+1)) {
            port++;
        }
        return port;
    }

    private static boolean isPortUsed(int port) {
        for (final var service : BedrockCloud.getGameServerProvider().gameServerMap.values()) {
            if (service.getServerPort() == port || service.getServerPort()+1 == port) return true;
        }
        for (final var service : BedrockCloud.getPrivateGameServerProvider().gameServerMap.values()) {
            if (service.getServerPort() == port || service.getServerPort()+1 == port) return true;
        }
        for (final var service : BedrockCloud.getProxyServerProvider().proxyServerMap.values()) {
            if (service.getServerPort() == port || service.getServerPort()+1 == port) return true;
        }

        try (final var serverSocket = new DatagramSocket(port)) {
            serverSocket.close();
            return false;
        } catch (Exception exception) {
            return true;
        }
    }

    private static boolean isCloudPortUsed(int port){
        try (final var serverSocket = new DatagramSocket(port)) {
            serverSocket.close();
            return false;
        } catch (Exception exception) {
            return true;
        }
    }
}
