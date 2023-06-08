package com.bedrockcloud.bedrockcloud.command.defaults;

import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.manager.FileManager;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.server.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.templates.Template;
import java.io.File;
import java.io.IOException;

import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.command.Command;

public class ServerCommand extends Command
{
    public ServerCommand() {
        super("server", "server <start | stop | save | list> <group | serverName> <count>", "Manage servers");
    }
    
    @Override
    protected void onCommand(final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) {
                int services = BedrockCloud.getGameServerProvider().getGameServerMap().size() + BedrockCloud.getPrivateGameServerProvider().getGameServerMap().size() + BedrockCloud.getProxyServerProvider().getProxyServerMap().size();
                this.getLogger().info("§e»§r §7There are currently " + services + " Services online! §e«");
                for (final GameServer gameServer : BedrockCloud.getGameServerProvider().gameServerMap.values()) {
                    this.getLogger().info("§c➤ §rGameServer: " + gameServer.getServerName() + " | Players: " + gameServer.getPlayerCount() + " ᐅ " + gameServer.getTemplate().getName());
                }
                for (final PrivateGameServer privateGameServer : BedrockCloud.getPrivateGameServerProvider().gameServerMap.values()) {
                    this.getLogger().info("§c➤ §rPrivateGameServer: " + privateGameServer.getServerName() + " | Players: " + privateGameServer.getPlayerCount() + " ᐅ " + privateGameServer.getTemplate().getName());
                }
                for (final ProxyServer proxyServer : BedrockCloud.getProxyServerProvider().proxyServerMap.values()) {
                    this.getLogger().info("§c➤ §rProxyServer: " + proxyServer.getServerName() + " | Template: " + proxyServer.getTemplate().getName());
                }
            } else if (args.length > 1) {
                if (args[0].equalsIgnoreCase("start")) {
                    if (args.length == 3) {
                        final String groupName = args[1];
                        final int count = Integer.parseInt(args[2]);
                        final Template group = BedrockCloud.getTemplateProvider().getTemplate(groupName);
                        if (group == null) {
                            BedrockCloud.getLogger().error("This Group doesn't exist");
                            return;
                        }

                        if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
                            BedrockCloud.getLogger().error("The Group is currently not running!");
                            return;
                        }

                        for (int i = 0; i < count; ++i) {
                            if (group.getType() == GroupAPI.POCKETMINE_SERVER) {
                                new GameServer(group);
                            } else {
                                if (group.getType() == GroupAPI.PROXY_SERVER) {
                                    new ProxyServer(group);
                                }
                            }
                        }
                    } else {
                        BedrockCloud.getLogger().warning("Try to execute: server start <group> <count>");
                    }
                } else if (args[0].equalsIgnoreCase("stop")) {
                    if (args.length == 2) {
                        final String servername = args[1];
                        final GameServer server = BedrockCloud.getGameServerProvider().getGameServer(servername);
                        if (server == null) {
                            BedrockCloud.getLogger().error("The Server doesn't exist!");
                            return;
                        }
                        server.stopServer();
                    } else {
                        BedrockCloud.getLogger().warning("Try to execute: server stop <server>");
                    }
                } else if (args[0].equalsIgnoreCase("save")) {
                    final String servername = args[1];
                    final GameServer server = BedrockCloud.getGameServerProvider().getGameServer(servername);
                    final String template = server.getTemplate().getName();
                    final File serverFile = new File("./temp/" + servername + "/worlds/");
                    final File templateworldsFile = new File("./templates/" + servername + "/worlds/");
                    final File templateFile = new File("./templates/" + servername + "/worlds/");
                    templateworldsFile.delete();
                    templateFile.mkdirs();
                    FileManager.copy(serverFile, templateFile);
                    if (serverFile.isDirectory()) {
                        final String[] var6;
                        final String[] files = var6 = serverFile.list();
                        for (int var7 = files.length, var8 = 0; var8 < var7; ++var8) {
                            final String file = var6[var8];
                            final File srcFile = new File(serverFile, file);
                            final File destFile = new File(template, file);
                            FileManager.copy(srcFile, destFile);
                        }
                        BedrockCloud.getLogger().info("The GameServer was saved!");
                    }
                }
            }
        } else {
            BedrockCloud.getLogger().warning("Try to execute: server <start | stop | save | list> <group | serverName> <count>");
        }
    }
}
