package com.bedrockcloud.bedrockcloud.network;

import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.bedrockcloud.bedrockcloud.console.Loggable;

public class DataPacket implements Loggable
{
    public final int TYPE_REQUEST = 0;
    public final int TYPE_REPONSE = 1;
    private String serverName;
    public String packetName;
    public Map<String, Object> data;
    
    public DataPacket() {
        this.data = new HashMap<String, Object>();
    }
    
    public String getPacketName() {
        return this.packetName;
    }
    
    public String getServerName() {
        return this.serverName;
    }
    
    public void addValue(final String key, final String value) {
        this.data.put(key, value);
    }
    
    public void addValue(final String key, final int value) {
        this.data.put(key, value);
    }
    
    public void addValue(final String key, final boolean value) {
        this.data.put(key, value);
    }
    
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        this.serverName = jsonObject.get("serverName").toString();
    }
    
    public String encode() {
        this.addValue("packetName", this.getPacketName());
        return JSONValue.toJSONString(this.data);
    }
}
