package com.bedrockcloud.bedrockcloud.network.packets;

import java.util.ConcurrentModificationException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONValue;
import java.util.Objects;

import org.json.simple.JSONArray;

public class TemplateInfoResponsePacket extends RequestPacket
{
    public String templateName;
    public boolean isLobby;
    public boolean isPrivate;
    public boolean isMaintenance;
    public boolean isBeta;
    public int maxPlayer;

    @Override
    public String getPacketName() {
        return "TemplateInfoResponsePacket";
    }

    @Override
    public String encode() {
        this.addValue("templateName", this.templateName);
        this.addValue("isLobby", this.isLobby);
        this.addValue("isPrivate", this.isPrivate);
        this.addValue("isMaintenance", this.isMaintenance);
        this.addValue("isBeta", this.isBeta);
        final JSONArray arr = new JSONArray();
        try {
            for (final Template key : BedrockCloud.getTemplateProvider().templateMap.values()) {
                if (key.getName() != null && Objects.equals(key.getName(), this.templateName) && key.getType() == GroupAPI.POCKETMINE_SERVER) {
                    arr.add(key.getName());
                }
            }
        } catch (ConcurrentModificationException exception){
            BedrockCloud.getLogger().exception(exception);
        }
        this.addValue("maxPlayer", this.maxPlayer);
        return super.encode();
    }
}