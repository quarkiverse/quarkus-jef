package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Optional;

import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiMode;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConvertWith;

@ConfigGroup
public class SPIBusConfig {
    /**
     * Enable bus
     */
    @ConfigItem(defaultValue = "false")
    public Boolean enabled;

    /**
     * Path to SPI bus i.e /dev/spi-1
     */
    @ConfigItem
    public Optional<String> path;

    /**
     * Clock Frequency
     */
    @ConfigItem(name = "clock-frequency", defaultValue = "500000")
    public Integer clockFrequency;

    /**
     * SPI modes:
     * SPI_MODE_0: CPOL = 0, CPHA = 0 mode
     * SPI_MODE_1: CPOL = 0, CPHA = 1 mode
     * SPI_MODE_2: CPOL = 1, CPHA = 0 mode
     * SPI_MODE_3: CPOL = 1, CPHA = 1 mode
     * Default: SPI_MODE_3
     */
    @ConfigItem(name = "spi-mode", defaultValue = "SPI_MODE_3")
    @ConvertWith(SpiModeConverter.class)
    public SpiMode spiMode;

    /**
     * Word length: Default 8 bits
     */
    @ConfigItem(name = "word-length", defaultValue = "8")
    public Integer wordLength;

    /**
     * Bit ordering. Default: BIG_ENDIAN
     */
    @ConfigItem(name = "bit-ordering", defaultValue = "BIG_ENDIAN")
    @ConvertWith(BitOrderingConverter.class)
    public BitOrdering bitOrdering;

    /**
     * Work mode: half or full duplex
     */
    @ConfigItem(name = "work-mode", defaultValue = "FULL_DUPLEX")
    @ConvertWith(SpiWorkModeConverter.class)
    public SpiWorkMode workMode;

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

    public enum SpiWorkMode {
        HALF_DUPLEX,
        FULL_DUPLEX;
    }

}
