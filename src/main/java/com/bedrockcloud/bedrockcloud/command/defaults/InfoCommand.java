package com.bedrockcloud.bedrockcloud.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.command.Command;

public class InfoCommand extends Command implements Loggable
{
    public InfoCommand() {
        super("info", "info", "See cloud informations.");
    }

    @Override
    protected void onCommand(final String[] args) {
        BedrockCloud.printCloudInfos();
    }
}
