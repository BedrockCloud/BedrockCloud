package com.bedrockcloud.bedrockcloud.network.packets;

import java.util.ConcurrentModificationException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import org.json.simple.JSONValue;
import java.util.Objects;

import org.json.simple.JSONArray;

public class GameServerInfoResponsePacket extends RequestPacket
{
    public String serverInfoName;
    public String templateName;
    public int state;
    public boolean isLobby;
    public boolean isPrivate;
    public boolean isMaintenance;
    public boolean isBeta;
    public boolean isStatic;
    public int playerCount;
    public int maxPlayer;
    
    @Override
    public String getPacketName() {
        return "GameServerInfoResponsePacket";
    }
    
    @Override
    public String encode() {
        this.addValue("serverInfoName", this.serverInfoName);
        this.addValue("templateName", this.templateName);
        this.addValue("state", this.state);
        this.addValue("isLobby", this.isLobby);
        this.addValue("isPrivate", this.isPrivate);
        this.addValue("isMaintenance", this.isMaintenance);
        this.addValue("isBeta", this.isBeta);
        this.addValue("isStatic", this.isStatic);
        final JSONArray arr = new JSONArray();
        try {
            for (final CloudPlayer key : BedrockCloud.getCloudPlayerProvider().cloudPlayerMap.values()) {
                if (key.getPlayerName() != null && Objects.equals(key.getCurrentServer(), this.serverInfoName)) {
                    arr.add(key.getPlayerName());
                }
            }
        } catch (ConcurrentModificationException exception){
            BedrockCloud.getLogger().exception(exception);
        }
        this.addValue("players", JSONValue.toJSONString(arr));
        this.addValue("playerCount", this.playerCount);
        this.addValue("maxPlayer", this.maxPlayer);
        return super.encode();
    }
}
