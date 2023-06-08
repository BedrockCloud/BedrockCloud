package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class StopGroupPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "StopGroupPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String groupName = jsonObject.get("groupName").toString();
        final Template group = BedrockCloud.getTemplateProvider().getTemplate(groupName);
        if (group == null) {
            BedrockCloud.getLogger().error("Uff.. This group is not exist");
        }
        else if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
            BedrockCloud.getLogger().error("The group is not running :/");
        } else {
            group.stop();
        }
    }
}
