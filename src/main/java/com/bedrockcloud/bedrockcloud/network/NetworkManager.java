package com.bedrockcloud.bedrockcloud.network;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.console.Loggable;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;

import java.io.IOException;
import java.net.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

public class NetworkManager implements Loggable {
    public DatagramSocket datagramSocket;
    public HashMap<String, DatagramPacket> channelList;

    public NetworkManager(final int port) {
        try {
            this.getLogger().info("Listening on 127.0.0.1:" + port);
            this.datagramSocket = new DatagramSocket(port);
            this.datagramSocket.setReuseAddress(true);
            this.channelList = new HashMap<String, DatagramPacket>();
        } catch (IOException e) {
            BedrockCloud.getLogger().exception(e);
        }
    }

    public void start() {
        while (BedrockCloud.isRunning()) {
            if (this.datagramSocket != null && !this.datagramSocket.isClosed()) {
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                    this.datagramSocket.receive(datagramPacket);

                    if (datagramPacket.getLength() > 0) {
                        ClientRequest request = new ClientRequest(datagramPacket, this.datagramSocket);
                        request.start();
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }
}