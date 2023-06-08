package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class CloudNotifyMessagePacket extends DataPacket
{
    public String message;

    @Override
    public String getPacketName() {
        return "CloudNotifyMessagePacket";
    }

    @Override
    public String encode() {
        this.addValue("message", this.message);
        return super.encode();
    }
}