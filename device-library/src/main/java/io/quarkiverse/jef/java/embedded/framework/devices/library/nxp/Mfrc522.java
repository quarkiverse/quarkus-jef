package io.quarkiverse.jef.java.embedded.framework.devices.library.nxp;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.IntReference;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.LongReference;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiInputParams;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static io.quarkiverse.jef.java.embedded.framework.devices.library.nxp.PCD_Command.*;
import static io.quarkiverse.jef.java.embedded.framework.devices.library.nxp.PCD_Register.*;
import static io.quarkiverse.jef.java.embedded.framework.devices.library.nxp.PICC_Command.*;
import static io.quarkiverse.jef.java.embedded.framework.devices.library.nxp.PICC_Type.*;
import static io.quarkiverse.jef.java.embedded.framework.devices.library.nxp.StatusCode.*;

// https://www.arduinolibraries.info/libraries/mfrc522
// https://github.com/OSSLibraries/Arduino_MFRC522v2/blob/master/src/MFRC522DriverSPI.cpp
// https://github.com/paguz/RPi-RFID


public class Mfrc522 {

    // Version 0.0 (0x90)
    // Philips Semiconductors; Preliminary Specification Revision 2.0 - 01 August 2005; 16.1 self-test
    private final static int[] MFRC522_firmware_referenceV0_0 = new int[]{
            0x00, 0x87, 0x98, 0x0f, 0x49, 0xFF, 0x07, 0x19,
            0xBF, 0x22, 0x30, 0x49, 0x59, 0x63, 0xAD, 0xCA,
            0x7F, 0xE3, 0x4E, 0x03, 0x5C, 0x4E, 0x49, 0x50,
            0x47, 0x9A, 0x37, 0x61, 0xE7, 0xE2, 0xC6, 0x2E,
            0x75, 0x5A, 0xED, 0x04, 0x3D, 0x02, 0x4B, 0x78,
            0x32, 0xFF, 0x58, 0x3B, 0x7C, 0xE9, 0x00, 0x94,
            0xB4, 0x4A, 0x59, 0x5B, 0xFD, 0xC9, 0x29, 0xDF,
            0x35, 0x96, 0x98, 0x9E, 0x4F, 0x30, 0x32, 0x8D
    };

    // Version 1.0 (0x91)
    // NXP Semiconductors; Rev. 3.8 - 17 September 2014; 16.1.1 self-test
    private final static int[] MFRC522_firmware_referenceV1_0 = {
            0x00, 0xC6, 0x37, 0xD5, 0x32, 0xB7, 0x57, 0x5C,
            0xC2, 0xD8, 0x7C, 0x4D, 0xD9, 0x70, 0xC7, 0x73,
            0x10, 0xE6, 0xD2, 0xAA, 0x5E, 0xA1, 0x3E, 0x5A,
            0x14, 0xAF, 0x30, 0x61, 0xC9, 0x70, 0xDB, 0x2E,
            0x64, 0x22, 0x72, 0xB5, 0xBD, 0x65, 0xF4, 0xEC,
            0x22, 0xBC, 0xD3, 0x72, 0x35, 0xCD, 0xAA, 0x41,
            0x1F, 0xA7, 0xF3, 0x53, 0x14, 0xDE, 0x7E, 0x02,
            0xD9, 0x0F, 0xB5, 0x5E, 0x25, 0x1D, 0x29, 0x79
    };

    // Version 2.0 (0x92)
    // NXP Semiconductors; Rev. 3.8 - 17 September 2014; 16.1.1 self-test
    private final static int[] MFRC522_firmware_referenceV2_0 = {
            0x00, 0xEB, 0x66, 0xBA, 0x57, 0xBF, 0x23, 0x95,
            0xD0, 0xE3, 0x0D, 0x3D, 0x27, 0x89, 0x5C, 0xDE,
            0x9D, 0x3B, 0xA7, 0x00, 0x21, 0x5B, 0x89, 0x82,
            0x51, 0x3A, 0xEB, 0x02, 0x0C, 0xA5, 0x00, 0x49,
            0x7C, 0x84, 0x4D, 0xB3, 0xCC, 0xD2, 0x1B, 0x81,
            0x5D, 0x48, 0x76, 0xD5, 0x71, 0x61, 0x21, 0xA9,
            0x86, 0x96, 0x83, 0x38, 0xCF, 0x9D, 0x5B, 0x6D,
            0xDC, 0x15, 0xBA, 0x3E, 0x7D, 0x95, 0x3B, 0x2F
    };

    // Clone
    // Fudan Semiconductor FM17522 (0x88)
    private final static int[] FM17522_firmware_reference = {
            0x00, 0xD6, 0x78, 0x8C, 0xE2, 0xAA, 0x0C, 0x18,
            0x2A, 0xB8, 0x7A, 0x7F, 0xD3, 0x6A, 0xCF, 0x0B,
            0xB1, 0x37, 0x63, 0x4B, 0x69, 0xAE, 0x91, 0xC7,
            0xC3, 0x97, 0xAE, 0x77, 0xF4, 0x37, 0xD7, 0x9B,
            0x7C, 0xF5, 0x3C, 0x11, 0x8F, 0x15, 0xC3, 0xD7,
            0xC1, 0x5B, 0x00, 0x2A, 0xD0, 0x75, 0xDE, 0x9E,
            0x51, 0x64, 0xAB, 0x3E, 0xE9, 0x15, 0xB5, 0xAB,
            0x56, 0x9A, 0x98, 0x82, 0x26, 0xEA, 0x2A, 0x62
    };

    private static final byte FIFO_SIZE = 64;        // The FIFO is 64 bytes.
    private final SpiBus spi;

    private Uid uid;

    byte _chipSelectPin;        // Arduino pin connected to MFRC522's SPI slave select input (Pin 24, NSS, active low)
    byte _resetPowerDownPin;    // Arduino pin connected to MFRC522's reset and power down input (Pin 6, NRSTPD, active low)


    public Mfrc522(SpiBus spi) {
        this.spi = spi;
        this.uid = new Uid();
    }

    /**
     * @param reg   The register to write to. One of the PCD_Register enums.
     * @param value The value to write.
     */
    void PCD_WriteRegister(PCD_Register reg, byte value) throws NativeIOException {
        spi.writeByteData(
                SpiInputParams
                        .allocate(2)
                        .put((byte) (reg.value & 0x7E))
                        .put(value)
        );
    }

    /**
     * @param reg    The register to write to. One of the PCD_Register enums.
     * @param count  The number of bytes to write to the register
     * @param values The values to write. Byte array.
     */
    void PCD_WriteRegister(PCD_Register reg, int count, byte[] values) throws NativeIOException {
        for (byte index = 0; index < count; index++) {
            PCD_WriteRegister(reg, values[index]);
        }
    }

    /**
     * The register to read from. One of the PCD_Register enums.
     */
    byte PCD_ReadRegister(PCD_Register reg) throws IOException {
        byte value = (byte) (0x80 | ((reg.value) & 0x7E));
        return (byte) spi.readByteData(SpiInputParams
                .allocate(1)
                .put(value));
    }

    /**
     * Reads a number of bytes from the specified register in the MFRC522 chip.
     * The interface is described in the datasheet section 8.1.2.
     *
     * @param reg     The register to read from. One of the PCD_Register enums.
     * @param count   The number of bytes to read
     * @param values  Byte array to store the values in.
     * @param rxAlign Only bit positions rxAlign..7 in values[0] are updated.
     */
    void PCD_ReadRegister(PCD_Register reg, int count, byte[] values, int rxAlign) throws IOException {
        if (count == 0) {
            return;
        }
        //Serial.print(F("Reading ")); 	Serial.print(count); Serial.println(F(" bytes from register."));
        byte address = (byte) (0x80 | (reg.value & 0x7E));        // MSB == 1 is for reading. LSB is not used in address. Datasheet section 8.1.2.3.
        byte index = 0;                            // Index in values array.
        count--;                                // One read is performed outside of the loop

        SpiInputParams addr = SpiInputParams.allocate(1).put(address);
        spi.writeByteData(addr);

        while (index < count) {
            if (index == 0 && rxAlign > 0) {        // Only update bit positions rxAlign..7 in values[0]
                // Create bit mask for bit positions rxAlign..7
                byte mask = 0;
                for (int i = rxAlign; i <= 7; i++) {
                    mask |= (1 << i);
                }
                // Read value and tell that we want to read the same address again.
                byte value = (byte) spi.readByteData(addr);
                // Apply mask to both current value of values[0] and the new data in value.
                values[0] = (byte) ((values[index] & ~mask) | (value & mask));
            } else { // Normal case
                values[index] = (byte) spi.readByteData(addr);
            }
            index++;
        }
        values[index] = (byte) spi.readByteData(SpiInputParams.allocate(0));            // Read the final byte. Send 0 to stop reading.
    } // End PCD_ReadRegister()


