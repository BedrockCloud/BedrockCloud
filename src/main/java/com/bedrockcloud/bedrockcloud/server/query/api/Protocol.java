package com.bedrockcloud.bedrockcloud.server.query.api;

/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public enum Protocol {
    UDP_BASIC(1),           // basic information query via UDP
    UDP_FULL(2);            // full information query via UDP

    private final int value;

    Protocol(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}