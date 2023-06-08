package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONObject;

public class StartGroupPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "StartGroupPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String groupName = jsonObject.get("groupName").toString();
        final Template group = BedrockCloud.getTemplateProvider().getTemplate(groupName);
        if (group == null) {
            this.getLogger().error("Uff.. This group is not exist");
        }
        else if (BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
            this.getLogger().error("The group is already running");
        }
        else {
            group.start(true);
        }
    }
}
