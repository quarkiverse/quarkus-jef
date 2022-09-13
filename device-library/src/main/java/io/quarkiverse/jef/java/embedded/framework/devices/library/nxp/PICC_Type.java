package io.quarkiverse.jef.java.embedded.framework.devices.library.nxp;

public enum PICC_Type {
    PICC_TYPE_UNKNOWN(0),
    PICC_TYPE_ISO_14443_4(1), // PICC compliant with ISO/IEC 14443-4.
    PICC_TYPE_ISO_18092(2), // PICC compliant with ISO/IEC 18092 (NFC).
    PICC_TYPE_MIFARE_MINI(3), // MIFARE Classic protocol, 320 bytes.
    PICC_TYPE_MIFARE_1K(4), // MIFARE Classic protocol, 1KB.
    PICC_TYPE_MIFARE_4K(5), // MIFARE Classic protocol, 4KB.
    PICC_TYPE_MIFARE_UL(6), // MIFARE Ultralight or Ultralight C.
    PICC_TYPE_MIFARE_PLUS(7), // MIFARE Plus.
    PICC_TYPE_MIFARE_DESFIRE(8), // MIFARE DESFire.
    PICC_TYPE_TNP3XXX(9), // Only mentioned in NXP AN 10833 MIFARE Type Identification Procedure.
    PICC_TYPE_NOT_COMPLETE(0xff); // SAK indicates UID is not complete.

    final byte value;

    PICC_Type(int value) {
        this.value = (byte) value;
    }
}
