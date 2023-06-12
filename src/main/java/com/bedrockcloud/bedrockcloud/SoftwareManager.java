package com.bedrockcloud.bedrockcloud;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SoftwareManager {

    //Software URLs
    public final static String POCKETMINE_URL = "https://github.com/pmmp/PocketMine-MP/releases/latest/download/PocketMine-MP.phar";
    public final static String WATERDOGPE_URL = "https://github.com/WaterdogPE/WaterdogPE/releases/latest/download/Waterdog.jar";

    //Plugin URLs
    public final static String CLOUDBRIDGEPM_URL = "https://github.com/BedrockCloud/CloudBridge-PM/releases/latest/download/CloudBridge.phar";
    public final static String DEVTOOLS_URL = "https://poggit.pmmp.io/r/199538/PocketMine-DevTools_dev-217.phar";
    public final static String CLOUDBRIDGEWD_URL = "https://github.com/BedrockCloud/CloudBridge-WD/releases/latest/download/BedrockProxyCore.jar";

    public static boolean download(final String url, final String destinationPath) {
        try {
            URL downloadUrl = new URL(url);
            InputStream inputStream = downloadUrl.openStream();
            Path destination = Path.of(destinationPath);

            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
        } catch (IOException e) {
            BedrockCloud.getLogger().exception(e);
            return false;
        }
        return true;
    }
}