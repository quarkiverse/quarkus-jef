package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import static io.quarkus.runtime.configuration.ConverterSupport.DEFAULT_QUARKUS_CONVERTER_PRIORITY;

import javax.annotation.Priority;

import org.eclipse.microprofile.config.spi.Converter;

@Priority(DEFAULT_QUARKUS_CONVERTER_PRIORITY)
public class BitOrderingConverter implements Converter<SPIBusConfig.BitOrdering> {
    @Override
    public SPIBusConfig.BitOrdering convert(String s) throws IllegalArgumentException, NullPointerException {
        return SPIBusConfig.BitOrdering.valueOf(s);
    }
}
