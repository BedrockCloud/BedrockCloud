package com.bedrockcloud.bedrockcloud.files;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.PasswordAPI;
import com.bedrockcloud.bedrockcloud.config.Config;
import com.bedrockcloud.bedrockcloud.config.ConfigSection;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.port.PortValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Startfiles implements Loggable
{
    private final ArrayList<String> directorys;
    
    public Startfiles() {
        this.directorys = new ArrayList<String>();
        try {
            this.delete(new File("./temp"));
            this.directorys.add("./templates");
            this.directorys.add("./temp");
            this.directorys.add("./local");
            this.directorys.add("./archive");
            this.directorys.add(this.directorys.get(2) + "/plugins");
            this.directorys.add(this.directorys.get(2) + "/plugins/pocketmine");
            this.directorys.add(this.directorys.get(2) + "/plugins/waterdogpe");
            this.directorys.add(this.directorys.get(2) + "/versions");
            this.directorys.add(this.directorys.get(2) + "/versions/pocketmine");
            this.directorys.add(this.directorys.get(2) + "/versions/waterdogpe");
            this.directorys.add(this.directorys.get(2) + "/notify");
            this.directorys.add(this.directorys.get(3) + "/crashdumps");
            this.directorys.add(this.directorys.get(3) + "/server-pids");
        } catch (NullPointerException e) {
            this.getLogger().exception(e);
        }
        this.checkFolder();
    }
    
    public void deleteFolder(final File file) {
        if (file.isDirectory()) {
            final String[] fileList = file.list();
            if (fileList.length == 0) {
                file.delete();
            }
            else {
                for (final String fileName : fileList) {
                    final String fullPath = file.getPath() + "/" + fileName;
                    final File fileOrFolder = new File(fullPath);
                    this.delete(fileOrFolder);
                }
                this.delete(file);
            }
        }
        else {
            file.delete();
        }
    }
    
    public void delete(final File file) {
        if (file.isDirectory()) {
            final String[] fileList = file.list();
            if (fileList.length == 0) {
                file.delete();
            }
            else {
                for (final String fileName : fileList) {
                    final String fullPath = file.getPath() + "/" + fileName;
                    final File fileOrFolder = new File(fullPath);
                    this.delete(fileOrFolder);
                }
                this.delete(file);
            }
        } else {
            file.delete();
        }
    }
    
    private void checkFolder() {
        try {
            for (final String direc : this.directorys) {
                final File theDir = new File(direc);
                if (!theDir.exists()) {
                    this.getLogger().debug("Creating Folder in the Directory " + direc + "!");
                    theDir.mkdirs();
                }
            }
            final File templatesc = new File("./templates/config.json");
            if (!templatesc.exists()) {
                templatesc.createNewFile();
            }

            final File file = new File("./local/config.json");
            final Config config = new Config(file, Config.JSON);
            config.setAll(new ConfigSection() {
                {
                    put("port", (double)PortValidator.getFreeCloudPort());
                    put("debug-mode", false);
                    put("motd", "Default BedrockCloud Service");
                    put("auto-update-on-start", false);
                    put("wdpe-login-extras", false);
                    put("enable-cloudlog-file", false);
                    put("use-proxy", true);
                    put("auto-restart-cloud", false);
                    put("rest-password", PasswordAPI.generateRandomPassword(8));
                    put("rest-port", 8080);
                    put("rest-username", "cloud");
                    put("rest-enabled", true);
                }
            });
            config.save();

            final File pocketmineFile = new File("./local/versions/pocketmine/PocketMine-MP.phar");
            final File waterdogFile = new File("./local/versions/waterdogpe/WaterdogPE.jar");

            if (!pocketmineFile.exists()) {
                if (SoftwareManager.download(SoftwareManager.POCKETMINE_URL, "./local/versions/pocketmine/PocketMine-MP.phar")) {
                    this.getLogger().debug("PocketMine Downloaded");
                } else {
                    this.getLogger().debug("Download server is offline.");
                }
            }

            if (!waterdogFile.exists()) {
                if (SoftwareManager.download(SoftwareManager.WATERDOGPE_URL, "./local/versions/waterdogpe/WaterdogPE.jar")) {
                    this.getLogger().debug("WaterdogPE downloaded");
                } else {
                    this.getLogger().debug("Download server is offline.");
                }
            }

            final File cloudbridgepmFile = new File("./local/plugins/pocketmine/CloudBridge-PM.phar");
            final File devtools = new File("./local/plugins/pocketmine/DevTools.phar");
            final File cloudbridgewdFile = new File("./local/plugins/waterdogpe/CloudBridge-WD.jar");

            if (!cloudbridgepmFile.exists()) {
                if (SoftwareManager.download(SoftwareManager.CLOUDBRIDGEPM_URL, "./local/plugins/pocketmine/CloudBridge-PM.phar")) {
                    this.getLogger().debug("CloudBridge-PM downloaded");
                } else {
                    this.getLogger().debug("Download server is offline.");
                }
            }

            if (!devtools.exists()) {
                if (SoftwareManager.download(SoftwareManager.DEVTOOLS_URL, "./local/plugins/pocketmine/DevTools.phar")) {
                    this.getLogger().debug("DevTools downloaded");
                } else {
                    this.getLogger().debug("Download server is offline.");
                }
            }

            if (!cloudbridgewdFile.exists()) {
                if (SoftwareManager.download(SoftwareManager.CLOUDBRIDGEWD_URL, "./local/plugins/waterdogpe/CloudBridge-WD.jar")) {
                    this.getLogger().debug("CloudBridge-WD downloaded");
                } else {
                    this.getLogger().debug("Download server is offline.");
                }
            }

            BedrockCloud.getLogger().info("§aPlease wait a short time.");
            Thread.sleep(3000);

            GroupAPI.createGroup("Proxy-Master", 0);
            GroupAPI.createGroup("Lobby", 1);
        } catch (Exception e) {
            this.getLogger().exception(e);
        }
    }
}
