package io.quarkiverse.jef.java.embedded.framework.devices.library.nxp;

public enum StatusCode {
    STATUS_OK(0),    // Success.
    STATUS_ERROR(1),    // Error in communication.
    STATUS_COLLISION(2),    // Collision detected.
    STATUS_TIMEOUT(3),    // Timeout in communication.
    STATUS_NO_ROOM(4),    // A buffer is not big enough.
    STATUS_INTERNAL_ERROR(5),    // Internal error in the code. Should not happen ;-)
    STATUS_INVALID(6),    // Invalid argument.
    STATUS_CRC_WRONG(7),    // The CRC_A does not match.
    STATUS_UNKNOWN(8),
    STATUS_MIFARE_NACK(0xff);    // A MIFARE PICC responded with NAK.;

    final byte value;

    StatusCode(int value) {
        this.value = (byte) value;
    }
}
