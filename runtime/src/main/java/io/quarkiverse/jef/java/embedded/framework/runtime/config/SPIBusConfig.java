package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiMode;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConvertWith;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.Optional;

public class SPIBusConfig {
    /**
     * Enable bus
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;

    /**
     * Path to SPI bus i.e /dev/spi-1
     */
    @ConfigItem
    public Optional<String> path;

    /**
     * Clock Frequency
     */
    @ConfigItem(name="clock-frequency", defaultValue = "500000")
    public int clockFrequency;

    /**
     * SPI modes:
     * SPI_MODE_0: CPOL = 0, CPHA = 0 mode
     * SPI_MODE_1: CPOL = 0, CPHA = 1 mode
     * SPI_MODE_2: CPOL = 1, CPHA = 0 mode
     * SPI_MODE_3: CPOL = 1, CPHA = 1 mode
     * Default: SPI_MODE_3
     */
    @ConfigItem(name="spi-mode", defaultValue = "SPI_MODE_3")
    @ConvertWith(SpiModeConverter.class)
    public SpiMode spiMode;

    /**
     * Word length: Default 8 bits
     */
    @ConfigItem(name="word-length", defaultValue = "8")
    public int wordLength;

    /**
     * Bit ordering. Default: BIG_ENDIAN
     */
    @ConfigItem(name="bit-ordering", defaultValue = "BIG_ENDIAN")
    @ConvertWith(BitOrderingConverter.class)
    public BitOrdering bitOrdering;
    public enum BitOrdering {
        BIG_ENDIAN(0),
        LITTLE_ENDIAN(1);

        private final int value;

        BitOrdering(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class SpiModeConverter implements Converter<SpiMode> {
        @Override
        public SpiMode convert(String s) throws IllegalArgumentException, NullPointerException {
            return SpiMode.valueOf(s);
        }
    }

    public static class BitOrderingConverter implements Converter<BitOrdering> {
        @Override
        public BitOrdering convert(String s) throws IllegalArgumentException, NullPointerException {
            return BitOrdering.valueOf(s);
        }
    }
}
