package com.bedrockcloud.bedrockcloud.console;

public interface Loggable
{
    default Logger getLogger() {
        return new Logger();
    }
}
