package com.bedrockcloud.bedrockcloud.utils;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.CloudStarter;
import com.bedrockcloud.bedrockcloud.VersionInfo;
import com.bedrockcloud.bedrockcloud.config.Config;

import java.net.URISyntaxException;

public class Utils {

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void printCloudInfos() {
        BedrockCloud.getLogger().command("Used Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes");
        BedrockCloud.getLogger().command("Free Memory   : " + Runtime.getRuntime().freeMemory() + " bytes");
        BedrockCloud.getLogger().command("Total Memory  : " + Runtime.getRuntime().totalMemory() + " bytes");
        BedrockCloud.getLogger().command("Max Memory    : " + Runtime.getRuntime().maxMemory() + " bytes");
    }

    public static String getCloudPath(){
        try {
            String path = BedrockCloud.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            String fullPath = path.substring(path.lastIndexOf("/") + 1);
            return path.replace(fullPath, "");
        } catch (NullPointerException | URISyntaxException e){
            BedrockCloud.getLogger().exception(e);
            return "";
        }
    }

    public static Config getTemplateConfig() {
        return new Config("./templates/config.json", Config.JSON);
    }

    public static Config getConfig() {
        return new Config("./local/config.yml", Config.YAML);
    }

    public static VersionInfo getVersion() {
        return CloudStarter.class.isAnnotationPresent(VersionInfo.class) ? CloudStarter.class.getAnnotation(VersionInfo.class) : null;
    }

    public static String boolToString(Boolean bool){
        return (bool ? "§aYes" : "§cNo");
    }
}
