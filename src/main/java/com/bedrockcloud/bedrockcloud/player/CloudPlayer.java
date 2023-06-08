package com.bedrockcloud.bedrockcloud.player;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;

public class CloudPlayer
{
    private final String playerName;
    private final String address;
    private final String uuid;
    private String currentServer;
    private final String xuid;
    private final String currentProxy;
    private boolean hasPrivateServer;
    
    public CloudPlayer(String playerName, String address, String uuid, String xuid, String currentServer, String currentProxy) {
        this.playerName = playerName.replace(" ", "_");
        this.address = address;
        this.uuid = uuid;
        this.xuid = xuid;
        this.currentServer = currentServer;
        this.currentProxy = currentProxy;
        this.hasPrivateServer = false;
    }
    
    public String getPlayerName() {
        return this.playerName;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public String getCurrentProxy() {
        return this.currentProxy;
    }
    
    public String getCurrentServer() {
        return this.currentServer;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public String getXuid() {
        return this.xuid;
    }
    
    public void setCurrentServer(final String serverName) {
        this.currentServer = serverName;
    }
    
    public ProxyServer getProxy() {
        return BedrockCloud.getProxyServerProvider().getProxyServer(this.getCurrentProxy());
    }

    public boolean isHasPrivateServer() {
        return hasPrivateServer;
    }

    public void setHasPrivateServer(boolean bool){
        this.hasPrivateServer = bool;
    }

    @Override
    public String toString() {
        return "CloudPlayer{playerName='" + this.playerName + '\'' + ", address='" + this.address + '\'' + ", uuid='" + this.uuid + '\'' + ", currentServer='" + this.currentServer + '\'' + ", xuid='" + this.xuid + '\'' + ", currentProxy='" + this.currentProxy + '\'' + '}';
    }
}
