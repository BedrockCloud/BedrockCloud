package com.bedrockcloud.bedrockcloud;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.net.URL;

public class SoftwareManager
{

    public final static String POCKETMINE_URL = "https://github.com/pmmp/PocketMine-MP/releases/latest/download/PocketMine-MP.phar";
    public final static String WATERDOGPE_URL = "https://github.com/WaterdogPE/WaterdogPE/releases/latest/download/Waterdog.jar";
    public final static String CLOUDBRIDGEPM_URL = "https://github.com/BedrockCloud/CloudBridge-PM/releases/latest/download/CloudBridge.phar";
    public final static String DEVTOOLS_URL = "https://poggit.pmmp.io/r/199538/PocketMine-DevTools_dev-217.phar";
    public final static String CLOUDBRIDGEWD_URL = "https://github.com/BedrockCloud/CloudBridge-WD/releases/latest/download/BedrockProxyCore.jar";

    public static boolean download(final String url, final String destinationPath) {
        try {
            final BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
            try {
                final FileOutputStream fileOS = new FileOutputStream(destinationPath);
                try {
                    final byte[] data = new byte[1024];
                    int byteContent;
                    while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                        fileOS.write(data, 0, byteContent);
                    }
                    fileOS.close();
                }
                catch (Throwable t) {
                    try {
                        fileOS.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                    throw t;
                }
                inputStream.close();
            }
            catch (Throwable t2) {
                try {
                    inputStream.close();
                }
                catch (Throwable exception2) {
                    t2.addSuppressed(exception2);
                }
                throw t2;
            }
        }
        catch (IOException e) {
            BedrockCloud.getLogger().exception(e);
            return false;
        }
        return true;
    }
}
