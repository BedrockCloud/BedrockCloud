package com.bedrockcloud.bedrockcloud.server.query.api;

import com.bedrockcloud.bedrockcloud.server.query.internal.QueryStatusUdp;
import com.bedrockcloud.bedrockcloud.server.query.internal.ServerDNS;

public final class QueryStatus {

    private final ServerDNS serverDNS;
    private final Protocol protocol;
    private final int timeOut;

    private Status status;

    private QueryStatus(Builder builder) 
            throws QueryException {
        this.serverDNS = new ServerDNS(builder.hostName, builder.port);
        this.timeOut = builder.timeOut;
        this.protocol = builder.protocol;
    }

    /**
     * Return last queried status of the Minecraft server without invoking a refresh
     * (at least if loaded already once)
     * 
     * @return Status of the Minecraft Server
     */
    public Status getStatus() 
            throws QueryException {
        
        if (this.status != null)
            return this.status;

        return refreshStatus();
    }

    /**
     * Queried status of the Minecraft server
     * 
     * @return Status of the Minecraft Server
     */
    public Status refreshStatus() 
            throws QueryException {
        
        switch(this.protocol) {
            case UDP_BASIC:
            case UDP_FULL:
                this.status = new QueryStatusUdp(this.protocol, this.serverDNS, this.timeOut).getStatus();
                break;
            default:
                break;
        }
        return this.status;
    }


    /**
     * Builder class to get an instance of StatusQuery 
     *
     * @author Patrick Weiss <info@tekgator.com>
     * @see QueryStatus
     */
    public static final class Builder {
        private final String hostName;
        private int port = 0;
        private int timeOut = 1000;
        private Protocol protocol = Protocol.UDP_FULL;
       
        /**
         * Constructor of the builder class
         * 
         * @param hostName or ip address of the Minecraft Server
         * 
         */
        public Builder(String hostName) {
            this.hostName = hostName;
        }

        /**
         * Set port of the minecraft server for the query. If not ommited it will be loaded via SRV 
         * record and in case no SRV revord is found the default Minecraft port (=25565) is used 
         * 
         * @param port of the Minecraft Server
         * 
         */
        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        /**
         * Set protocol to be used for the Query
         * 
         * @param protocol used Minecraft Protocol version for invoking the query
         * @see Protocol
         */
        public Builder setProtocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        /**
         * Use Minecraft Protocol UDP basic for Query (method can be 
         * used instead setProtocol to drop dependency of Protocol enum
         */
        public Builder setProtocolUdpBasic() {
            this.protocol = Protocol.UDP_BASIC;
            return this;
        }


        /**
         * Use Minecraft Protocol UDP full for Query (method can be 
         * used instead setProtocol to drop dependency of Protocol enum
         */
        public Builder setProtocolUdpFull() {
            this.protocol = Protocol.UDP_FULL;
            return this;
        }        

        /**
         * Set socket timeout
         * @param timeOut in MS
         */
        public Builder setTimeout(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @return New instance of the QueryStatus class
         */
        public QueryStatus build() throws QueryException {
            return new QueryStatus(this);
        }     
    }
}