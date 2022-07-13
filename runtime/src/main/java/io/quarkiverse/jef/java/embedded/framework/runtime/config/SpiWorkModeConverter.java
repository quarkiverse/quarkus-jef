package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.io.Serializable;

import org.eclipse.microprofile.config.spi.Converter;

public class SpiWorkModeConverter implements Converter<SPIBusConfig.SpiWorkMode>, Serializable {
    private static final long serialVersionUID = 5829109830654968883L;

    @Override
    public SPIBusConfig.SpiWorkMode convert(String value) throws IllegalArgumentException, NullPointerException {
        return SPIBusConfig.SpiWorkMode.valueOf(value);
    }
}
