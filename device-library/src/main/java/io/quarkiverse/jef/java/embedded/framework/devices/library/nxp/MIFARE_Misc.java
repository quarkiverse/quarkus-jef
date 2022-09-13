package io.quarkiverse.jef.java.embedded.framework.devices.library.nxp;

public enum MIFARE_Misc {
    MF_ACK(0xA),        // The MIFARE Classic uses a 4 bit ACK/NAK. Any other value than 0xA is NAK.
    MF_KEY_SIZE(6);           // A Mifare Crypto1 key is 6 bytes.

    final byte value;

    MIFARE_Misc(int value) {
        this.value = (byte) value;
    }
}
