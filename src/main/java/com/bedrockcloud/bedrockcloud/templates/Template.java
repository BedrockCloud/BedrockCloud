package com.bedrockcloud.bedrockcloud.templates;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.server.serviceHelper.ServiceHelper;

public class Template implements Loggable
{
    private final String name;
    private final int minRunningServer;
    private final int maxRunningServer;
    private final int maxPlayers;
    private final int type;
    public final Boolean isBeta;
    public final Boolean isMaintenance;
    public final Boolean isLobby;
    public final Boolean canBePrivate;
    public final Boolean isStatic;
    public HashMap<String, Template> runningTemplateServers;
    public HashMap<String, String> templatePlayer;
    
    public Template(final String name, final Integer minRunningServer, final Integer maxRunningServer, final Integer maxPlayers, final Integer type, final Boolean isBeta, final Boolean isMaintenance, final Boolean isLobby, final Boolean canBePrivate, final Boolean isStatic) {
        this.name = name;
        this.minRunningServer = Math.round(minRunningServer);
        this.maxRunningServer = Math.round(maxRunningServer);
        this.maxPlayers = Math.round(maxPlayers);
        this.type = type;
        this.isBeta = isBeta;
        this.isMaintenance = isMaintenance;
        this.isLobby = isLobby;
        this.canBePrivate = canBePrivate;
        this.isStatic = isStatic;
        if (!BedrockCloud.getTemplateProvider().existsTemplate(this.getName())) {
            BedrockCloud.getTemplateProvider().addTemplate(this);
        }
        this.runningTemplateServers = new HashMap<String, Template>();
        this.templatePlayer = new HashMap<String, String>();
    }

    public HashMap<String, Template> getRunningTemplateServers() {
        return this.runningTemplateServers;
    }

    public void addServer(final Template template, final String serverName) {
        this.runningTemplateServers.put(serverName, template);
    }

    public void removeServer(final String name) {
        this.runningTemplateServers.remove(name);
    }

    public Boolean getCanBePrivate() {
        return canBePrivate;
    }

    public HashMap<String, String> getTemplatePlayers() {
        return this.templatePlayer;
    }

    public void addPlayer(final CloudPlayer player, final String serverName) {
        this.templatePlayer.put(serverName, player.getPlayerName());
    }

    public void removePlayer(final CloudPlayer name) {
        this.templatePlayer.remove(name.getPlayerName());
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public int getCurrentPlayers() {
        return this.templatePlayer.size();
    }
    
    public int getMaxRunningServer() {
        return this.maxRunningServer;
    }
    
    public int getMinRunningServer() {
        return this.minRunningServer;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void start(boolean byCommand) {
        if (this.getMaintenance()) {
            if (!byCommand) {
                BedrockCloud.getLogger().warning("§cThe group §e" + this.getName() + " §cwas not started because it is in maintenance, but you can start it yourself with the command §7'§etemplate start <template>§7'§c.");
                return;
            }
        }

        BedrockCloud.getLogger().info("Starting group " + this.getName() + "...");
        if (this.getType() == 0) {
            new ProxyServer(this);
        } else {
            for (int i = 0; i < this.getMinRunningServer(); ++i) {
                new GameServer(this);
            }
        }
        BedrockCloud.getTemplateProvider().addRunningTemplate(this);
    }

    public void restart() {
        BedrockCloud.getLogger().info("Restarting group " + this.getName() + "...");
        for (final String servername : BedrockCloud.getGameServerProvider().gameServerMap.keySet()) {
            if (Objects.equals(BedrockCloud.getGameServerProvider().getGameServer(servername).getTemplate().getName(), this.getName())) {
                final GameServer server = BedrockCloud.getGameServerProvider().getGameServer(servername);
                if (server == null) {
                    return;
                }
                server.stopServer();
            }
        }

        for (final String servername : BedrockCloud.getPrivateGameServerProvider().gameServerMap.keySet()) {
            if (Objects.equals(BedrockCloud.getPrivateGameServerProvider().getGameServer(servername).getTemplate().getName(), this.getName())) {
                final PrivateGameServer server = BedrockCloud.getPrivateGameServerProvider().getGameServer(servername);
                if (server == null) {
                    return;
                }
                server.stopServer();
            }
        }

        for (final String servername : BedrockCloud.getProxyServerProvider().proxyServerMap.keySet()) {
            if (Objects.equals(BedrockCloud.getProxyServerProvider().getProxyServer(servername).getTemplate().getName(), this.getName())) {
                final ProxyServer server = BedrockCloud.getProxyServerProvider().getProxyServer(servername);
                if (server == null) {
                    return;
                }
                server.stopServer();
            }
        }
    }
    
    public void stop() {
        BedrockCloud.getLogger().info("Stopping group " + this.getName() + "...");
        BedrockCloud.getTemplateProvider().removeRunningGroup(this);

        for (final String servername : BedrockCloud.getGameServerProvider().gameServerMap.keySet()) {
            if (Objects.equals(BedrockCloud.getGameServerProvider().getGameServer(servername).getTemplate().getName(), this.getName())) {
                final GameServer server = BedrockCloud.getGameServerProvider().getGameServer(servername);
                if (server == null) {
                    return;
                }
                server.stopServer();
            }
        }

        for (final String servername : BedrockCloud.getPrivateGameServerProvider().gameServerMap.keySet()) {
            if (Objects.equals(BedrockCloud.getPrivateGameServerProvider().getGameServer(servername).getTemplate().getName(), this.getName())) {
                final PrivateGameServer server = BedrockCloud.getPrivateGameServerProvider().getGameServer(servername);
                if (server == null) {
                    return;
                }
                server.stopServer();
            }
        }

        for (final String servername : BedrockCloud.getProxyServerProvider().proxyServerMap.keySet()) {
            if (Objects.equals(BedrockCloud.getProxyServerProvider().getProxyServer(servername).getTemplate().getName(), this.getName())) {
                final ProxyServer server = BedrockCloud.getProxyServerProvider().getProxyServer(servername);
                if (server == null) {
                    return;
                }
                server.stopServer();
            }
        }
    }
    
    public Boolean getBeta() {
        return this.isBeta;
    }
    
    public Boolean getLobby() {
        return this.isLobby;
    }
    
    public Boolean getMaintenance() {
        return this.isMaintenance;
    }

    public Boolean getStatic() {
        return isStatic;
    }

    @Override
    public String toString() {
        return "Template{name='" + this.name + '\'' + ", minRunningServer=" + this.minRunningServer + ", maxRunningServer=" + this.maxRunningServer + ", maxPlayers=" + this.maxPlayers + ", type='" + this.type + '\'' + '}';
    }
}
