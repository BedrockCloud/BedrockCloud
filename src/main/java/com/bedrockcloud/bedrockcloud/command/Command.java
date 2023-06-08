package com.bedrockcloud.bedrockcloud.command;

import com.bedrockcloud.bedrockcloud.console.Loggable;

public class Command implements Loggable
{
    public String cmd;
    public String usage = "";
    public String description = "";
    
    public Command(final String cmd, String usage, String description) {
        this.cmd = cmd;
        this.usage = usage;
        this.description = description;
    }

    public Command(final String cmd, String usage) {
        this.cmd = cmd;
        this.usage = usage;
    }

    public Command(final String cmd) {
        this.cmd = cmd;
    }
    
    protected void onCommand(final String[] args) {
    }
    
    public String getCommand() {
        return this.cmd;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }
}
