package com.bedrockcloud.bedrockcloud.command.defaults;

import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.command.Command;

public class SoftwareCommand extends Command implements Loggable
{
    public SoftwareCommand() {
        super("software", "software <all|pocketmine|waterdogpe>", "Update Proxy/Server softwares");
    }
    
    @Override
    protected void onCommand(final String[] args) {
        if (args.length != 0) {
            final String s = args[0];
            switch (s) {
                case "all": {
                    if (SoftwareManager.download(SoftwareManager.POCKETMINE_URL, "./local/versions/pocketmine/PocketMine-MP.phar")) {
                        this.getLogger().info("PocketMine downloaded");
                    } else {
                        this.getLogger().error("Download server offline!");
                    }
                    if (SoftwareManager.download(SoftwareManager.WATERDOGPE_URL, "./local/versions/waterdogpe/WaterdogPE.jar")) {
                        this.getLogger().info("WaterdogPE downloaded");
                        break;
                    } else {
                        this.getLogger().error("Download server offline!");
                    }
                    break;
                }
                case "pocketmine": {
                    if (args.length != 1) {
                        final String versionId = args[1];
                        if (SoftwareManager.download(SoftwareManager.POCKETMINE_URL, "./local/versions/pocketmine/PocketMine-MP.phar")) {
                            this.getLogger().info("PocketMine downloaded");
                        } else {
                            this.getLogger().error("Download server offline!");
                        }
                        break;
                    }
                    break;
                }
                case "waterdogpe": {
                    if (SoftwareManager.download(SoftwareManager.WATERDOGPE_URL, "./local/versions/waterdogpe/WaterdogPE.jar")) {
                        this.getLogger().info("WaterdogPE downloaded");
                        break;
                    } else {
                        this.getLogger().error("Download server offline!");
                    }
                    break;
                }
            }
        }
    }
}
