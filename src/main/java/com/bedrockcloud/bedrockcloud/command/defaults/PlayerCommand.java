package com.bedrockcloud.bedrockcloud.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerKickPacket;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerMovePacket;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;

import java.util.Objects;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerTextPacket;
import com.bedrockcloud.bedrockcloud.command.Command;

public class PlayerCommand extends Command
{
    public PlayerCommand() {
        super("player", "player <list | message | kick | transfer> [player] [message | reason | server]", "Manage players");
    }
    
    @Override
    protected void onCommand(final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) {
                this.getLogger().info("§e»§r §7There are currently " + BedrockCloud.getCloudPlayerProvider().cloudPlayerMap.size() + " Player connected! §e«");
                for (CloudPlayer cloudPlayer : BedrockCloud.getCloudPlayerProvider().cloudPlayerMap.values()) {
                    this.getLogger().info("§c➤ §rPlayer: " + cloudPlayer.getPlayerName() + " (" + cloudPlayer.getAddress() + ") | (" + cloudPlayer.getXuid() + ") ᐅ " + cloudPlayer.getCurrentServer());
                }
            } else if (args[0].equalsIgnoreCase("message") && args.length != 2) {
                final String value = args[1];
                final String playerName = args[2];
                if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
                    final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
                    playerTextPacket.playerName = playerName;
                    Objects.requireNonNull(playerTextPacket);
                    playerTextPacket.type = 0;
                    playerTextPacket.value = value;
                    BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerTextPacket);
                } else {
                    BedrockCloud.getLogger().command("§cThis player don't exists.");
                }
            } else if (args[0].equalsIgnoreCase("kick") && args.length != 2) {
                final String playerName = args[1];
                final String reason = args[2];
                if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
                    final PlayerKickPacket playerKickPacket = new PlayerKickPacket();
                    playerKickPacket.playerName = playerName;
                    Objects.requireNonNull(playerKickPacket);
                    playerKickPacket.reason = reason;
                    BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerKickPacket);
                } else {
                    BedrockCloud.getLogger().command("§cThis player don't exists.");
                }
            } else if (args[0].equalsIgnoreCase("transfer")) {
                final String playerName = args[1];
                final String server = args[2];
                if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
                    if (BedrockCloud.getGameServerProvider().existServer(server) || BedrockCloud.getPrivateGameServerProvider().existServer(server)) {
                        final PlayerMovePacket playerMovePacket = new PlayerMovePacket();
                        playerMovePacket.playerName = playerName;
                        Objects.requireNonNull(playerMovePacket);
                        playerMovePacket.toServer = server;
                        BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerMovePacket);
                    } else {
                        BedrockCloud.getLogger().command("§cThis server don't exists.");
                    }
                } else {
                    BedrockCloud.getLogger().command("§cThis player don't exists.");
                }
            }
        }
    }
}
