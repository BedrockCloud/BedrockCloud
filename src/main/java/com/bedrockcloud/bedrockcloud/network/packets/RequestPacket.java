package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.network.DataPacket;

public class RequestPacket extends DataPacket implements Loggable
{
    public String requestId;
    public int type;
    
    @Override
    public String encode() {
        this.addValue("requestId", this.requestId);
        this.addValue("type", this.type);
        return super.encode();
    }
}
