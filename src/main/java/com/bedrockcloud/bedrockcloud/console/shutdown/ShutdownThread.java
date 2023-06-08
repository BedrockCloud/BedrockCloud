package com.bedrockcloud.bedrockcloud.console.shutdown;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.manager.ShutdownManager;

public class ShutdownThread extends Thread {

    @Override
    public synchronized void start() {
        ShutdownManager.shutdown();
    }
}
