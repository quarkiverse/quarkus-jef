package io.quarkiverse.jef.java.embedded.framework.devices.library.rohm.bh750fvi;

public enum Resolution {
    BH1750_CONTINUOUS_HIGH_RES_MODE(0x10), //continuous measurement, 1.0 lx resolution
    BH1750_CONTINUOUS_HIGH_RES_MODE_2(0x11), //continuous measurement, 0.5 lx resolution
    BH1750_CONTINUOUS_LOW_RES_MODE(0x13), //continuous measurement, 4.0 lx resolution

    BH1750_ONE_TIME_HIGH_RES_MODE(0x20), //one measurement & power down, 1.0 lx resolution
    BH1750_ONE_TIME_HIGH_RES_MODE_2(0x21), //one measurement & power down, 0.5 lx resolution
    BH1750_ONE_TIME_LOW_RES_MODE(0x23); //one measurement & power down, 4.0 lx resolution

    private byte value;

    Resolution(int value) {
        this.value = (byte) value;
    }

    byte getValue() {
        return value;
    }
}
