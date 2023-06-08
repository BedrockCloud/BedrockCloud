package com.bedrockcloud.bedrockcloud.manager;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privategameserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxy.ProxyServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

public class PushPacketManager {

    public static void pushPacket(final DataPacket cloudPacket, final GameServer server) {
        if (server.getServerName() == null || server.getSocket() == null) {
            return;
        }

        if (server.getSocket().isClosed()) {
            BedrockCloud.getLogger().error("CloudPacket cannot be push because socket is closed.");
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(cloudPacket.encode().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        InetAddress address = null;
        try {
            address = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException ignored) {
        }
        int port = server.getServerPort()+1;
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            BedrockCloud.getLogger().exception(ex);
        }
        try {
            assert datagramSocket != null;
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            BedrockCloud.getLogger().exception(ex);
        }
    }

    public static void pushPacket(final DataPacket cloudPacket, final PrivateGameServer server) {
        if (server.getServerName() == null || server.getSocket() == null) {
            return;
        }

        if (server.getSocket().isClosed()) {
            BedrockCloud.getLogger().error("CloudPacket cannot be push because socket is closed.");
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(cloudPacket.encode().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        InetAddress address = null;
        try {
            address = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException ignored) {
        }
        int port = server.getServerPort()+1;
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            BedrockCloud.getLogger().exception(ex);
        }
        try {
            assert datagramSocket != null;
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            BedrockCloud.getLogger().exception(ex);
        }
    }

    public static void pushPacket(final DataPacket cloudPacket, final ProxyServer server) {
        if (server.getServerName() == null || server.getSocket() == null) {
            return;
        }

        if (server.getSocket().isClosed()) {
            BedrockCloud.getLogger().error("CloudPacket cannot be push because socket is closed.");
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(cloudPacket.encode().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        InetAddress address = null;
        try {
            address = InetAddress.getByName("0.0.0.0");
        } catch (UnknownHostException ignored) {
        }
        int port = server.getServerPort()+1;
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            BedrockCloud.getLogger().exception(ex);
        }
        try {
            assert datagramSocket != null;
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            BedrockCloud.getLogger().exception(ex);
        }
    }
}
