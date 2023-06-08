package com.bedrockcloud.bedrockcloud.server.serviceHelper;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.files.json.json;
import com.bedrockcloud.bedrockcloud.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.manager.FileManager;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;
import com.bedrockcloud.bedrockcloud.server.serviceKiller.ServiceKiller;
import com.bedrockcloud.bedrockcloud.templates.Template;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ServiceHelper {

    public static void startAllProxies() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            BedrockCloud.getLogger().exception(e);
        }
        for (final String name : GroupAPI.getGroups()) {
            try {
                final HashMap<String, Object> stats = (HashMap<String, Object>) json.get(name, 9);
                if (Integer.parseInt(stats.get("type").toString()) == 0) {
                    final Template group = BedrockCloud.getTemplateProvider().getTemplate(name);
                    if (group != null) {
                        if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
                            group.start(false);
                        }
                    }
                }
            } catch (IOException e2) {
                BedrockCloud.getLogger().exception(e2);
            }
        }
    }

    public static void startAllServers() {
        for (final String name : GroupAPI.getGroups()) {
            try {
                final HashMap<String, Object> stats = (HashMap<String, Object>) json.get(name, 9);
                if (Integer.parseInt(stats.get("type").toString()) == 1) {
                    final Template group = BedrockCloud.getTemplateProvider().getTemplate(name);
                    if (group != null) {
                        if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
                            group.start(false);
                        }
                    }
                }
            } catch (IOException e) {
                BedrockCloud.getLogger().exception(e);
            }
        }
    }

    public static void killWithPID(GameServer server) throws IOException {
        killWithPID(true, server);
    }

    public static void killWithPID(PrivateGameServer server) throws IOException {
        killWithPID(true, server);
    }

    public static void killWithPID(ProxyServer server) throws IOException {
        killWithPID(true, server);
    }

    public static void killWithPID(boolean startNewService, GameServer server) throws IOException {
        String notifyMessage = MessageAPI.stoppedMessage.replace("%service", server.getServerName());
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        final ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command("/bin/sh", "-c", "screen -X -S " + server.getServerName() + " kill").start();
        } catch (Exception ignored) {
        }
        try {
            builder.command("/bin/sh", "-c", "kill " + server.pid).start();
        } catch (Exception ignored) {
        }

        try {
            FileManager.deleteServer(new File("./temp/" + server.getServerName()), server.getServerName());
        } catch (NullPointerException ex) {
            BedrockCloud.getLogger().exception(ex);
        }

        ServiceKiller.killPid(server);

        server.getTemplate().removeServer(server.getServerName());
        BedrockCloud.getProxyServerProvider().removeServer(server.getServerName());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        if (!startNewService) return;
        if (BedrockCloud.getTemplateProvider().isTemplateRunning(server.getTemplate())) {
            if (server.getTemplate().getRunningTemplateServers().size() < server.getTemplate().getMinRunningServer()) {
                new ProxyServer(server.getTemplate());
            }
        }
    }

    public static void killWithPID(boolean startNewService, PrivateGameServer server) throws IOException {
        String notifyMessage = MessageAPI.stoppedMessage.replace("%service", server.getServerName());
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        final ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command("/bin/sh", "-c", "screen -X -S " + server.getServerName() + " kill").start();
        } catch (Exception ignored) {
        }
        try {
            builder.command("/bin/sh", "-c", "kill " + server.pid).start();
        } catch (Exception ignored) {
        }

        try {
            FileManager.deleteServer(new File("./temp/" + server.getServerName()), server.getServerName());
        } catch (NullPointerException ex) {
            BedrockCloud.getLogger().exception(ex);
        }

        ServiceKiller.killPid(server);

        server.getTemplate().removeServer(server.getServerName());
        BedrockCloud.getProxyServerProvider().removeServer(server.getServerName());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        if (!startNewService) return;
        if (BedrockCloud.getTemplateProvider().isTemplateRunning(server.getTemplate())) {
            if (server.getTemplate().getRunningTemplateServers().size() < server.getTemplate().getMinRunningServer()) {
                new ProxyServer(server.getTemplate());
            }
        }
    }

    public static void killWithPID(boolean startNewService, ProxyServer server) throws IOException {
        String notifyMessage = MessageAPI.stoppedMessage.replace("%service", server.getServerName());
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        final ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command("/bin/sh", "-c", "screen -X -S " + server.getServerName() + " kill").start();
        } catch (Exception ignored) {
        }
        try {
            builder.command("/bin/sh", "-c", "kill " + server.pid).start();
        } catch (Exception ignored) {
        }

        try {
            FileManager.deleteServer(new File("./temp/" + server.getServerName()), server.getServerName());
        } catch (NullPointerException ex) {
            BedrockCloud.getLogger().exception(ex);
        }

        ServiceKiller.killPid(server);

        server.getTemplate().removeServer(server.getServerName());
        BedrockCloud.getProxyServerProvider().removeServer(server.getServerName());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        if (!startNewService) return;
        if (BedrockCloud.getTemplateProvider().isTemplateRunning(server.getTemplate())) {
            if (server.getTemplate().getRunningTemplateServers().size() < server.getTemplate().getMinRunningServer()) {
                new ProxyServer(server.getTemplate());
            }
        }
    }
}