    /**
     * @param reg  The register to update. One of the PCD_Register enums.
     * @param mask The bits to set.
     */
    void PCD_SetRegisterBitMask(PCD_Register reg, byte mask) throws IOException {
        byte tmp;
        tmp = PCD_ReadRegister(reg);
        PCD_WriteRegister(reg, (byte) (tmp | mask));            // set bit mask
    }

    /**
     * @param reg  The register to update. One of the PCD_Register enums.
     * @param mask The bits to clear.
     */
    void PCD_ClearRegisterBitMask(PCD_Register reg, byte mask) throws IOException {
        byte tmp;
        tmp = PCD_ReadRegister(reg);
        PCD_WriteRegister(reg, (byte) (tmp & (~mask)));        // clear bit mask
    }

    /**
     * @param data       In: Pointer to the data to transfer to the FIFO for CRC calculation.
     * @param length     In: The number of bytes to transfer.
     * @param resultOut: Pointer to result buffer. Result is written to result[0..1], low byte first.
     * @return
     */
    StatusCode PCD_CalculateCRC(byte[] data,        ///<
                                int length,    ///<
                                byte[] resultOut, int from    ///<
    ) throws IOException {
        PCD_WriteRegister(CommandReg, PCD_Idle.value);        // Stop any active command.
        PCD_WriteRegister(DivIrqReg, (byte) 0x04);                // Clear the CRCIRq interrupt request bit
        PCD_SetRegisterBitMask(FIFOLevelReg, (byte) 0x80);        // FlushBuffer = 1, FIFO initialization
        PCD_WriteRegister(FIFODataReg, length, data);    // Write data to the FIFO
        PCD_WriteRegister(CommandReg, PCD_CalcCRC.value);        // Start the calculation

        // Wait for the CRC calculation to complete. Each iteration of the while-loop takes 17.73�s.
        int i = 5000;
        byte n;
        while (true) {
            n = PCD_ReadRegister(DivIrqReg);    // DivIrqReg[7..0] bits are: Set2 reserved reserved MfinActIRq reserved CRCIRq reserved reserved
            if ((n & 0x04) > 0) {                        // CRCIRq bit set - calculation done
                break;
            }
            if (--i == 0) {                        // The emergency break. We will eventually terminate on this one after 89ms. Communication with the MFRC522 might be down.
                return STATUS_TIMEOUT;
            }
        }
        PCD_WriteRegister(CommandReg, PCD_Idle.value);        // Stop calculating CRC for new content in the FIFO.

        // Transfer the result from the registers to the result buffer
        resultOut[from + 0] = PCD_ReadRegister(CRCResultRegL);
        resultOut[from + 1] = PCD_ReadRegister(CRCResultRegH);
        return STATUS_OK;
    }

    public void PCD_Init() throws IOException {
        /*if (bcm2835_gpio_lev(RSTPIN) == LOW) {	//The MFRC522 chip is in power down mode.
            bcm2835_gpio_write(RSTPIN, HIGH);		// Exit power down mode. This triggers a hard reset.
            // Section 8.8.2 in the datasheet says the oscillator start-up time is the start up time of the crystal + 37,74�s. Let us be generous: 50ms.
            delay(50);
        }
        else*/
        { // Perform a soft reset
            PCD_Reset();
        }

        // When communicating with a PICC we need a timeout if something goes wrong.
        // f_timer = 13.56 MHz / (2*TPreScaler+1) where TPreScaler = [TPrescaler_Hi:TPrescaler_Lo].
        // TPrescaler_Hi are the four low bits in TModeReg. TPrescaler_Lo is TPrescalerReg.
        PCD_WriteRegister(TModeReg, (byte) 0x80);            // TAuto=1; timer starts automatically at the end of the transmission in all communication modes at all speeds
        PCD_WriteRegister(TPrescalerReg, (byte) 0xA9);        // TPreScaler = TModeReg[3..0]:TPrescalerReg, ie 0x0A9 = 169 => f_timer=40kHz, ie a timer period of 25�s.
        PCD_WriteRegister(TReloadRegH, (byte) 0x03);        // Reload timer with 0x3E8 = 1000, ie 25ms before timeout.
        PCD_WriteRegister(TReloadRegL, (byte) 0xE8);

        PCD_WriteRegister(TxASKReg, (byte) 0x40);        // Default 0x00. Force a 100 % ASK modulation independent of the ModGsPReg register setting
        PCD_WriteRegister(ModeReg, (byte) 0x3D);        // Default 0x3F. Set the preset value for the CRC coprocessor for the CalcCRC command to 0x6363 (ISO 14443-3 part 6.2.4)
        PCD_AntennaOn();                        // Enable the antenna driver pins TX1 and TX2 (they were disabled by the reset)
    } // End PCD_Init()

    /**
     * Performs a soft reset on the MFRC522 chip and waits for it to be ready again.
     */
    void PCD_Reset() throws IOException {
        PCD_WriteRegister(CommandReg, PCD_SoftReset.value);    // Issue the SoftReset command.
        // The datasheet does not mention how long the SoftRest command takes to complete.
        // But the MFRC522 might have been in soft power-down mode (triggered by bit 4 of CommandReg)
        // Section 8.8.2 in the datasheet says the oscillator start-up time is the start up time of the crystal + 37,74�s. Let us be generous: 50ms.
        delay(50);
        // Wait for the PowerDown bit in CommandReg to be cleared
        while ((PCD_ReadRegister(CommandReg) & (1 << 4)) > 0) {
            // PCD still restarting - unlikely after waiting 50ms, but better safe than sorry.
            Thread.yield();
        }
    }

    void PCD_AntennaOn() throws IOException {
        byte value = PCD_ReadRegister(TxControlReg);
        if ((value & 0x03) != 0x03) {
            PCD_WriteRegister(TxControlReg, (byte) (value | 0x03));
        }
    }

    void PCD_AntennaOff() throws IOException {
        PCD_ClearRegisterBitMask(TxControlReg, (byte) 0x03);
    }

    byte PCD_GetAntennaGain() throws IOException {
        return (byte) (PCD_ReadRegister(RFCfgReg) & (0x07 << 4));
    }

    void PCD_SetAntennaGain(byte mask) throws IOException {
        if (PCD_GetAntennaGain() != mask) {                        // only bother if there is a change
            PCD_ClearRegisterBitMask(RFCfgReg, (byte) (0x07 << 4));        // clear needed to allow 000 pattern
            PCD_SetRegisterBitMask(RFCfgReg, (byte) (mask & (0x07 << 4)));    // only set RxGain[2:0] bits
        }
    }

    boolean PCD_PerformSelfTest() throws IOException {
        // This follows directly the steps outlined in 16.1.1
        // 1. Perform a soft reset.
        PCD_Reset();

        // 2. Clear the internal buffer by writing 25 bytes of 00h
        byte[] ZEROES = new byte[25];
        PCD_SetRegisterBitMask(FIFOLevelReg, (byte) 0x80); // flush the FIFO buffer
        PCD_WriteRegister(FIFODataReg, ZEROES.length, ZEROES); // write 25 bytes of 00h to FIFO
        PCD_WriteRegister(CommandReg, PCD_Mem.value); // transfer to internal buffer

        // 3. Enable self-test
        PCD_WriteRegister(AutoTestReg, (byte) 0x09);

        // 4. Write 00h to FIFO buffer
        PCD_WriteRegister(FIFODataReg, (byte) 0x00);

        // 5. Start self-test by issuing the CalcCRC command
        PCD_WriteRegister(CommandReg, PCD_CalcCRC.value);

        // 6. Wait for self-test to complete
        int i;
        byte n;
        for (i = 0; i < 0xFF; i++) {
            n = PCD_ReadRegister(DivIrqReg);    // DivIrqReg[7..0] bits are: Set2 reserved reserved MfinActIRq reserved CRCIRq reserved reserved
            if ((n & 0x04) > 0) {                        // CRCIRq bit set - calculation done
                break;
            }
        }
        PCD_WriteRegister(CommandReg, PCD_Idle.value);        // Stop calculating CRC for new content in the FIFO.

        // 7. Read out resulting 64 bytes from the FIFO buffer.
        byte[] result = new byte[64];
        PCD_ReadRegister(FIFODataReg, result.length, result, 0);

        // Auto self-test done
        // Reset AutoTestReg register to be 0 again. Required for normal operation.
        PCD_WriteRegister(AutoTestReg, (byte) 0x00);

        // Determine firmware version (see section 9.3.4.8 in spec)
        int version = PCD_ReadRegister(VersionReg);

        // Pick the appropriate reference values
        int[] reference;
        switch (version) {
            case 0x91:    // Version 1.0
                reference = MFRC522_firmware_referenceV1_0;
                break;
            case 0x92:    // Version 2.0
                reference = MFRC522_firmware_referenceV2_0;
                break;
            default:    // Unknown version
                return false;
        }

        // Verify that the results match up to our expectations
        for (i = 0; i < 64; i++) {
            if (result[i] != reference[i]) {
                return false;
            }
        }
        // Test passed; all is good.
        return true;
    }

