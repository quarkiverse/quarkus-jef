package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

public interface Nmea0183Listener {
    default void handle(DHVRecord record) {
    }

    default void handle(GGARecord record) {
    }

    default void handle(GLLRecord record) {
    }

    default void handle(GSARecord record) {
    }

    default void handle(GSTRecord record) {
    }

    default void handle(GSVRecord record) {
    }

    default void handle(RMCRecord record) {
    }

    default void handle(TXTRecord record) {
    }

    default void handle(VTGRecord record) {
    }

    default void handle(ZDARecord record) {
    }
}
