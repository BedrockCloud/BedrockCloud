package com.bedrockcloud.bedrockcloud.command.defaults;

import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.command.Command;
import com.bedrockcloud.bedrockcloud.utils.Utils;

public class InfoCommand extends Command implements Loggable
{
    public InfoCommand() {
        super("info", "info", "See cloud information's.");
    }

    @Override
    protected void onCommand(final String[] args) {
        Utils.printCloudInfos();
    }
}