package com.bedrockcloud.bedrockcloud.manager;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.packets.CloudNotifyMessagePacket;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;

public class CloudNotifyManager {

    public static void sendNotifyCloud(final String message) {
        for (final ProxyServer proxy : BedrockCloud.getProxyServerProvider().getProxyServerMap().values()) {
            final CloudNotifyMessagePacket packet = new CloudNotifyMessagePacket();
            packet.message = BedrockCloud.prefix + message;
            proxy.pushPacket(packet);
        }
    }
}
