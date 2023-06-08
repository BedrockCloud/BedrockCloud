package com.bedrockcloud.bedrockcloud.server.query.internal;

import com.bedrockcloud.bedrockcloud.server.query.api.Protocol;
import com.bedrockcloud.bedrockcloud.server.query.api.QueryException;
import com.bedrockcloud.bedrockcloud.server.query.api.Status;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class StatusBuilder {

    public static final String JSON_SERVER = "server";
    public static final String JSON_SERVER_HOSTNAME = "hostname";
    public static final String JSON_SERVER_TARGETHOSTNAME = "targethostname";
    public static final String JSON_SERVER_IPADDRESS = "ipaddress";
    public static final String JSON_SERVER_PORT = "port";
    public static final String JSON_SERVER_QUERYPORT = "queryport";
    public static final String JSON_SERVER_LATENCY = "latency";

    public static final String JSON_DESCRIPTION = "description";

    public static final String JSON_VERSION = "version";
    public static final String JSON_VERSION_NAME = "name";

    public static final String JSON_PLAYERS = "players";
    public static final String JSON_PLAYERS_MAX = "max";
    public static final String JSON_PLAYERS_ONLINE = "online";
    public static final String JSON_PLAYERS_SAMPLE = "sample";
    public static final String JSON_PLAYERS_SAMPLE_NAME = "name";

    public static final String JSON_MODINFO = "modinfo";
    public static final String JSON_MODINFO_TYPE = "type";
    public static final String JSON_MODINFO_MODLIST = "modList";
    public static final String JSON_MODINFO_MODLIST_MODID = "modid";

    public static final String JSON_GAMETYPE = "gametype";

    public static final String JSON_MAP = "map";

    public static final String JSON_FAVICON = "favicon";

    
    private Protocol protocol;
    private ServerDNS serverDNS;
    private int latency;
    private String dataTcp;
    private byte[] dataUdp;

    public StatusBuilder setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public StatusBuilder setServerDNS(ServerDNS serverDNS) {
        this.serverDNS = serverDNS;
        return this;
    }

    public StatusBuilder setLatency(int latency) {
        this.latency = latency;
        return this;
    }

    public StatusBuilder setData(String data) {
        this.dataTcp = data;
        return this;
    }
 
    public StatusBuilder setData(byte[] data) {
        this.dataUdp = data;
        return this;
    }

    public Status build()
            throws QueryException {
        Status status;
        
        switch (this.protocol) {
            case UDP_BASIC:
                status =  buildUdpBasic();
                break;
            case UDP_FULL:
                status =  buildUdpFull();
                break;
            default:
                status = new Status();
                break;

        }

        return status;
    }

    private Status buildUdpBasic ()
            throws QueryException {

        JsonObject json = new JsonObject();

        ByteArrayInputStream b = new ByteArrayInputStream(this.dataUdp);
        DataInputStream d = new DataInputStream(b);

        json.add(JSON_VERSION, new JsonObject());
        json.add(JSON_PLAYERS, new JsonObject());

        for (int i = 1; i <= 5; i++) {
            switch (i) {
                case 1:
                    json.addProperty(JSON_DESCRIPTION, readNullTerminatedString(d));
                    break;
                case 2:
                    json.addProperty(JSON_GAMETYPE, readNullTerminatedString(d));
                    break;
                case 3:
                    json.addProperty(JSON_MAP, readNullTerminatedString(d));
                    break;
                case 4:
                case 5:
                    try {
                        json.get(JSON_PLAYERS).getAsJsonObject().
                                addProperty(i == 4 ? JSON_PLAYERS_ONLINE : JSON_PLAYERS_MAX, Integer.parseInt(readNullTerminatedString(d)));
                    } catch (NumberFormatException e) {
                        // invalid number, just ignore
                    }
                    break;
                default:
                    break;
            }
        }

        addHostInfoToJson(json);

        return new Gson().fromJson(json, Status.class);
    }

    private Status buildUdpFull ()
            throws QueryException {

        JsonObject json = new JsonObject();
        String key;
        String value;

        ByteArrayInputStream b = new ByteArrayInputStream(this.dataUdp);
        DataInputStream d = new DataInputStream(b);

        json.add(JSON_VERSION, new JsonObject());
        json.add(JSON_PLAYERS, new JsonObject());
        json.add(JSON_MODINFO, new JsonObject());

        while(b.available() > 0) {
            key = readNullTerminatedString(d);
            value = readNullTerminatedString(d);

            if (key.isEmpty() && value.equalsIgnoreCase("player_")) {
                byte[] streamRest = new byte[b.available()];

                try {
                    d.read(streamRest);
                } catch (IOException e) {
                    throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE,
                            "Server returned invalid response!");
                }

                readUdpPlayers(json, new String(streamRest));
            } else {
                if (key.equalsIgnoreCase("hostname")) {
                    json.addProperty(JSON_DESCRIPTION, value);
                } else if (key.equalsIgnoreCase("gametype")) {
                    json.addProperty(JSON_GAMETYPE, value);
                } else if (key.equalsIgnoreCase("version")) {
                    json.get(JSON_VERSION).getAsJsonObject().addProperty(JSON_VERSION_NAME, value);
                } else if (key.equalsIgnoreCase("plugins")) {
                    readUdpModInfo(json, value);
                } else if (key.equalsIgnoreCase("map")) {
                    json.addProperty(JSON_MAP, value);
                } else if (key.equalsIgnoreCase("numplayers") ||
                        key.equalsIgnoreCase("maxplayers")) {
                    try {
                        json.get(JSON_PLAYERS).getAsJsonObject().
                                addProperty(key.equalsIgnoreCase("numplayers") ? JSON_PLAYERS_ONLINE : JSON_PLAYERS_MAX,
                                        Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        // invalid number, just ignore
                    }
                } else if (key.equalsIgnoreCase("hostport")) {
                    try {
                        serverDNS.setPort(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        // invalid number, just ignore
                    }
                }
            }
        }

        addHostInfoToJson(json);

        return new Gson().fromJson(json, Status.class);
    }

    private void readUdpModInfo (
            JsonObject json,
            String plugins) {

        int colonPos = plugins.indexOf(":");

        if (colonPos > 0) {
            json.get(JSON_MODINFO).getAsJsonObject().addProperty(JSON_MODINFO_TYPE, plugins.substring(0, colonPos).trim());
            plugins = plugins.substring(colonPos + 1).trim();

            String splitStr[] = new String(plugins).split(";");

            JsonArray jsonModArray = new JsonArray();

            for (int i = 0; i < splitStr.length; i++) {
                JsonObject jsonMod = new JsonObject();
                jsonMod.addProperty(JSON_MODINFO_MODLIST_MODID, splitStr[i].trim());
                jsonModArray.add(jsonMod);
            }

            json.get(JSON_MODINFO).getAsJsonObject().add(JSON_MODINFO_MODLIST, jsonModArray);
        }
    }

    private void readUdpPlayers(
            JsonObject json,
            String players) {

        JsonArray jsonPlayerArray = new JsonArray();
        String playerName;

        String splitStr[] = new String(players).split("\0");
        for (int i = 0; i < splitStr.length; i++) {
            playerName = splitStr[i].trim();
            if (playerName.length() > 0) {
                JsonObject jsonPlayer = new JsonObject();
                jsonPlayer.addProperty(JSON_PLAYERS_SAMPLE_NAME, playerName);
                jsonPlayerArray.add(jsonPlayer);
            }
        }

        json.get(JSON_PLAYERS).getAsJsonObject().add(JSON_PLAYERS_SAMPLE, jsonPlayerArray);
    }

    private String readNullTerminatedString(
            DataInputStream dataInputStream)
            throws QueryException {

        byte byteRead;
        byte[] tmpData;

        try {
            tmpData = new byte[dataInputStream.available()];
            for (int i = 0; (byteRead = dataInputStream.readByte()) != 0; i++) {
                tmpData[i] = byteRead;
            }
        } catch (IOException e) {
            throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE,
                    "Server returned invalid response!");
        }

        return new String(tmpData).trim();
    }

    private void addHostInfoToJson (
            JsonObject json) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(JSON_SERVER_TARGETHOSTNAME, this.serverDNS.getTargetHostName());
        jsonObject.addProperty(JSON_SERVER_HOSTNAME, this.serverDNS.getHostName());
        jsonObject.addProperty(JSON_SERVER_IPADDRESS, this.serverDNS.getIpAddress());
        jsonObject.addProperty(JSON_SERVER_PORT, this.serverDNS.getPort());
        jsonObject.addProperty(JSON_SERVER_QUERYPORT, this.serverDNS.getQueryPort());
        jsonObject.addProperty(JSON_SERVER_LATENCY, this.latency);

        json.add(JSON_SERVER, jsonObject);
    }
}

