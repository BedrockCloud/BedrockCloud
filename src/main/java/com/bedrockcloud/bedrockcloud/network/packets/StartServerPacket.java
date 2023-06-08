package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONObject;

public class StartServerPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "StartServerPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String groupName = jsonObject.get("groupName").toString();
        final String count = jsonObject.get("count").toString();
        final Template group = BedrockCloud.getTemplateProvider().getTemplate(groupName);
        if (group == null) {
            BedrockCloud.getLogger().error("Uff.. This group is not exist");
        }
        else if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
            BedrockCloud.getLogger().error("The group is not running :/");
        }
        else {
            for (int i = 0; i < Integer.parseInt(count); ++i) {
                if (group.getType() == GroupAPI.POCKETMINE_SERVER) {
                    new GameServer(group);
                } else {
                    if (group.getType() == GroupAPI.PROXY_SERVER){
                        new ProxyServer(group);
                    }
                }
            }
        }
    }
}
