package com.bedrockcloud.bedrockcloud.network.packets;

import java.util.ConcurrentModificationException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;

public class ListServerResponsePacket extends RequestPacket
{
    @Override
    public String getPacketName() {
        return "ListServerResponsePacket";
    }
    
    @Override
    public String encode() {
        final JSONArray arr = new JSONArray();
        try {
            for (final GameServer key : BedrockCloud.getGameServerProvider().getGameServerMap().values()) {
                if (key.getSocket() != null && key.getTemplate().runningTemplateServers.get(key.getServerName()) != null) {
                    arr.add(key.getServerName());
                }
            }
        } catch (ConcurrentModificationException e){
            BedrockCloud.getLogger().exception(e);
        }
        this.addValue("servers", JSONValue.toJSONString(arr));
        return super.encode();
    }
}
