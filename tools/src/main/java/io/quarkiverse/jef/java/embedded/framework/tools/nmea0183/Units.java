package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

public enum Units {
    M("meters"),
    N("knots"),
    K("kilometers");

    private final String value;

    Units(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Units{" +
                "value='" + value + '\'' +
                '}';
    }
}
