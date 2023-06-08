package com.bedrockcloud.bedrockcloud.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.command.Command;

public class EndCommand extends Command implements Loggable
{
    public EndCommand() {
        super("end", "end", "Stop the cloud.");
    }

    @Override
    protected void onCommand(final String[] args) {
        this.getLogger().info("§cStopped BedrockCloud");
        BedrockCloud.setRunning(false);
        System.exit(0);
    }
}
