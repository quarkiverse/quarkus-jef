package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

public enum CoordinateCalculationMethod {
    A("Autonomous"),
    D("Differential"),
    E("Approximation"),
    M("Fixed"),
    N("False data"),
    NA("Not applicable");

    private final String value;

    CoordinateCalculationMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CoordinateCalculationMethod{" +
                "value='" + value + '\'' +
                '}';
    }
}