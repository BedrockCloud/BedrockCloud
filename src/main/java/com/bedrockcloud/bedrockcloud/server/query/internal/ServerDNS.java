package com.bedrockcloud.bedrockcloud.server.query.internal;

import com.bedrockcloud.bedrockcloud.server.query.api.QueryException;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import java.util.regex.Pattern;

public class ServerDNS {

    private static final String IP4_PATTERN =
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final int DEFAULT_PORT = 25565;
    private static final String SRV_STR = "_minecraft._tcp.";

    private String ipAddress = "";
    private String targetHostName = "";
    private String hostName = "";
    private int port = 0;
    private int queryPort = 0;

    public ServerDNS(String hostName, int port) throws
            QueryException {
        resolve(hostName, port);
    }

    public ServerDNS(String hostName) throws
            QueryException {
        resolve(hostName, 0);
    }

    private void resolve(String hostName, int port)
            throws QueryException {

        this.targetHostName = hostName;
        this.hostName = this.targetHostName;
        this.port = port;

        if (!Pattern.compile(IP4_PATTERN).matcher(this.targetHostName).matches() &&
                this.port == 0) {
            // input is an hostname, but no port submitted, try to resolve via SRV record
            try {
                SRVRecord srvRecord = (SRVRecord) lookupRecord(SRV_STR + hostName, Type.SRV);

                this.targetHostName = srvRecord.getTarget().toString().replaceFirst("\\.$","");
                this.port = srvRecord.getPort();
            } catch (QueryException e) {
                // no SRV record found at the moment, just continue
            }
        }

        if (Pattern.compile(IP4_PATTERN).matcher(this.targetHostName).matches()) {
            // hostname provided is a IP address
            this.ipAddress = this.targetHostName;
        } else {
            // hostname provided is an actual hostname, resolve IP address
            this.ipAddress = ((ARecord) lookupRecord(this.targetHostName, Type.A)).getAddress().getHostAddress();
        }

        if (this.port == 0) {
            // couldn't resolve via SVR record, therefore use default minecraft port
            this.port = DEFAULT_PORT;
        }

        setQueryPort(this.port); // this will raise an exception in case the supplied port is out of range
    }

    private Record lookupRecord(String hostName, int type)
            throws QueryException {

        Record record;
        Lookup lookup;
        int result;

        try {
            lookup = new Lookup(hostName, type);
        } catch (TextParseException e) {           
            throw new QueryException(QueryException.ErrorType.HOST_NOT_FOUND,
                    String.format("Host '%s' parsing error:%s", hostName, e.getMessage()));
        }

        lookup.run();

        result = lookup.getResult();

        if (result == Lookup.SUCCESSFUL) {
            record = lookup.getAnswers()[0];
        } else {
            switch (result) {
                case Lookup.HOST_NOT_FOUND:
                    throw new QueryException(QueryException.ErrorType.HOST_NOT_FOUND,
                            String.format("Host '%s' not found", hostName));
                case Lookup.TYPE_NOT_FOUND:
                    throw new QueryException(String.format("Host '%s' not found (no A record)", hostName));
                case Lookup.UNRECOVERABLE:
                    throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM,
                            String.format("Cannot lookup host '%s'", hostName));
                case Lookup.TRY_AGAIN:
                    throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM,
                            String.format("Temporary failure to lookup host '%s'", hostName));
                default:
                    throw new QueryException(String.format("Unknown error %d in host lookup of '%s'", result, hostName));
            }
        }

        return record;
    }

    private int validatePort(int port)
            throws IllegalArgumentException {
        if (port == 0 || port > Math.pow(2, 16) - 1) {
            throw new IllegalArgumentException(String.format("Port is out of valid range: %d", port));
        }
        return port;
    }

    public String getTargetHostName() {
        return this.targetHostName;
    }

    public String getHostName() {
        return this.hostName;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port)
            throws IllegalArgumentException {
        this.port = validatePort(port);
    }

    public int getQueryPort() {
        return this.queryPort;
    }

    public void setQueryPort(int queryPort)
            throws IllegalArgumentException {
        this.queryPort = validatePort(queryPort);
    }
}
