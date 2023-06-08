package com.bedrockcloud.bedrockcloud.network.handler;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.HashMap;
import java.util.Map;
import com.bedrockcloud.bedrockcloud.console.Loggable;

public class PacketHandler implements Loggable
{
    public Map<String, Class> registeredPackets;
    
    public PacketHandler() {
        this.registeredPackets = new HashMap<String, Class>();
    }
    
    public void registerPacket(final String name, final Class packet) {
        if (!this.isPacketRegistered(name)) {
            this.registeredPackets.put(name, packet);
        } else {
            BedrockCloud.getLogger().warning("§cPacket §e" + name + " §cis already registered.");
        }
    }
    
    public void unregisterPacket(final String name) {
        this.registeredPackets.remove(name);
    }
    
    public boolean isPacketRegistered(final String name) {
        return this.registeredPackets.get(name) != null;
    }
    
    public Map<String, Class> getRegisteredPackets() {
        return this.registeredPackets;
    }
    
    public Class getPacketByName(final String packetName) {
        return this.registeredPackets.get(packetName);
    }
    
    public String getPacketNameByRequest(final String request) {
        final Object obj = JSONValue.parse(request);
        if (obj != null) {
            final JSONObject jsonObject = (JSONObject)obj;
            if (jsonObject.get("packetName") != null) {
                return jsonObject.get("packetName").toString();
            }
        }
        this.getLogger().warning("Handling of packet cancelled because the packet is unknown!");
        return "Unknown Packet";
    }
    
    public JSONObject handleJsonObject(final String packetName, final String input) {
        if (this.isPacketRegistered(packetName)) {
            final Object obj = JSONValue.parse(input);
            final JSONObject jsonObject = (JSONObject)obj;
            return jsonObject;
        }

        this.getLogger().warning("§eFailed to handle packet: " + packetName);
        return new JSONObject();
    }

    public void handleCloudPacket(final JSONObject jsonObject, final ClientRequest clientRequest) {
        if (this.isLocalHost(clientRequest)) {
            if (jsonObject.get("packetName") != null) {
                final String packetName = jsonObject.get("packetName").toString();
                final Class c = this.getPacketByName(packetName);
                try {
                    final DataPacket packet = (DataPacket) c.newInstance();
                    packet.handle(jsonObject, clientRequest);
                } catch (InstantiationException | IllegalAccessException ex2) {
                    BedrockCloud.getLogger().exception(ex2);
                }
            }
        } else {
            this.getLogger().warning("§cAuthorization failed. Client infomation:" + clientRequest.getDatagramPacket().getAddress().toString() + ":" + clientRequest.getDatagramPacket().getPort());
        }
    }

    public boolean isLocalHost(ClientRequest request){
        return request.getDatagramPacket().getAddress().isLoopbackAddress();
    }
}