package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

public enum ReliabilityOfReceivedCoordinates {
    A("data is reliable"),
    V("erroneous data");

    private final String value;

    ReliabilityOfReceivedCoordinates(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ReliabilityOfReceivedCoordinates{" +
                "value='" + value + '\'' +
                '}';
    }
}