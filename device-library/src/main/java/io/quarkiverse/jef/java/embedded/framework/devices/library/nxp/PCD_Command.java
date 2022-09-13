package io.quarkiverse.jef.java.embedded.framework.devices.library.nxp;

public enum PCD_Command {
    PCD_Idle(0x00), // no action, cancels current command execution
    PCD_Mem(0x01), // stores 25 bytes into the internal buffer
    PCD_GenerateRandomID(0x02), // generates a 10-byte random ID number
    PCD_CalcCRC(0x03), // activates the CRC coprocessor or performs a self-test
    PCD_Transmit(0x04), // transmits data from the FIFO buffer
    PCD_NoCmdChange(0x07), // no command change, can be used to modify the CommandReg register bits without affecting the command, for example, the PowerDown bit
    PCD_Receive(0x08), // activates the receiver circuits
    PCD_Transceive(0x0C), // transmits data from FIFO buffer to antenna and automatically activates the receiver after transmission
    PCD_MFAuthent(0x0E), // performs the MIFARE standard authentication as a reader
    PCD_SoftReset(0x0F); // resets the MFRC522

    final byte value;

    PCD_Command(int value) {
        this.value = (byte) value;
    }
}
