package com.bedrockcloud.bedrockcloud.network.packets;

import java.util.Arrays;
import java.util.Objects;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class VersionInfoPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "VersionInfoPacket";
    }
    
    @Override
    public String encode() {
        this.addValue("name", Objects.requireNonNull(BedrockCloud.getVersion()).name());
        this.addValue("author", Arrays.toString(Objects.requireNonNull(BedrockCloud.getVersion()).developers()));
        this.addValue("version", Objects.requireNonNull(BedrockCloud.getVersion()).version());
        this.addValue("identifier", Objects.requireNonNull(BedrockCloud.getVersion()).identifier());
        return super.encode();
    }
}
