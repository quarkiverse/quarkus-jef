package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import static io.quarkus.runtime.configuration.ConverterSupport.DEFAULT_QUARKUS_CONVERTER_PRIORITY;

import java.io.Serializable;

import javax.annotation.Priority;

import org.eclipse.microprofile.config.spi.Converter;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBaudRate;

@Priority(DEFAULT_QUARKUS_CONVERTER_PRIORITY)
public class SerialBaudRateConverter implements Converter<SerialBaudRate>, Serializable {
    private static final long serialVersionUID = 532349386481352009L;

    @Override
    public SerialBaudRate convert(String s) throws IllegalArgumentException, NullPointerException {
        return SerialBaudRate.valueOf(s);
    }
}