    StatusCode PCD_TransceiveData(byte[] sendData, int sendLen, byte[] backData, int backLen, IntReference validBits) throws IOException {
        return PCD_TransceiveData(sendData, sendLen, backData, backLen, validBits, (byte) 0, false);
    }

    /**
     * @param sendData  Pointer to the data to transfer to the FIFO.
     * @param sendLen   Number of bytes to transfer to the FIFO.
     * @param backData  NULL or pointer to buffer if data should be read back after executing the command.
     * @param backLen   In: Max number of bytes to write to *backData. Out: The number of bytes returned.
     * @param validBits In/Out: The number of valid bits in the last byte. 0 for 8 valid bits. Default NULL.
     * @param rxAlign   In: Defines the bit position in backData[0] for the first bit received. Default 0.
     * @param checkCRC  In: True => The last two bytes of the response is assumed to be a CRC_A that must be validated.
     * @return
     */
    StatusCode PCD_TransceiveData(byte[] sendData, int sendLen, byte[] backData, int backLen, IntReference validBits, byte rxAlign, boolean checkCRC) throws IOException {
        byte waitIRq = 0x30;        // RxIRq and IdleIRq
        return PCD_CommunicateWithPICC(PCD_Transceive, waitIRq, sendData, sendLen, backData, backLen, validBits, rxAlign, checkCRC);
    }

    private StatusCode PCD_CommunicateWithPICC(PCD_Command command, byte waitIRq, byte[] sendData, int length) throws IOException {
        return PCD_CommunicateWithPICC(command, waitIRq, sendData, length, null, -1, null, (byte) 0, false);
    }

    /**
     * @param command   The command to execute. One of the PCD_Command enums.
     * @param waitIRq   The bits in the ComIrqReg register that signals successful completion of the command.
     * @param sendData  Pointer to the data to transfer to the FIFO.
     * @param sendLen   Number of bytes to transfer to the FIFO.
     * @param backData  NULL or pointer to buffer if data should be read back after executing the command.
     * @param backLen   In: Max number of bytes to write to *backData. Out: The number of bytes returned.
     * @param validBits In/Out: The number of valid bits in the last byte. 0 for 8 valid bits.
     * @param rxAlign   In: Defines the bit position in backData[0] for the first bit received. Default 0.
     * @param checkCRC  In: True => The last two bytes of the response is assumed to be a CRC_A that must be validated.
     * @return
     */
    StatusCode PCD_CommunicateWithPICC(PCD_Command command, byte waitIRq, byte[] sendData, int sendLen, byte[] backData, int backLen, IntReference validBits, byte rxAlign, boolean checkCRC) throws IOException {
        byte n;
        byte _validBits = 0;
        int i;

        // Prepare values for BitFramingReg
        byte txLastBits = (byte) (Math.max(validBits.getValue(), 0));
        byte bitFraming = (byte) ((rxAlign << 4) + txLastBits);        // RxAlign = BitFramingReg[6..4]. TxLastBits = BitFramingReg[2..0]

        PCD_WriteRegister(CommandReg, PCD_Idle.value);            // Stop any active command.
        PCD_WriteRegister(ComIrqReg, (byte) 0x7F);                    // Clear all seven interrupt request bits
        PCD_SetRegisterBitMask(FIFOLevelReg, (byte) 0x80);            // FlushBuffer = 1, FIFO initialization
        PCD_WriteRegister(FIFODataReg, sendLen, sendData);    // Write sendData to the FIFO
        PCD_WriteRegister(BitFramingReg, bitFraming);        // Bit adjustments
        PCD_WriteRegister(CommandReg, command.value);                // Execute the command
        if (command == PCD_Transceive) {
            PCD_SetRegisterBitMask(BitFramingReg, (byte) 0x80);    // StartSend=1, transmission of data starts
        }

        // Wait for the command to complete.
        // In PCD_Init() we set the TAuto flag in TModeReg. This means the timer automatically starts when the PCD stops transmitting.
        // Each iteration of the do-while-loop takes 17.86�s.
        i = 2000;
        while (true) {
            n = PCD_ReadRegister(ComIrqReg);    // ComIrqReg[7..0] bits are: Set1 TxIRq RxIRq IdleIRq HiAlertIRq LoAlertIRq ErrIRq TimerIRq
            if ((n & waitIRq) > 0) {                    // One of the interrupts that signal success has been set.
                break;
            }
            if ((n & 0x01) > 0) {                        // Timer interrupt - nothing received in 25ms
                return STATUS_TIMEOUT;
            }
            if (--i == 0) {                        // The emergency break. If all other condions fail we will eventually terminate on this one after 35.7ms. Communication with the MFRC522 might be down.
                return STATUS_TIMEOUT;
            }
        }

        // Stop now if any errors except collisions were detected.
        byte errorRegValue = PCD_ReadRegister(ErrorReg); // ErrorReg[7..0] bits are: WrErr TempErr reserved BufferOvfl CollErr CRCErr ParityErr ProtocolErr
        if ((errorRegValue & 0x13) > 0) {     // BufferOvfl ParityErr ProtocolErr
            return STATUS_ERROR;
        }

        // If the caller wants data back, get it from the MFRC522.
        if (backData != null && backLen > 0) {
            n = PCD_ReadRegister(FIFOLevelReg);            // Number of bytes in the FIFO
            if (n > backLen) {
                return STATUS_NO_ROOM;
            }
            backLen = n;                                            // Number of bytes returned
            PCD_ReadRegister(FIFODataReg, n, backData, rxAlign);    // Get received data from FIFO
            _validBits = (byte) (PCD_ReadRegister(ControlReg) & 0x07);        // RxLastBits[2:0] indicates the number of valid bits in the last received byte. If this value is 000b, the whole byte is valid.
            if (validBits != null) {
                validBits.setValue(_validBits);
            }
        }

        // Tell about collisions
        if ((errorRegValue & 0x08) > 0) {        // CollErr
            return STATUS_COLLISION;
        }

        // Perform CRC_A validation if requested.
        if (backData != null && backLen > 0 && checkCRC) {
            // In this case a MIFARE Classic NAK is not OK.
            if (backLen == 1 && _validBits == 4) {
                return STATUS_MIFARE_NACK;
            }
            // We need at least the CRC_A value and all 8 bits of the last byte must be received.
            if (backLen < 2 || _validBits != 0) {
                return STATUS_CRC_WRONG;
            }
            // Verify CRC_A - do our own calculation and store the control in controlBuffer.
            byte[] controlBuffer = new byte[2];
            StatusCode code = PCD_CalculateCRC(backData, backLen - 2, controlBuffer, 0);
            if (code != STATUS_OK) {
                return code;
            }
            if ((backData[backLen - 2] != controlBuffer[0]) || (backData[backLen - 1] != controlBuffer[1])) {
                return STATUS_CRC_WRONG;
            }
        }

        return STATUS_OK;
    }

    /**
     * @param bufferATQA The buffer to store the ATQA (Answer to request) in
     * @param bufferSize Buffer size, at least two bytes. Also number of bytes returned if STATUS_OK.
     * @return
     */
    StatusCode PICC_RequestA(byte[] bufferATQA, int bufferSize) throws IOException {
        return PICC_REQA_or_WUPA(PICC_CMD_REQA, bufferATQA, bufferSize);
    }

    /**
     * @param bufferATQA The buffer to store the ATQA (Answer to request) in
     * @param bufferSize Buffer size, at least two bytes. Also number of bytes returned if STATUS_OK.
     * @return
     */
    StatusCode PICC_WakeupA(byte[] bufferATQA, int bufferSize) throws IOException {
        return PICC_REQA_or_WUPA(PICC_CMD_WUPA, bufferATQA, bufferSize);
    }

    StatusCode PICC_REQA_or_WUPA(PICC_Command command,        ///< The command to send - PICC_CMD_REQA or PICC_CMD_WUPA
                                 byte[] bufferATQA,    ///< The buffer to store the ATQA (Answer to request) in
                                 int bufferSize    ///< Buffer size, at least two bytes. Also number of bytes returned if STATUS_OK.
    ) throws IOException {
        IntReference validBits = new IntReference(7);

        if (bufferATQA == null || bufferSize < 2) {    // The ATQA response is 2 bytes long.
            return STATUS_NO_ROOM;
        }
        PCD_ClearRegisterBitMask(CollReg, (byte) 0x80);        // ValuesAfterColl=1 => Bits received after collision are cleared.
        StatusCode status = PCD_TransceiveData(new byte[]{command.value}, 1, bufferATQA, bufferSize, validBits);
        if (status != STATUS_OK) {
            return status;
        }
        if (bufferSize != 2 || validBits.getValue() != 0) {        // ATQA must be exactly 16 bits.
            return STATUS_ERROR;
        }
        return STATUS_OK;
    }

