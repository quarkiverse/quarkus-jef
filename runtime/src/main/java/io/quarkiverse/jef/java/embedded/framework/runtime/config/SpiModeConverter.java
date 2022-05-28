package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import static io.quarkus.runtime.configuration.ConverterSupport.DEFAULT_QUARKUS_CONVERTER_PRIORITY;

import javax.annotation.Priority;

import org.eclipse.microprofile.config.spi.Converter;

import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiMode;

@Priority(DEFAULT_QUARKUS_CONVERTER_PRIORITY)
public class SpiModeConverter implements Converter<SpiMode> {
    @Override
    public SpiMode convert(String s) throws IllegalArgumentException, NullPointerException {
        return SpiMode.valueOf(s);
    }
}
