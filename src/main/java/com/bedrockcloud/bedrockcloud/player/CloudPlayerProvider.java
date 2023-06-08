package com.bedrockcloud.bedrockcloud.player;

import java.util.HashMap;
import java.util.Map;

public class CloudPlayerProvider
{
    public HashMap<String, CloudPlayer> cloudPlayerMap;
    
    public CloudPlayerProvider() {
        this.cloudPlayerMap = new HashMap<String, CloudPlayer>();
    }
    
    public HashMap<String, CloudPlayer> getCloudPlayerMap() {
        return this.cloudPlayerMap;
    }
    
    public void addCloudPlayer(final CloudPlayer cloudPlayer) {
        this.cloudPlayerMap.put(cloudPlayer.getPlayerName().toLowerCase(), cloudPlayer);
    }
    
    public void removeCloudPlayer(final CloudPlayer cloudPlayer) {
        this.cloudPlayerMap.remove(cloudPlayer.getPlayerName().toLowerCase(), cloudPlayer);
    }
    
    public void removeCloudPlayer(final String playerName, final CloudPlayer cloudPlayer) {
        this.cloudPlayerMap.remove(playerName.toLowerCase(), cloudPlayer);
    }
    
    public CloudPlayer getCloudPlayer(final String playerName) {
        return this.cloudPlayerMap.get(playerName.toLowerCase());
    }
    
    public boolean existsPlayer(final String playerName) {
        return this.cloudPlayerMap.containsKey(playerName.toLowerCase());
    }
}
