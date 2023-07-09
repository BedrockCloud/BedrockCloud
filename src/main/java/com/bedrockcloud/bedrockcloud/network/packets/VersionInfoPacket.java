package com.bedrockcloud.bedrockcloud.network.packets;

import java.util.Arrays;
import java.util.Objects;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.utils.Utils;

public class VersionInfoPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "VersionInfoPacket";
    }
    
    @Override
    public String encode() {
        this.addValue("name", Objects.requireNonNull(Utils.getVersion()).name());
        this.addValue("author", Arrays.toString(Objects.requireNonNull(Utils.getVersion()).developers()));
        this.addValue("version", Objects.requireNonNull(Utils.getVersion()).version());
        this.addValue("identifier", Objects.requireNonNull(Utils.getVersion()).identifier());
        return super.encode();
    }
}
