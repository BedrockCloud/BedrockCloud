package com.bedrockcloud.bedrockcloud.network.packets;

import java.util.ConcurrentModificationException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;

public class ListTemplatesResponsePacket extends RequestPacket
{
    @Override
    public String getPacketName() {
        return "ListTemplatesResponsePacket";
    }

    @Override
    public String encode() {
        final JSONArray arr = new JSONArray();
        try {
            for (final Template key : BedrockCloud.getTemplateProvider().getRunningTemplates().values()) {
                if (key.getName() != null) {
                    if (key.getType() == GroupAPI.POCKETMINE_SERVER) {
                        arr.add(key.getName());
                    }
                }
            }
        } catch (ConcurrentModificationException e){
            BedrockCloud.getLogger().exception(e);
        }
        this.addValue("templates", JSONValue.toJSONString(arr));
        return super.encode();
    }
}
