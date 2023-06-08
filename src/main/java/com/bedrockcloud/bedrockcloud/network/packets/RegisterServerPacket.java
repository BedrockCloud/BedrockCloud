package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class RegisterServerPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "RegisterServerPacket";
    }
}
