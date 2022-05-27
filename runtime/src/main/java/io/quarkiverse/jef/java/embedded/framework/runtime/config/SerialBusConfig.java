package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBaudRate;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConvertWith;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.Optional;

public class SerialBusConfig {
    /**
     * Enable bus
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;

    /**
     * Path to I2C bus i.e /dev/something
     */
    @ConfigItem
    public Optional<String> path;


    @ConfigItem(name="baud-rate", defaultValue = "B9600")
    @ConvertWith(SerialBaudRateConverter.class)
    public SerialBaudRate baudRate;

    public static class SerialBaudRateConverter  implements Converter<SerialBaudRate> {
        @Override
        public SerialBaudRate convert(String s) throws IllegalArgumentException, NullPointerException {
            return SerialBaudRate.valueOf(s);
        }
    }

}
