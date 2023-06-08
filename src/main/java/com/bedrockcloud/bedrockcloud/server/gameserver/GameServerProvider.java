package com.bedrockcloud.bedrockcloud.server.gameserver;

import java.io.*;
import java.util.Objects;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.config.Config;
import com.bedrockcloud.bedrockcloud.config.FileUtils;
import com.bedrockcloud.bedrockcloud.manager.FileManager;

import java.util.HashMap;
import java.util.Map;

public class GameServerProvider
{
    public Map<String, GameServer> gameServerMap;
    
    public GameServerProvider() {
        this.gameServerMap = new HashMap<String, GameServer>();
    }
    
    public Map<String, GameServer> getGameServerMap() {
        return this.gameServerMap;
    }
    
    public void addGameServer(final GameServer gameServer) {
        this.gameServerMap.put(gameServer.getServerName(), gameServer);
    }
    
    public void removeServer(final GameServer gameServer) {
        this.gameServerMap.remove(gameServer.getServerName());
    }
    
    public void removeServer(final String name) {
        this.gameServerMap.remove(name);
    }
    
    public GameServer getGameServer(final String name) {
        return this.gameServerMap.get(name);
    }
    
    public boolean existServer(final String name) {
        return this.gameServerMap.get(name) != null;
    }
}
