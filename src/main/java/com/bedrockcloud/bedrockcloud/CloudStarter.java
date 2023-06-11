package com.bedrockcloud.bedrockcloud;

import com.bedrockcloud.bedrockcloud.files.Startfiles;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@VersionInfo(name = "BedrockCloud", version = "3.0.2", developers = { "BedrockCloud" }, identifier = "@Stable")
public class CloudStarter {
    public static void main(String[] args) {
        try {
            Class.forName("com.bedrockcloud.bedrockcloud.BedrockCloud");
            int javaVersion = getJavaVersion();
            if (javaVersion < 17) {
                BedrockCloud.getLogger().error("Using unsupported Java version! Minimum supported version is Java 17, found Java " + javaVersion);
                Runtime.getRuntime().halt(0);
                return;
            }

            if (!isLinux()){
                BedrockCloud.getLogger().error("You need a Linux distribution to use BedrockCloud.");
                Runtime.getRuntime().halt(0);
                return;
            }

            File file = new File("./bin");
            if (!file.exists()){
                BedrockCloud.getLogger().error("No PocketMine PHP binary was found. This is needed to start the PocketMine servers.");
                Runtime.getRuntime().halt(0);
                return;
            }

            Thread.currentThread().setName("BedrockCloud");

            try {
                if (!Objects.requireNonNull(BedrockCloud.getVersion()).identifier().equals("@Beta")) {
                    if (!Objects.equals(getVersion(), Objects.requireNonNull(BedrockCloud.getVersion()).version())) {
                        BedrockCloud.getLogger().info("§cYou are not using the latest version of BedrockCloud.");
                        BedrockCloud.getLogger().info("§aNewest version§f: §e" + getVersion());
                    } else {
                        BedrockCloud.getLogger().info("§aBedrockCloud is on the latest version.");
                    }
                } else {
                    BedrockCloud.getLogger().info("§cYou are currently using a development version of BedrockCloud. Errors may occur.");
                }
            } catch (IOException ignored){}

            new Startfiles();
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                BedrockCloud.getLogger().exception(e);
            }
            new BedrockCloud();

        }catch (ClassNotFoundException ex) {
            BedrockCloud.getLogger().exception(ex);
        }
    }

    public static String checkForUpdate() throws IOException {
        String url = "https://raw.githubusercontent.com/BedrockCloud/CloudSystem/master/src/main/resources/version.yml";
        InputStream inputStream = new URL(url).openStream();
        String content = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            content = bufferedReader.lines().reduce("", (x, y) -> x + y);
        }
        return content;
    }

    public static String getVersion() throws IOException {
        String content = checkForUpdate();
        Yaml yaml = new Yaml();
        Object obj = yaml.load(content);
        return (String) ((java.util.LinkedHashMap<?, ?>) obj).get("version");
    }

    private static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            return Integer.parseInt(version.substring(2, 3));
        }

        int index = version.indexOf(".");
        if (index != -1) {
            version = version.substring(0, index);
        }
        return Integer.parseInt(version);
    }

    public static boolean isLinux(){
        String osName = System.getProperty("os.name");
        return (osName.contains("Linux"));
    }
}
