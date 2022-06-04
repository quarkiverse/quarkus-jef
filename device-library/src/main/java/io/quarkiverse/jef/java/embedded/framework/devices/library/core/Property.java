package io.quarkiverse.jef.java.embedded.framework.devices.library.core;

public interface Property {
    String name();

    PropertyType type();

    Object value();

    void setValue(Object value);

    int getOrder();

    boolean isReadOnly();

    boolean isWriteOnly();

    boolean isReadWrite();
}