    StatusCode PICC_Select(Uid uid, byte validBits) throws IOException {
        boolean uidComplete;
        boolean selectDone;
        boolean useCascadeTag;
        byte cascadeLevel = 1;
        StatusCode result;
        byte count;
        byte index;
        byte uidIndex;                    // The first index in uid->uidByte[] that is used in the current Cascade Level.
        byte currentLevelKnownBits;        // The number of known UID bits in the current Cascade Level.
        byte[] buffer = new byte[9];                    // The SELECT/ANTICOLLISION commands uses a 7 byte standard frame + 2 bytes CRC_A
        byte bufferUsed;                // The number of bytes used in the buffer, ie the number of bytes to transfer to the FIFO.
        byte rxAlign;                    // Used in BitFramingReg. Defines the bit position for the first bit received.
        byte txLastBits = 0;                // Used in BitFramingReg. The number of valid bits in the last transmitted byte.
        byte[] responseBuffer = new byte[4];
        byte responseLength = 0;

        // Description of buffer structure:
        //		Byte 0: SEL 				Indicates the Cascade Level: PICC_CMD_SEL_CL1, PICC_CMD_SEL_CL2 or PICC_CMD_SEL_CL3
        //		Byte 1: NVB					Number of Valid Bits (in complete command, not just the UID): High nibble: complete bytes, Low nibble: Extra bits.
        //		Byte 2: UID-data or CT		See explanation below. CT means Cascade Tag.
        //		Byte 3: UID-data
        //		Byte 4: UID-data
        //		Byte 5: UID-data
        //		Byte 6: BCC					Block Check Character - XOR of bytes 2-5
        //		Byte 7: CRC_A
        //		Byte 8: CRC_A
        // The BCC and CRC_A is only transmitted if we know all the UID bits of the current Cascade Level.
        //
        // Description of bytes 2-5: (Section 6.5.4 of the ISO/IEC 14443-3 draft: UID contents and cascade levels)
        //		UID size	Cascade level	Byte2	Byte3	Byte4	Byte5
        //		========	=============	=====	=====	=====	=====
        //		 4 bytes		1			uid0	uid1	uid2	uid3
        //		 7 bytes		1			CT		uid0	uid1	uid2
        //						2			uid3	uid4	uid5	uid6
        //		10 bytes		1			CT		uid0	uid1	uid2
        //						2			CT		uid3	uid4	uid5
        //						3			uid6	uid7	uid8	uid9

        // Sanity checks
        if (validBits > 80) {
            return STATUS_INVALID;
        }

        // Prepare MFRC522
        PCD_ClearRegisterBitMask(CollReg, (byte) 0x80);        // ValuesAfterColl=1 => Bits received after collision are cleared.

        // Repeat Cascade Level loop until we have a complete UID.
        uidComplete = false;
        while (!uidComplete) {
            // Set the Cascade Level in the SEL byte, find out if we need to use the Cascade Tag in byte 2.
            switch (cascadeLevel) {
                case 1:
                    buffer[0] = PICC_CMD_SEL_CL1.value;
                    uidIndex = 0;
                    useCascadeTag = validBits > 1 && uid.size > 4;    // When we know that the UID has more than 4 bytes
                    break;

                case 2:
                    buffer[0] = PICC_CMD_SEL_CL2.value;
                    uidIndex = 3;
                    useCascadeTag = validBits > 0 && uid.size > 7;    // When we know that the UID has more than 7 bytes
                    break;

                case 3:
                    buffer[0] = PICC_CMD_SEL_CL3.value;
                    uidIndex = 6;
                    useCascadeTag = false;                        // Never used in CL3.
                    break;

                default:
                    return STATUS_INTERNAL_ERROR;
            }

            // How many UID bits are known in this Cascade Level?
            currentLevelKnownBits = (byte) (validBits - (8 * uidIndex));
            if (currentLevelKnownBits < 0) {
                currentLevelKnownBits = 0;
            }
            // Copy the known bits from uid->uidByte[] to buffer[]
            index = 2; // destination index in buffer[]
            if (useCascadeTag) {
                buffer[index++] = PICC_CMD_CT.value;
            }
            byte bytesToCopy = (byte) (currentLevelKnownBits / 8 + (currentLevelKnownBits % 8 > 0 ? 1 : 0)); // The number of bytes needed to represent the known bits for this level.
            if (bytesToCopy > 0) {
                byte maxBytes = (byte) (useCascadeTag ? 3 : 4); // Max 4 bytes in each Cascade Level. Only 3 left if we use the Cascade Tag
                if (bytesToCopy > maxBytes) {
                    bytesToCopy = maxBytes;
                }
                for (count = 0; count < bytesToCopy; count++) {
                    buffer[index++] = uid.uidByte[uidIndex + count];
                }
            }
            // Now that the data has been copied we need to include the 8 bits in CT in currentLevelKnownBits
            if (useCascadeTag) {
                currentLevelKnownBits += 8;
            }

            // Repeat anti collision loop until we can transmit all UID bits + BCC and receive a SAK - max 32 iterations.
            selectDone = false;
            while (!selectDone) {
                // Find out how many bits and bytes to send and receive.
                if (currentLevelKnownBits >= 32) { // All UID bits in this Cascade Level are known. This is a SELECT.
                    //Serial.print(F("SELECT: currentLevelKnownBits=")); Serial.println(currentLevelKnownBits, DEC);
                    buffer[1] = 0x70; // NVB - Number of Valid Bits: Seven whole bytes
                    // Calculate BCC - Block Check Character
                    buffer[6] = (byte) (buffer[2] ^ buffer[3] ^ buffer[4] ^ buffer[5]);
                    // Calculate CRC_A
                    result = PCD_CalculateCRC(buffer, 7, buffer, 7);
                    if (result != STATUS_OK) {
                        return result;
                    }
                    txLastBits = 0; // 0 => All 8 bits are valid.
                    bufferUsed = 9;
                    // Store response in the last 3 bytes of buffer (BCC and CRC_A - not needed after tx)
                    responseBuffer = Arrays.copyOfRange(buffer, 6, buffer.length);
                    responseLength = 3;
                } else { // This is an ANTICOLLISION.
                    //Serial.print(F("ANTICOLLISION: currentLevelKnownBits=")); Serial.println(currentLevelKnownBits, DEC);
                    txLastBits = (byte) (currentLevelKnownBits % 8);
                    count = (byte) (currentLevelKnownBits / 8);    // Number of whole bytes in the UID part.
                    index = (byte) (2 + count);                    // Number of whole bytes: SEL + NVB + UIDs
                    buffer[1] = (byte) ((index << 4) + txLastBits);    // NVB - Number of Valid Bits
                    bufferUsed = (byte) (index + (txLastBits > 0 ? 1 : 0));
                    // Store response in the unused part of buffer
                    responseBuffer = Arrays.copyOfRange(buffer, index, buffer.length);
                    responseLength = (byte) (buffer.length - index);
                }

                // Set bit adjustments
                rxAlign = txLastBits;                                            // Having a seperate variable is overkill. But it makes the next line easier to read.
                PCD_WriteRegister(BitFramingReg, (byte) ((rxAlign << 4) + txLastBits));    // RxAlign = BitFramingReg[6..4]. TxLastBits = BitFramingReg[2..0]
                IntReference ref = new IntReference(txLastBits);
                // Transmit the buffer and receive the response.
                result = PCD_TransceiveData(buffer, bufferUsed, responseBuffer, responseLength, ref, rxAlign, false);
                txLastBits = (byte) ref.getValue();
                if (result == STATUS_COLLISION) { // More than one PICC in the field => collision.
                    int res = PCD_ReadRegister(CollReg); // CollReg[7..0] bits are: ValuesAfterColl reserved CollPosNotValid CollPos[4:0]
                    if ((res & 0x20) > 0) { // CollPosNotValid
                        return STATUS_COLLISION; // Without a valid collision position we cannot continue
                    }
                    byte collisionPos = (byte) (res & 0x1F); // Values 0-31, 0 means bit 32.
                    if (collisionPos == 0) {
                        collisionPos = 32;
                    }
                    if (collisionPos <= currentLevelKnownBits) { // No progress - should not happen
                        return STATUS_INTERNAL_ERROR;
                    }
                    // Choose the PICC with the bit set.
                    currentLevelKnownBits = collisionPos;
                    count = (byte) ((currentLevelKnownBits - 1) % 8); // The bit to modify
                    index = (byte) (1 + (currentLevelKnownBits / 8) + (count > 0 ? 1 : 0)); // First byte is index 0.
                    buffer[index] |= (1 << count);
                } else if (result != STATUS_OK) {
                    return result;
                } else { // STATUS_OK
                    if (currentLevelKnownBits >= 32) { // This was a SELECT.
                        selectDone = true; // No more anticollision
                        // We continue below outside the while.
                    } else { // This was an ANTICOLLISION.
                        // We now have all 32 bits of the UID in this Cascade Level
                        currentLevelKnownBits = 32;
                        // Run loop again to do the SELECT.
                    }
                }
            } // End of while (!selectDone)

            // We do not check the CBB - it was constructed by us above.

            // Copy the found UID bytes from buffer[] to uid->uidByte[]
            index = (byte) ((buffer[2] == PICC_CMD_CT.value) ? 3 : 2); // source index in buffer[]
            bytesToCopy = (byte) ((buffer[2] == PICC_CMD_CT.value) ? 3 : 4);
            for (count = 0; count < bytesToCopy; count++) {
                uid.uidByte[uidIndex + count] = buffer[index++];
            }

            // Check response SAK (Select Acknowledge)
            if (responseLength != 3 || txLastBits != 0) { // SAK must be exactly 24 bits (1 byte + CRC_A).
                return STATUS_ERROR;
            }
            // Verify CRC_A - do our own calculation and store the control in buffer[2..3] - those bytes are not needed anymore.
            result = PCD_CalculateCRC(responseBuffer, 1, buffer, 2);
            if (result != STATUS_OK) {
                return result;
            }
            if ((buffer[2] != responseBuffer[1]) || (buffer[3] != responseBuffer[2])) {
                return STATUS_CRC_WRONG;
            }
            if ((responseBuffer[0] & 0x04) > 0) { // Cascade bit set - UID not complete yes
                cascadeLevel++;
            } else {
                uidComplete = true;
                uid.sak = responseBuffer[0];
            }
        } // End of while (!uidComplete)

        // Set correct uid->size
        uid.size = (byte) (3 * cascadeLevel + 1);

        return STATUS_OK;
    }

