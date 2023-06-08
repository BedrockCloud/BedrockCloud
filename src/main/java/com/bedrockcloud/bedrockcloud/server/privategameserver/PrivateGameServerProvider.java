package com.bedrockcloud.bedrockcloud.server.privategameserver;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.config.Config;
import com.bedrockcloud.bedrockcloud.config.FileUtils;
import com.bedrockcloud.bedrockcloud.manager.FileManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PrivateGameServerProvider
{
    public Map<String, PrivateGameServer> gameServerMap;

    public PrivateGameServerProvider() {
        this.gameServerMap = new HashMap<String, PrivateGameServer>();
    }
    
    public Map<String, PrivateGameServer> getGameServerMap() {
        return this.gameServerMap;
    }
    
    public void addGameServer(final PrivateGameServer gameServer) {
        this.gameServerMap.put(gameServer.getServerName(), gameServer);
    }
    
    public void removeServer(final PrivateGameServer gameServer) {
        this.gameServerMap.remove(gameServer.getServerName());
    }
    
    public void removeServer(final String name) {
        this.gameServerMap.remove(name);
    }
    
    public PrivateGameServer getGameServer(final String name) {
        return this.gameServerMap.get(name);
    }
    
    public boolean existServer(final String name) {
        return this.gameServerMap.get(name) != null;
    }
}
