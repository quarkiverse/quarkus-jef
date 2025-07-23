package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Optional;

import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiMode;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface SPIBusConfig {
    /**
     * Enable bus
     */
    @WithDefault("false")
    Boolean enabled();

    /**
     * Path to SPI bus i.e /dev/spi-1
     */
    Optional<String> path();

    /**
     * Clock Frequency
     */
    @WithDefault("500000")
    Integer clockFrequency();

    /**
     * SPI modes:
     * SPI_MODE_0: CPOL = 0, CPHA = 0 mode
     * SPI_MODE_1: CPOL = 0, CPHA = 1 mode
     * SPI_MODE_2: CPOL = 1, CPHA = 0 mode
     * SPI_MODE_3: CPOL = 1, CPHA = 1 mode
     * Default: SPI_MODE_3
     */
    @WithDefault("SPI_MODE_3")
    @WithConverter(SpiModeConverter.class)
    SpiMode spiMode();

    /**
     * Word length: Default 8 bits
     */
    @WithDefault("8")
    Integer wordLength();

    /**
     * Bit ordering. Default: BIG_ENDIAN
     */
    @WithDefault("BIG_ENDIAN")
    @WithConverter(BitOrderingConverter.class)
    BitOrdering bitOrdering();

    /**
     * Work mode: half or full duplex
     */
    @WithDefault("FULL_DUPLEX")
    @WithConverter(SpiWorkModeConverter.class)
    SpiWorkMode workMode();

    enum BitOrdering {
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

    enum SpiWorkMode {
        HALF_DUPLEX,
        FULL_DUPLEX;
    }

}