    public StatusCode PICC_HaltA() throws IOException {
        StatusCode result;
        byte[] buffer = new byte[4];

        // Build command buffer
        buffer[0] = PICC_CMD_HLTA.value;
        buffer[1] = 0;
        // Calculate CRC_A
        result = PCD_CalculateCRC(buffer, 2, buffer, 2);
        if (result != STATUS_OK) {
            return result;
        }
        // Send the command.
        // The standard says:
        //		If the PICC responds with any modulation during a period of 1 ms after the end of the frame containing the
        //		HLTA command, this response shall be interpreted as 'not acknowledge'.
        // We interpret that this way: Only STATUS_TIMEOUT is an success.
        result = PCD_TransceiveData(buffer, buffer.length, null, 0, null);
        if (result == STATUS_TIMEOUT) {
            return STATUS_OK;
        }
        if (result == STATUS_OK) { // That is ironically NOT ok in this case ;-)
            return STATUS_ERROR;
        }
        return result;
    }

    /**
     * @param command   PICC_CMD_MF_AUTH_KEY_A or PICC_CMD_MF_AUTH_KEY_B
     * @param blockAddr The block number. See numbering in the comments in the .h file.
     * @param key       Pointer to the Crypteo1 key to use (6 bytes)
     * @param uid       Pointer to Uid struct. The first 4 bytes of the UID is used.
     * @return
     */
    public StatusCode PCD_Authenticate(PICC_Command command, byte blockAddr, MIFARE_Key key, Uid uid) throws IOException {
        byte waitIRq = 0x10;        // IdleIRq

        // Build command buffer
        byte[] sendData = new byte[12];
        sendData[0] = command.value;
        sendData[1] = blockAddr;
        for (byte i = 0; i < MIFARE_Misc.MF_KEY_SIZE.value; i++) {    // 6 key bytes
            sendData[2 + i] = key.keyByte[i];
        }
        for (byte i = 0; i < 4; i++) {                // The first 4 bytes of the UID
            sendData[8 + i] = uid.uidByte[i];
        }

        // Start the authentication.
        return PCD_CommunicateWithPICC(PCD_MFAuthent, waitIRq, sendData, sendData.length);
    }


    public void PCD_StopCrypto1() throws IOException {
        // Clear MFCrypto1On bit
        PCD_ClearRegisterBitMask(Status2Reg, (byte) 0x08); // Status2Reg[7..0] bits are: TempSensClear I2CForceHS reserved reserved MFCrypto1On ModemState[2:0]
    }

    /**
     * @param blockAddr  MIFARE Classic: The block (0-0xff) number. MIFARE Ultralight: The first page to return data from.
     * @param buffer     The buffer to store the data in
     * @param bufferSize Buffer size, at least 18 bytes. Also number of bytes returned if STATUS_OK.
     * @return
     */
    public StatusCode MIFARE_Read(byte blockAddr, byte[] buffer, int bufferSize) throws IOException {
        StatusCode result;

        // Sanity check
        if (buffer == null || bufferSize < 18) {
            return STATUS_NO_ROOM;
        }

        // Build command buffer
        buffer[0] = PICC_CMD_MF_READ.value;
        buffer[1] = blockAddr;
        // Calculate CRC_A
        result = PCD_CalculateCRC(buffer, 2, buffer, 2);
        if (result != STATUS_OK) {
            return result;
        }

        // Transmit the buffer and receive the response, validate CRC_A.
        return PCD_TransceiveData(buffer, 4, buffer, bufferSize, null, (byte) 0, true);
    }

    /**
     * @param blockAddr  MIFARE Classic: The block (0-0xff) number. MIFARE Ultralight: The page (2-15) to write to.
     * @param buffer     The 16 bytes to write to the PICC
     * @param bufferSize Buffer size, must be at least 16 bytes. Exactly 16 bytes are written.
     * @return
     */
    public StatusCode MIFARE_Write(byte blockAddr, byte[] buffer, int bufferSize) throws IOException {
        StatusCode result;

        // Sanity check
        if (buffer == null || bufferSize < 16) {
            return STATUS_INVALID;
        }

        // Mifare Classic protocol requires two communications to perform a write.
        // Step 1: Tell the PICC we want to write to block blockAddr.
        byte[] cmdBuffer = new byte[2];
        cmdBuffer[0] = PICC_CMD_MF_WRITE.value;
        cmdBuffer[1] = blockAddr;
        result = PCD_MIFARE_Transceive(cmdBuffer, 2); // Adds CRC_A and checks that the response is MF_ACK.
        if (result != STATUS_OK) {
            return result;
        }

        // Step 2: Transfer the data
        return PCD_MIFARE_Transceive(buffer, bufferSize); // Adds CRC_A and checks that the response is MF_ACK.
    }

    /**
     * @param page       The page (2-15) to write to.
     * @param buffer     The 4 bytes to write to the PICC
     * @param bufferSize Buffer size, must be at least 4 bytes. Exactly 4 bytes are written.
     * @return
     */
    StatusCode MIFARE_Ultralight_Write(byte page, byte[] buffer, int bufferSize) throws IOException {
        StatusCode result;

        // Sanity check
        if (buffer == null || bufferSize < 4) {
            return STATUS_INVALID;
        }

        // Build commmand buffer
        byte[] cmdBuffer = new byte[6];
        cmdBuffer[0] = PICC_CMD_UL_WRITE.value;
        cmdBuffer[1] = page;
        System.arraycopy(cmdBuffer, 2, buffer, 0, 4);

        // Perform the write
        return PCD_MIFARE_Transceive(cmdBuffer, 6); // Adds CRC_A and checks that the response is MF_ACK.
    }

    /**
     * @param blockAddr The block (0-0xff) number.
     * @param delta     This number is subtracted from the value of block blockAddr.
     * @return
     */
    StatusCode MIFARE_Decrement(byte blockAddr, long delta) throws IOException {
        return MIFARE_TwoStepHelper(PICC_CMD_MF_DECREMENT.value, blockAddr, delta);
    }

    /**
     * @param blockAddr The block (0-0xff) number.
     * @param delta     This number is subtracted from the value of block blockAddr.
     * @return
     */
    StatusCode MIFARE_Increment(byte blockAddr, long delta) throws IOException {
        return MIFARE_TwoStepHelper(PICC_CMD_MF_INCREMENT.value, blockAddr, delta);
    }

    /**
     * The block (0-0xff) number.
     *
     * @param blockAddr
     * @return
     */
    StatusCode MIFARE_Restore(byte blockAddr) throws IOException {
        // The datasheet describes Restore as a two-step operation, but does not explain what data to transfer in step 2.
        // Doing only a single step does not work, so I chose to transfer 0L in step two.
        return MIFARE_TwoStepHelper(PICC_CMD_MF_RESTORE.value, blockAddr, 0L);
    }

    /**
     * @param command   The command to use
     * @param blockAddr The block (0-0xff) number.
     * @param data      The data to transfer in step 2
     * @return
     */
    StatusCode MIFARE_TwoStepHelper(byte command, byte blockAddr, long data) throws IOException {
        StatusCode result;
        byte[] cmdBuffer = new byte[2]; // We only need room for 2 bytes.

        // Step 1: Tell the PICC the command and block address
        cmdBuffer[0] = command;
        cmdBuffer[1] = blockAddr;
        result = PCD_MIFARE_Transceive(cmdBuffer, 2); // Adds CRC_A and checks that the response is MF_ACK.
        if (result != STATUS_OK) {
            return result;
        }

        // Step 2: Transfer the data
        byte[] array = ByteBuffer.allocate(4).putLong(data).array();
        return PCD_MIFARE_Transceive(array, 4, true); // Adds CRC_A and accept timeout as success.
    }

