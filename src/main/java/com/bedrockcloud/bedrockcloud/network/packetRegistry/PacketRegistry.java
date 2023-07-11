package com.bedrockcloud.bedrockcloud.network.packetRegistry;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.packets.*;

public class PacketRegistry {

    public static void registerPackets() {
        BedrockCloud.getPacketHandler().registerPacket("GameServerConnectPacket", GameServerConnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("GameServerDisconnectPacket", GameServerDisconnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ProxyServerConnectPacket", ProxyServerConnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ProxyServerDisconnectPacket", ProxyServerDisconnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("GameServerInfoRequestPacket", GameServerInfoRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("GameServerInfoResponsePacket", GameServerInfoResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ListServerRequestPacket", ListServerRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ListServerResponsePacket", ListServerResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ListCloudPlayersRequestPacket", ListCloudPlayersRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ListCloudPlayersResponsePacket", ListCloudPlayersResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket("PlayerMessagePacket", PlayerMessagePacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ProxyPlayerJoinPacket", ProxyPlayerJoinPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ProxyPlayerQuitPacket", ProxyPlayerQuitPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("UnregisterServerPacket", UnregisterServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("RegisterServerPacket", RegisterServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("KeepALivePacket", KeepALivePacket.class);
        BedrockCloud.getPacketHandler().registerPacket("UpdateGameServerInfoPacket", UpdateGameServerInfoPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("StartGroupPacket", StartGroupPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("StartServerPacket", StartServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("StartPrivateServerPacket", StartPrivateServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("StopGroupPacket", StopGroupPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("StopServerPacket", StopServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("StopPrivateServerPacket", StopPrivateServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("CloudPlayerAddPermissionPacket", CloudPlayerAddPermissionPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("VersionInfoPacket", VersionInfoPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("CloudNotifyMessagePacket", CloudNotifyMessagePacket.class);
        BedrockCloud.getPacketHandler().registerPacket("PlayerMovePacket", PlayerMovePacket.class);
        BedrockCloud.getPacketHandler().registerPacket("PlayerKickPacket", PlayerKickPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("CloudPlayerChangeServerPacket", CloudPlayerChangeServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("SendToHubPacket", SendToHubPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ListTemplatesRequestPacket", ListTemplatesRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("ListTemplatesResponsePacket", ListTemplatesResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket("TemplateInfoRequestPacket", TemplateInfoRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("TemplateInfoResponsePacket", TemplateInfoResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket("CloudPlayerInfoRequestPacket", CloudPlayerInfoRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket("CloudPlayerInfoResponsePacket", CloudPlayerInfoResponsePacket.class);
    }
}
