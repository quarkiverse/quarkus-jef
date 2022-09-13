package io.quarkiverse.jef.java.embedded.framework.devices.library.nxp;

public enum PCD_RxGain {
    RxGain_18dB(0x00), // 000b - 18 dB, minimum
    RxGain_23dB(0x01 << 4), // 001b - 23 dB
    RxGain_18dB_2(0x02 << 4), // 010b - 18 dB, it seems 010b is a duplicate for 000b
    RxGain_23dB_2(0x03 << 4), // 011b - 23 dB, it seems 011b is a duplicate for 001b
    RxGain_33dB(0x04 << 4), // 100b - 33 dB, average, and typical default
    RxGain_38dB(0x05 << 4), // 101b - 38 dB
    RxGain_43dB(0x06 << 4), // 110b - 43 dB
    RxGain_48dB(0x07 << 4), // 111b - 48 dB, maximum
    RxGain_min(0x00), // 000b - 18 dB, minimum, convenience for RxGain_18dB
    RxGain_avg(0x04 << 4), // 100b - 33 dB, average, convenience for RxGain_33dB
    RxGain_max(0x07 << 4); // 111b - 48 dB, maximum, convenience for RxGain_48dB

    final byte value;

    PCD_RxGain(int value) {
        this.value = (byte) value;
    }
}
