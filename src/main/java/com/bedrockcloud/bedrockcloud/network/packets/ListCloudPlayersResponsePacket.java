package com.bedrockcloud.bedrockcloud.network.packets;

import java.util.ConcurrentModificationException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;

public class ListCloudPlayersResponsePacket extends RequestPacket
{
    @Override
    public String getPacketName() {
        return "ListCloudPlayersResponsePacket";
    }
    
    @Override
    public String encode() {
        final JSONArray arr = new JSONArray();
        try {
            for (final CloudPlayer key : BedrockCloud.getCloudPlayerProvider().cloudPlayerMap.values()) {
                if (key.getPlayerName() != null) {
                    arr.add(key.getPlayerName());
                }
            }
        } catch (ConcurrentModificationException e){
            BedrockCloud.getLogger().exception(e);
        }
        this.addValue("players", JSONValue.toJSONString(arr));
        return super.encode();
    }
}
