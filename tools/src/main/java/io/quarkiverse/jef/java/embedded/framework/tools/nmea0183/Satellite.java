package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

public enum Satellite {
    GP("GPS"),
    GL("Glonass"),
    GA("Galileo"),
    BD("Beidou"),
    GN("Misc");

    private final String name;

    Satellite(String name) {
        this.name = name;
    }

}
