package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class UnregisterServerPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "UnregisterServerPacket";
    }
}