    StatusCode MIFARE_Transfer(byte blockAddr) throws IOException {
        byte result;
        byte[] cmdBuffer = new byte[2]; // We only need room for 2 bytes.

        // Tell the PICC we want to transfer the result into block blockAddr.
        cmdBuffer[0] = PICC_CMD_MF_TRANSFER.value;
        cmdBuffer[1] = blockAddr;
        return PCD_MIFARE_Transceive(cmdBuffer, 2); // Adds CRC_A and checks that the response is MF_ACK.
    }

    StatusCode MIFARE_GetValue(byte blockAddr, LongReference value) throws IOException {
        StatusCode status;
        byte[] buffer = new byte[18];
        int size = buffer.length;

        // Read the block
        status = MIFARE_Read(blockAddr, buffer, size);
        if (status == STATUS_OK) {
            // Extract the value
            long tmp = ((long) (buffer[3]) << 24) | ((long) (buffer[2]) << 16) | ((long) (buffer[1]) << 8) | (long) buffer[0];
            value.setValue(tmp);
        }
        return status;
    }

    StatusCode MIFARE_SetValue(byte blockAddr, long value) throws IOException {
        byte[] buffer = new byte[18];

        // Translate the long into 4 bytes; repeated 2x in value block
        buffer[0] = buffer[8] = (byte) (value & 0xFF);
        buffer[1] = buffer[9] = (byte) ((value & 0xFF00) >> 8);
        buffer[2] = buffer[10] = (byte) ((value & 0xFF0000) >> 16);
        buffer[3] = buffer[11] = (byte) ((value & 0xFF000000) >> 24);
        // Inverse 4 bytes also found in value block
        buffer[4] = (byte) ~buffer[0];
        buffer[5] = (byte) ~buffer[1];
        buffer[6] = (byte) ~buffer[2];
        buffer[7] = (byte) ~buffer[3];
        // Address 2x with inverse address 2x
        buffer[12] = buffer[14] = blockAddr;
        buffer[13] = buffer[15] = (byte) ~blockAddr;

        // Write the whole data block
        return MIFARE_Write(blockAddr, buffer, 16);
    }

    StatusCode PCD_MIFARE_Transceive(byte[] sendData,        ///< Pointer to the data to transfer to the FIFO. Do NOT include the CRC_A.
                                     int sendLen) throws IOException {
        return PCD_MIFARE_Transceive(sendData, sendLen, false);
    }

    StatusCode PCD_MIFARE_Transceive(byte[] sendData,        ///< Pointer to the data to transfer to the FIFO. Do NOT include the CRC_A.
                                     int sendLen,        ///< Number of bytes in sendData.
                                     boolean acceptTimeout    ///< True => A timeout is also success
    ) throws IOException {
        StatusCode result;
        byte[] cmdBuffer = new byte[18]; // We need room for 16 bytes data and 2 bytes CRC_A.

        // Sanity check
        if (sendData == null || sendLen > 16) {
            return STATUS_INVALID;
        }

        // Copy sendData[] to cmdBuffer[] and add CRC_A
        System.arraycopy(sendData, 0, cmdBuffer, 0, sendData.length);
        result = PCD_CalculateCRC(cmdBuffer, sendLen, cmdBuffer, sendLen);
        if (result != STATUS_OK) {
            return result;
        }
        sendLen += 2;

        // Transceive the data, store the reply in cmdBuffer[]
        byte waitIRq = 0x30;        // RxIRq and IdleIRq
        int cmdBufferSize = cmdBuffer.length;
        IntReference validBits = new IntReference(0);
        result = PCD_CommunicateWithPICC(PCD_Transceive, waitIRq, cmdBuffer, sendLen, cmdBuffer, cmdBufferSize, validBits, (byte) 0, false);
        if (acceptTimeout && result == STATUS_TIMEOUT) {
            return STATUS_OK;
        }
        if (result != STATUS_OK) {
            return result;
        }
        // The PICC must reply with a 4 bit ACK
        if (cmdBufferSize != 1 || validBits.getValue() != 4) {
            return STATUS_ERROR;
        }
        if (cmdBuffer[0] != MIFARE_Misc.MF_ACK.value) {
            return STATUS_MIFARE_NACK;
        }
        return STATUS_OK;
    }

    public String GetStatusCodeName(StatusCode code) {
        switch (code) {
            case STATUS_OK:
                return ("Success.");
            case STATUS_ERROR:
                return ("Error in communication.");
            case STATUS_COLLISION:
                return ("Collission detected.");
            case STATUS_TIMEOUT:
                return ("Timeout in communication.");
            case STATUS_NO_ROOM:
                return ("A buffer is not big enough.");
            case STATUS_INTERNAL_ERROR:
                return ("Internal error in the code. Should not happen.");
            case STATUS_INVALID:
                return ("Invalid argument.");
            case STATUS_CRC_WRONG:
                return ("The CRC_A does not match.");
            case STATUS_MIFARE_NACK:
                return ("A MIFARE PICC responded with NAK.");
            default:
                return ("Unknown error");
        }
    }

    public PICC_Type PICC_GetType(byte sak        ///< The SAK byte returned from PICC_Select().
    ) {
        if ((sak & 0x04) > 0) { // UID not complete
            return PICC_TYPE_NOT_COMPLETE;
        }

        switch (sak) {
            case 0x09:
                return PICC_TYPE_MIFARE_MINI;
            case 0x08:
                return PICC_TYPE_MIFARE_1K;
            case 0x18:
                return PICC_TYPE_MIFARE_4K;
            case 0x00:
                return PICC_TYPE_MIFARE_UL;
            case 0x10:
            case 0x11:
                return PICC_TYPE_MIFARE_PLUS;
            case 0x01:
                return PICC_TYPE_TNP3XXX;
        }

        if ((sak & 0x20) > 0) {
            return PICC_TYPE_ISO_14443_4;
        }

        if ((sak & 0x40) > 0) {
            return PICC_TYPE_ISO_18092;
        }

        return PICC_TYPE_UNKNOWN;
    }

    public String PICC_GetTypeName(PICC_Type piccType    ///< One of the PICC_Type enums.
    ) {
        switch (piccType) {
            case PICC_TYPE_ISO_14443_4:
                return ("PICC compliant with ISO/IEC 14443-4");
            case PICC_TYPE_ISO_18092:
                return ("PICC compliant with ISO/IEC 18092 (NFC)");
            case PICC_TYPE_MIFARE_MINI:
                return ("MIFARE Mini, 320 bytes");
            case PICC_TYPE_MIFARE_1K:
                return ("MIFARE 1KB");
            case PICC_TYPE_MIFARE_4K:
                return ("MIFARE 4KB");
            case PICC_TYPE_MIFARE_UL:
                return ("MIFARE Ultralight or Ultralight C");
            case PICC_TYPE_MIFARE_PLUS:
                return ("MIFARE Plus");
            case PICC_TYPE_TNP3XXX:
                return ("MIFARE TNP3XXX");
            case PICC_TYPE_NOT_COMPLETE:
                return ("SAK indicates UID is not complete.");
            case PICC_TYPE_UNKNOWN:
            default:
                return ("Unknown type");
        }
    }

    void PICC_DumpToSerial(Uid uid) throws IOException {
        MIFARE_Key key = new MIFARE_Key();

        // UID
        printf("Card UID:");
        for (byte i = 0; i < uid.size; i++) {
            if (uid.uidByte[i] < 0x10)
                printf(" 0");
            else
                printf(" ");
            printf("%X", uid.uidByte[i]);
        }
        printf("\n");

        // PICC type
        PICC_Type piccType = PICC_GetType(uid.sak);
        printf("PICC type: ");
        printf("%s", PICC_GetTypeName(piccType));

        // Dump contents
        switch (piccType) {
            case PICC_TYPE_MIFARE_MINI:
            case PICC_TYPE_MIFARE_1K:
            case PICC_TYPE_MIFARE_4K:
                // All keys are set to FFFFFFFFFFFFh at chip delivery from the factory.
                for (byte i = 0; i < 6; i++) {
                    key.keyByte[i] = (byte) 0xFF;
                }
                PICC_DumpMifareClassicToSerial(uid, piccType, key);
                break;

            case PICC_TYPE_MIFARE_UL:
                PICC_DumpMifareUltralightToSerial();
                break;

            case PICC_TYPE_ISO_14443_4:
            case PICC_TYPE_ISO_18092:
            case PICC_TYPE_MIFARE_PLUS:
            case PICC_TYPE_TNP3XXX:
                printf("Dumping memory contents not implemented for that PICC type.");
                break;

            case PICC_TYPE_UNKNOWN:
            case PICC_TYPE_NOT_COMPLETE:
            default:
                break; // No memory dump here
        }

        printf("\n");
        PICC_HaltA(); // Already done if it was a MIFARE Classic PICC.
    }

