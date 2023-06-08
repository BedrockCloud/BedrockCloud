package com.bedrockcloud.bedrockcloud.network.client;

import java.io.*;
import com.bedrockcloud.bedrockcloud.BedrockCloud;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientRequest extends Thread implements AutoCloseable {
    private final DatagramPacket datagramPacket;
    private final DatagramSocket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public ClientRequest(final DatagramPacket datagramPacket, final DatagramSocket socket) {
        this.datagramPacket = datagramPacket;
        this.socket = socket;
        this.dataInputStream = new DataInputStream(new ByteArrayInputStream(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength()));
        this.dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
    }

    public DatagramPacket getDatagramPacket() {
        return this.datagramPacket;
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    @Override
    public void run() {
        String line = null;
        try {
            line = this.dataInputStream.readLine();
            if (line == null) {
                return;
            }
            try {
                BedrockCloud.getPacketHandler().handleCloudPacket(BedrockCloud.getPacketHandler().handleJsonObject(BedrockCloud.getPacketHandler().getPacketNameByRequest(line), line), this);
            } catch (NullPointerException ex) {
                BedrockCloud.getLogger().exception(ex);
            }
        } catch (NullPointerException | IOException ex1) {
            BedrockCloud.getLogger().exception(ex1);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            this.dataOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException ignored) {}
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket responsePacket = new DatagramPacket(data, data.length, this.datagramPacket.getAddress(), this.datagramPacket.getPort());
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(responsePacket);
        } catch (IOException e) {
            BedrockCloud.getLogger().exception(e);
        }
    }

    @Override
    public void close() throws Exception {
        this.dataOutputStream.close();
        this.dataInputStream.close();
    }
}