package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONObject;

public class StartPrivateServerPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "StartPrivateServerPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String groupName = jsonObject.get("groupName").toString();
        final String serverOwner = jsonObject.get("serverOwner").toString();
        final Template group = BedrockCloud.getTemplateProvider().getTemplate(groupName);
        if (group == null) {
            BedrockCloud.getLogger().error("Uff.. This group is not exist");
        } else if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
            BedrockCloud.getLogger().error("The group is not running :/");
        } else if (!group.getCanBePrivate()){
            BedrockCloud.getLogger().error("This group can't be used for private-servers");
        } else {
            new PrivateGameServer(group, serverOwner);
        }
    }
}