    void PICC_DumpMifareClassicToSerial(Uid uid,        ///< Pointer to Uid struct returned from a successful PICC_Select().
                                        PICC_Type piccType,    ///< One of the PICC_Type enums.
                                        MIFARE_Key key    ///< Key A used for all sectors.
    ) throws IOException {
        byte no_of_sectors = 0;
        switch (piccType) {
            case PICC_TYPE_MIFARE_MINI:
                // Has 5 sectors * 4 blocks/sector * 16 bytes/block = 320 bytes.
                no_of_sectors = 5;
                break;

            case PICC_TYPE_MIFARE_1K:
                // Has 16 sectors * 4 blocks/sector * 16 bytes/block = 1024 bytes.
                no_of_sectors = 16;
                break;

            case PICC_TYPE_MIFARE_4K:
                // Has (32 sectors * 4 blocks/sector + 8 sectors * 16 blocks/sector) * 16 bytes/block = 4096 bytes.
                no_of_sectors = 40;
                break;

            default: // Should not happen. Ignore.
                break;
        }

        // Dump sectors, highest address first.
        if (no_of_sectors > 0) {
            printf("Sector Block   0  1  2  3   4  5  6  7   8  9 10 11  12 13 14 15  AccessBits\n");
            for (int i = no_of_sectors - 1; i >= 0; i--) {
                PICC_DumpMifareClassicSectorToSerial(uid, key, (byte) i);
            }
        }
        PICC_HaltA(); // Halt the PICC before stopping the encrypted session.
        PCD_StopCrypto1();
    }

    /**
     * @param uid    Pointer to Uid struct returned from a successful PICC_Select().
     * @param key    Key A for the sector.
     * @param sector The sector to dump, 0..39.
     * @throws IOException
     */
    public void PICC_DumpMifareClassicSectorToSerial(Uid uid, MIFARE_Key key, byte sector) throws IOException {
        StatusCode status;
        int firstBlock;        // Address of lowest address to dump actually last block dumped)
        byte no_of_blocks;        // Number of blocks in sector
        boolean isSectorTrailer;    // Set to true while handling the "last" (ie highest address) in the sector.

        // The access bits are stored in a peculiar fashion.
        // There are four groups:
        //		g[3]	Access bits for the sector trailer, block 3 (for sectors 0-31) or block 15 (for sectors 32-39)
        //		g[2]	Access bits for block 2 (for sectors 0-31) or blocks 10-14 (for sectors 32-39)
        //		g[1]	Access bits for block 1 (for sectors 0-31) or blocks 5-9 (for sectors 32-39)
        //		g[0]	Access bits for block 0 (for sectors 0-31) or blocks 0-4 (for sectors 32-39)
        // Each group has access bits [C1 C2 C3]. In this code C1 is MSB and C3 is LSB.
        // The four CX bits are stored together in a nible cx and an inverted nible cx_.
        byte c1, c2, c3;        // Nibbles
        byte c1_, c2_, c3_;        // Inverted nibbles
        boolean invertedError = false;        // True if one of the inverted nibbles did not match
        byte[] g = new byte[4];                // Access bits for each of the four groups.
        int group;                // 0-3 - active group for access bits
        boolean firstInGroup;        // True for the first block dumped in the group

        // Determine position and size of sector.
        if (sector < 32) { // Sectors 0..31 has 4 blocks each
            no_of_blocks = 4;
            firstBlock = sector * no_of_blocks;
        } else if (sector < 40) { // Sectors 32-39 has 16 blocks each
            no_of_blocks = 16;
            firstBlock = 128 + (sector - 32) * no_of_blocks;
        } else { // Illegal input, no MIFARE Classic PICC has more than 40 sectors.
            return;
        }

        // Dump blocks, highest address first.
        int byteCount;
        byte[] buffer = new byte[18];
        int blockAddr;
        isSectorTrailer = true;
        for (int blockOffset = no_of_blocks - 1; blockOffset >= 0; blockOffset--) {
            blockAddr = firstBlock + blockOffset;
            // Sector number - only on first line
            if (isSectorTrailer) {
                if (sector < 10)
                    printf("   "); // Pad with spaces
                else
                    printf("  "); // Pad with spaces
                printf("%02X", sector);
                printf("   ");
            } else {
                printf("       ");
            }
            // Block number
            if (blockAddr < 10)
                printf("   "); // Pad with spaces
            else {
                if (blockAddr < 100)
                    printf("  "); // Pad with spaces
                else
                    printf(" "); // Pad with spaces
            }
            printf("%02X", blockAddr);
            printf("  ");
            // Establish encrypted communications before reading the first block
            if (isSectorTrailer) {
                status = PCD_Authenticate(PICC_CMD_MF_AUTH_KEY_A, (byte) firstBlock, key, uid);
                if (status != STATUS_OK) {
                    printf("PCD_Authenticate() failed: ");
                    printf("%s\n", GetStatusCodeName(status));
                    return;
                }
            }
            // Read block
            byteCount = buffer.length;
            status = MIFARE_Read((byte) blockAddr, buffer, byteCount);
            if (status != STATUS_OK) {
                printf("MIFARE_Read() failed: ");
                printf("%s\n", GetStatusCodeName(status));
                continue;
            }
            // Dump data
            for (byte index = 0; index < 16; index++) {
                if (buffer[index] < 0x10)
                    printf(" 0");
                else
                    printf(" ");
                printf("9x%02X", buffer[index]);
                if ((index % 4) == 3) {
                    printf(" ");
                }
            }
            // Parse sector trailer data
            if (isSectorTrailer) {
                c1 = (byte) (buffer[7] >> 4);
                c2 = (byte) (buffer[8] & 0xF);
                c3 = (byte) (buffer[8] >> 4);
                c1_ = (byte) (buffer[6] & 0xF);
                c2_ = (byte) (buffer[6] >> 4);
                c3_ = (byte) (buffer[7] & 0xF);
                invertedError = (c1 != (~c1_ & 0xF)) || (c2 != (~c2_ & 0xF)) || (c3 != (~c3_ & 0xF));
                g[0] = (byte) (((c1 & 1) << 2) | ((c2 & 1) << 1) | ((c3 & 1) << 0));
                g[1] = (byte) (((c1 & 2) << 1) | ((c2 & 2) << 0) | ((c3 & 2) >> 1));
                g[2] = (byte) (((c1 & 4) << 0) | ((c2 & 4) >> 1) | ((c3 & 4) >> 2));
                g[3] = (byte) (((c1 & 8) >> 1) | ((c2 & 8) >> 2) | ((c3 & 8) >> 3));
                isSectorTrailer = false;
            }

            // Which access group is this block in?
            if (no_of_blocks == 4) {
                group = blockOffset;
                firstInGroup = true;
            } else {
                group = blockOffset / 5;
                firstInGroup = (group == 3) || (group != (blockOffset + 1) / 5);
            }

            if (firstInGroup) {
                // Print access bits
                printf(" [ ");
                printf("%02X", (g[group] >> 2) & 1);
                printf(" ");
                printf("%02X", (g[group] >> 1) & 1);
                printf(" ");
                printf("%02X", (g[group] >> 0) & 1);
                printf(" ] ");

                if (invertedError) {
                    printf(" Inverted access bits did not match! ");
                }
            }

            if (group != 3 && (g[group] == 1 || g[group] == 6)) { // Not a sector trailer, a value block
                long value = ((long) (buffer[3]) << 24) | ((long) (buffer[2]) << 16) | ((long) (buffer[1]) << 8) | (long) (buffer[0]);
                printf(" Value=");
                printf("0x%02X", value);
                printf(" Adr=");
                printf("0x%02X", buffer[12]);
            }
            printf("\n");
        }
    }

    void PICC_DumpMifareUltralightToSerial() throws IOException {
        StatusCode status;
        int byteCount;
        byte[] buffer = new byte[18];
        int i;

        printf("Page  0  1  2  3");
        // Try the mpages of the original Ultralight. Ultralight C has more pages.
        for (int page = 0; page < 16; page += 4) { // Read returns data for 4 pages at a time.
            // Read pages
            byteCount = buffer.length;
            status = MIFARE_Read((byte) page, buffer, byteCount);
            if (status != STATUS_OK) {
                printf("MIFARE_Read() failed: ");
                printf("%s\n", GetStatusCodeName(status));
                break;
            }
            // Dump data
            for (int offset = 0; offset < 4; offset++) {
                i = page + offset;
                if (i < 10)
                    printf("  "); // Pad with spaces
                else
                    printf(" "); // Pad with spaces
                printf("%02X", i);
                printf("  ");
                for (byte index = 0; index < 4; index++) {
                    i = 4 * offset + index;
                    if (buffer[i] < 0x10)
                        printf(" 0");
                    else
                        printf(" ");
                    printf("%0x%02X", buffer[i]);
                }
                printf("\n");
            }
        }
    }

