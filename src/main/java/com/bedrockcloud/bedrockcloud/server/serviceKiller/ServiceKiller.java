package com.bedrockcloud.bedrockcloud.server.serviceKiller;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.config.Config;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServiceKiller {

    public static void killPidByPort(int port) {
        try {
            String[] command = {"fuser", "-k", port + "/udp"};
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                BedrockCloud.getLogger().error("Failed to kill process for port " + port);
            }
        } catch (IOException | InterruptedException ex) {
            BedrockCloud.getLogger().exception(ex);
        }

    }

    public static void killPid(GameServer server){
        final File pidFile = new File("./archive/server-pids/" + server.getServerName() + ".json");
        if (pidFile.exists()) {
            Config config = new Config("./archive/server-pids/" + server.getServerName() + ".json", Config.JSON);

            final ProcessBuilder builder = new ProcessBuilder();
            try {
                builder.command("/bin/sh", "-c", "kill " + config.get("pid")).start();
            } catch (Exception ignored) {
            }

            pidFile.delete();

            ServiceKiller.killPidByPort(server.getServerPort() + 1);
        }
    }

    public static void killPid(PrivateGameServer server){
        final File pidFile = new File("./archive/server-pids/" + server.getServerName() + ".json");
        if (pidFile.exists()) {
            Config config = new Config("./archive/server-pids/" + server.getServerName() + ".json", Config.JSON);

            final ProcessBuilder builder = new ProcessBuilder();
            try {
                builder.command("/bin/sh", "-c", "kill " + config.get("pid")).start();
            } catch (Exception ignored) {
            }

            pidFile.delete();

            ServiceKiller.killPidByPort(server.getServerPort() + 1);
        }
    }

    public static void killPid(ProxyServer server){
        final File pidFile = new File("./archive/server-pids/" + server.getServerName() + ".json");
        if (pidFile.exists()) {
            Config config = new Config("./archive/server-pids/" + server.getServerName() + ".json", Config.JSON);

            final ProcessBuilder builder = new ProcessBuilder();
            try {
                builder.command("/bin/sh", "-c", "kill " + config.get("pid")).start();
            } catch (Exception ignored) {
            }

            pidFile.delete();

            ServiceKiller.killPidByPort(server.getServerPort() + 1);
        }
    }
}
