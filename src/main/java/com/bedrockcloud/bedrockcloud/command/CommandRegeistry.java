package com.bedrockcloud.bedrockcloud.command;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.command.defaults.*;

public class CommandRegeistry {

    public static void registerCommands() {
        BedrockCloud.commandManager.addCommand(new HelpCommand());
        BedrockCloud.commandManager.addCommand(new ServerCommand());
        BedrockCloud.commandManager.addCommand(new TemplateCommand());
        BedrockCloud.commandManager.addCommand(new SoftwareCommand());
        BedrockCloud.commandManager.addCommand(new PlayerCommand());
        BedrockCloud.commandManager.addCommand(new EndCommand());
        BedrockCloud.commandManager.addCommand(new InfoCommand());
    }
}