    void MIFARE_SetAccessBits(byte[] accessBitBuffer,    ///< Pointer to byte 6, 7 and 8 in the sector trailer. Bytes [0..2] will be set.
                              byte g0,                ///< Access bits [C1 C2 C3] for block 0 (for sectors 0-31) or blocks 0-4 (for sectors 32-39)
                              byte g1,                ///< Access bits C1 C2 C3] for block 1 (for sectors 0-31) or blocks 5-9 (for sectors 32-39)
                              byte g2,                ///< Access bits C1 C2 C3] for block 2 (for sectors 0-31) or blocks 10-14 (for sectors 32-39)
                              byte g3                    ///< Access bits C1 C2 C3] for the sector trailer, block 3 (for sectors 0-31) or block 15 (for sectors 32-39)
    ) {
        byte c1 = (byte) (((g3 & 4) << 1) | ((g2 & 4) << 0) | ((g1 & 4) >> 1) | ((g0 & 4) >> 2));
        byte c2 = (byte) (((g3 & 2) << 2) | ((g2 & 2) << 1) | ((g1 & 2) << 0) | ((g0 & 2) >> 1));
        byte c3 = (byte) (((g3 & 1) << 3) | ((g2 & 1) << 2) | ((g1 & 1) << 1) | ((g0 & 1) << 0));

        accessBitBuffer[0] = (byte) ((~c2 & 0xF) << 4 | (~c1 & 0xF));
        accessBitBuffer[1] = (byte) (c1 << 4 | (~c3 & 0xF));
        accessBitBuffer[2] = (byte) (c3 << 4 | c2);
    }

    boolean MIFARE_OpenUidBackdoor(boolean logErrors) throws IOException {
        // Magic sequence:
        // > 50 00 57 CD (HALT + CRC)
        // > 40 (7 bits only)
        // < A (4 bits only)
        // > 43
        // < A (4 bits only)
        // Then you can write to sector 0 without authenticating

        PICC_HaltA(); // 50 00 57 CD

        byte[] cmd = {0x40};
        IntReference validBits = new IntReference(7); /* Our command is only 7 bits. After receiving card response,
			 this will contain amount of valid response bits. */
        byte[] response = new byte[32]; // Card's response is written here
        byte received = 0;
        StatusCode status = PCD_TransceiveData(cmd, (byte) 1, response, received, validBits, (byte) 0, false); // 40
        if (status != STATUS_OK) {
            if (logErrors) {
                printf("Card did not respond to 0x40 after HALT command. Are you sure it is a UID changeable one?");
                printf("Error name: ");
                printf("%s", GetStatusCodeName(status));
            }
            return false;
        }
        if (received != 1 || response[0] != 0x0A) {
            if (logErrors) {

                printf("Got bad response on backdoor 0x40 command: ");
                printf("0x%02X", response[0]);
                printf(" (");
                printf("%02X", validBits);
                printf(" valid bits)\r\n");
            }
            return false;
        }

        cmd = new byte[]{0x43};
        validBits = new IntReference(8);
        status = PCD_TransceiveData(cmd, (byte) 1, response, received, validBits, (byte) 0, false); // 43
        if (status != STATUS_OK) {
            if (logErrors) {
                printf("Error in communication at command 0x43, after successfully executing 0x40");
                printf("Error name: ");
                printf("%s\n", GetStatusCodeName(status));
            }
            return false;
        }
        if (received != 1 || response[0] != 0x0A) {
            if (logErrors) {
                printf("Got bad response on backdoor 0x43 command: ");
                printf("%02X", response[0]);
                printf(" (");
                printf("%02X", validBits);
                printf(" valid bits)\r\n");
            }
            return false;
        }

        // You can now write to sector 0 without authenticating!
        return true;
    }

    boolean MIFARE_SetUid(byte[] newUid, byte uidSize, boolean logErrors) throws IOException {

        // UID + BCC byte can not be larger than 16 together
        if (newUid == null || uidSize < 0 || uidSize > 15) {
            if (logErrors) {
                printf("New UID buffer empty, size 0, or size > 15 given");
            }
            return false;
        }

        // Authenticate for reading
        MIFARE_Key key = new MIFARE_Key(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
        StatusCode status = PCD_Authenticate(PICC_CMD_MF_AUTH_KEY_A, (byte) 1, key, uid);
        if (status != STATUS_OK) {

            if (status == STATUS_TIMEOUT) {
                // We get a read timeout if no card is selected yet, so let's select one

                // Wake the card up again if sleeping
                //			  byte atqa_answer[2];
                //			  byte atqa_size = 2;
                //			  PICC_WakeupA(atqa_answer, &atqa_size);

                if (!PICC_IsNewCardPresent() || !PICC_ReadCardSerial()) {
                    printf("No card was previously selected, and none are available. Failed to set UID.");
                    return false;
                }

                status = PCD_Authenticate(PICC_CMD_MF_AUTH_KEY_A, (byte) 1, key, uid);
                if (status != STATUS_OK) {
                    // We tried, time to give up
                    if (logErrors) {
                        printf("Failed to authenticate to card for reading, could not set UID: ");
                        printf("%s\n", GetStatusCodeName(status));
                    }
                    return false;
                }
            } else {
                if (logErrors) {
                    printf("PCD_Authenticate() failed: ");
                    printf("%s\n", GetStatusCodeName(status));
                }
                return false;
            }
        }

        // Read block 0
        byte[] block0_buffer = new byte[18];
        int byteCount = block0_buffer.length;
        status = MIFARE_Read((byte) 0, block0_buffer, byteCount);
        if (status != STATUS_OK) {
            if (logErrors) {
                printf("MIFARE_Read() failed: ");
                printf("%s\n", GetStatusCodeName(status));
                printf("Are you sure your KEY A for sector 0 is 0xFFFFFFFFFFFF?");
            }
            return false;
        }

        // Write new UID to the data we just read, and calculate BCC byte
        byte bcc = 0;
        for (int i = 0; i < uidSize; i++) {
            block0_buffer[i] = newUid[i];
            bcc ^= newUid[i];
        }

        // Write BCC byte to buffer
        block0_buffer[uidSize] = bcc;

        // Stop encrypted traffic so we can send raw bytes
        PCD_StopCrypto1();

        // Activate UID backdoor
        if (!MIFARE_OpenUidBackdoor(logErrors)) {
            if (logErrors) {
                printf("Activating the UID backdoor failed.");
            }
            return false;
        }

        // Write modified block 0 back to card
        status = MIFARE_Write((byte) 0, block0_buffer, (byte) 16);
        if (status != STATUS_OK) {
            if (logErrors) {
                printf("MIFARE_Write() failed: ");
                printf("%s\n", GetStatusCodeName(status));
            }
            return false;
        }

        // Wake the card up again
        byte[] atqa_answer = new byte[2];
        byte atqa_size = 2;
        PICC_WakeupA(atqa_answer, atqa_size);

        return true;
    }

    boolean MIFARE_UnbrickUidSector(boolean logErrors) throws IOException {
        MIFARE_OpenUidBackdoor(logErrors);

        byte[] block0_buffer = {0x01, 0x02, 0x03, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        // Write modified block 0 back to card
        StatusCode status = MIFARE_Write((byte) 0, block0_buffer, (byte) 16);
        if (status != STATUS_OK) {
            if (logErrors) {
                printf("MIFARE_Write() failed: ");
                printf("%s\n", GetStatusCodeName(status));
            }
            return false;
        }
        return true;
    }

    boolean PICC_IsNewCardPresent() throws IOException {
        byte[] bufferATQA = new byte[2];
        int bufferSize = bufferATQA.length;
        StatusCode result = PICC_RequestA(bufferATQA, bufferSize);
        return (result == STATUS_OK || result == STATUS_COLLISION);
    }

    public boolean PICC_ReadCardSerial() throws IOException {
        StatusCode result = PICC_Select(uid, (byte) 0);
        return (result == STATUS_OK);
    }

    private static void delay(int ms) {
        long gotup = System.currentTimeMillis() + ms;
        while (System.currentTimeMillis() < gotup) {
            Thread.yield();
        }
    }

    private static void printf(String s) {
        System.out.print(s);
    }

    private static void printf(String f, Object o) {
        System.out.printf(f, o);
    }

    public Uid uid() {
        return uid;
    }


    public final static class Uid {
        byte size;            // Number of bytes in the UID. 4, 7 or 10.
        byte[] uidByte = new byte[10];
        byte sak;            // The SAK (Select acknowledge) byte returned from the PICC after successful selection.

        public byte sak() {
            return sak;
        }

        public byte[] value() {
            return uidByte;
        }

        public byte size() {
            return size;
        }
    }

    public final static class MIFARE_Key {
        byte[] keyByte = new byte[MIFARE_Misc.MF_KEY_SIZE.value];


        public MIFARE_Key() {
        }

        public MIFARE_Key(byte[] keyByte) {
            this.keyByte = keyByte;
        }
    }
}
