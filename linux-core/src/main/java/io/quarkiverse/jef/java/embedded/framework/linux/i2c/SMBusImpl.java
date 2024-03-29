
package io.quarkiverse.jef.java.embedded.framework.linux.i2c;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.SmBusConstants;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.SmbusData;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.SmbusIoctlData;

/**
 * The System Management Bus (abbreviated to SMBus or SMB) is a single-ended simple two-wire bus for
 * the purpose of lightweight communication.
 */
@SuppressWarnings("unused")
public class SMBusImpl implements SmBusConstants, SMBus {
    private static final Logger log = Logger.getLogger(SMBusImpl.class.getName());

    private final I2CInterfaceImpl iface;
    private final byte[] buffer = new byte[BUFFER_SIZE];

    /**
     * Allocates new instance of {@link SMBusImpl} based on interface ID
     *
     * @param iface I2C interface. Please see {@link I2CInterfaceImpl}
     */
    SMBusImpl(I2CInterfaceImpl iface) {
        this.iface = iface;
    }

    /**
     * Specify packet error checking
     *
     * @param usePEC {@code true} if packet checking enabled or {@code false} otherwise
     * @throws IOException if I2C bus reject command
     */
    @Override
    public void usePEC(boolean usePEC) throws IOException {
        log.log(
                Level.INFO,
                () -> String.format(
                        "set usePEC to '%b' for bus '%s' and address '%d'",
                        usePEC, iface.getPath(), iface.getAddress()));

        synchronized (synchLock()) {
            synchSelect();
            iface.bus.ioctl(fd(), I2C_PEC, usePEC ? 1L : 0L);
        }
    }

    /**
     * This sends or reads a single bit to the device.
     *
     * @param isWrite {@code true} for write operation or {@code false} otherwise
     * @throws IOException if I2C bus reject command
     */
    @Override
    public void writeQuick(boolean isWrite) throws IOException {
        log.log(
                Level.FINEST,
                () -> String.format(
                        "set writeQuick to '%b' for bus '%s' and address '%d'",
                        isWrite, iface.getPath(), iface.getAddress()));

        synchronized (synchLock()) {
            synchSelect();
            i2cSmbusAccess(isWrite ? I2C_SMBUS_WRITE : I2C_SMBUS_READ, 0, I2C_SMBUS_QUICK, buffer);
        }
    }

    /**
     * This reads a single byte from a device, without specifying a device register. Some devices are so simple that this
     * interface is enough; for others,
     * it is a shorthand if you want to read the same register as in the previous SMBus command.
     *
     * @return {@code byte} from the current selected device
     * @throws IOException if I2C bus reject command
     */
    @Override
    @SuppressWarnings("UnusedReturnValue")
    public int readByte() throws IOException {
        synchronized (synchLock()) {
            synchSelect();
            log.log(Level.FINEST,
                    () -> String.format("read byte from bus '%s' and address '%d'", iface.getPath(), iface.getAddress()));
            i2cSmbusAccess(I2C_SMBUS_READ, 0, I2C_SMBUS_BYTE, buffer);
            byte result = buffer[0];
            log.log(Level.FINEST, () -> String.format("read byte from bus '%s' and address '%d' returns '%d'", iface.getPath(),
                    iface.getAddress(), result));
            return result;
        }
    }

    /**
     * This reads a single byte from a device, from a designated register.
     *
     * @param command register command
     * @return {@code byte} from the current selected device
     * @throws IOException if I2C bus reject command
     */
    @Override
    public int readByteData(int command) throws IOException {
        synchronized (synchLock()) {
            synchSelect();
            log.log(Level.FINEST, () -> String.format("read byte command '%d' from bus '%s' and address '%d'", command,
                    iface.getPath(), iface.getAddress()));
            i2cSmbusAccess(I2C_SMBUS_READ, command, I2C_SMBUS_BYTE_DATA, buffer);
            byte result = buffer[0];
            log.log(Level.FINEST, () -> String.format("read byte command '%d' from bus '%s' returns '%d' and address '%d'",
                    command, iface.getPath(), iface.getAddress(), result));
            return result;
        }
    }

    /**
     * This operation is very like Read Byte; again, data is read from a device, from a designated
     * register that is specified through the Comm byte.
     * But this time, the data is a complete word (16 bits)
     *
     * @param command register command
     * @return {@code short} from the current selected device
     * @throws IOException if I2C bus reject command
     */
    @Override
    public int readWordData(int command) throws IOException {
        synchronized (synchLock()) {
            synchSelect();
            log.log(Level.FINEST, () -> String.format("read short command '%d' from bus '%s' and address '%d'", command,
                    iface.getPath(), iface.getAddress()));
            i2cSmbusAccess(I2C_SMBUS_READ, command, I2C_SMBUS_WORD_DATA, buffer);
            byte hi = buffer[1];
            byte lo = buffer[0];
            short result = (short) (((hi & 0xFF) << 8) | (lo & 0xFF));
            log.log(Level.FINEST, () -> String.format("read short command '%d' from bus '%s' returns '%d' and address '%d'",
                    command, iface.getPath(), iface.getAddress(), result));
            return result;
        }
    }

    /**
     * This command reads a block of bytes from a device,
     * from a designated register that is specified through the Comm byte.
     *
     * @param command register command
     * @return array with readed data
     * @throws IOException if I2C bus reject command
     */
    @Override
    public ByteBuffer readBlockData(int command) throws IOException {
        synchronized (synchLock()) {
            synchSelect();
            i2cSmbusAccess(I2C_SMBUS_READ, command, I2C_SMBUS_BLOCK_DATA, buffer);
            int length = getUnsignedByte(buffer);
            byte[] res = new byte[length];
            System.arraycopy(buffer, 1, res, 0, length);
            return ByteBuffer.wrap(res);
        }
    }

    /**
     * This operation is the reverse of Receive Byte:
     * it sends a single byte to a device. See Receive Byte for more information
     *
     * @param b {@code byte} single byte
     * @throws IOException if I2C bus reject command
     */
    @Override
    public void writeByte(int b) throws IOException {
        synchronized (synchLock()) {
            synchSelect();
            log.log(Level.FINEST,
                    () -> String.format("write byte to bus '%s' and address '%d'", iface.getPath(), iface.getAddress()));
            i2cSmbusAccess(I2C_SMBUS_WRITE, b, I2C_SMBUS_BYTE, null);
        }
    }

    /**
     * This writes a single byte to a device, to a designated register.
     * The register is specified through the Comm byte.
     * This is the opposite of the Read Byte operation.
     *
     * @param command register command
     * @param b {@code byte} single byte
     * @throws IOException if I2C bus reject command
     */
    @Override
    public void writeByteData(int command, int b) throws IOException {
        synchronized (synchLock()) {
            synchSelect();
            setUnsignedByte(buffer, b);
            log.log(Level.FINEST, () -> String.format("write byte command '%d' value '%d' from bus '%s' and address '%d'",
                    command, b, iface.getPath(), iface.getAddress()));
            this.i2cSmbusAccess(I2C_SMBUS_WRITE, command, I2C_SMBUS_BYTE_DATA, buffer);
        }
    }

    /**
     * This is the opposite of the Read Word operation. 16 bits of data are written to a device,
     * to the designated register that is specified through the Comm byte.
     *
     * @param command register command
     * @param word {@code short} single byte
     * @throws IOException if I2C bus reject command
     */
    @Override
    public void writeWordData(int command, int word) throws IOException {
        synchronized (synchLock()) {
            synchSelect();
            setUnsignedShort(buffer, word);
            log.log(Level.FINEST, () -> String.format("write short command '%d' value '%d' from bus '%s' and address '%d'",
                    command, word, iface.getPath(), iface.getAddress()));
            this.i2cSmbusAccess(I2C_SMBUS_WRITE, command, I2C_SMBUS_WORD_DATA, buffer);
        }
    }

    /**
     * The opposite of the Block Read command, this writes up to 32 bytes to a device,
     * to a designated register that is specified through the Comm byte.
     * The amount of data is specified in the Count byte.
     *
     * @param command register command
     * @param buf input buffer
     * @throws IOException if I2C bus reject command
     */
    @Override
    public void writeBlockData(int command, ByteBuffer buf) throws IOException {
        synchronized (synchLock()) {
            synchSelect();
            int capacity = buf.capacity();
            buffer[0] = (byte) capacity;
            System.arraycopy(buf.array(), 0, buffer, 1, capacity);
            this.i2cSmbusAccess(I2C_SMBUS_WRITE, command, I2C_SMBUS_I2C_BLOCK_DATA, buffer);
        }
    }

    /**
     * Execute command in real I2C device
     *
     * @param readWrite read or write operation
     * @param command ioctl command
     * @param size size of data
     * @param data array with data
     * @throws IOException if I2C bus reject command
     */
    @Override
    public void i2cSmbusAccess(byte readWrite,
            long command,
            int size,
            byte[] data) throws IOException {
        SmbusData smbusData = new SmbusData();
        smbusData.setBlock(data);
        SmbusIoctlData ioctlData = new SmbusIoctlData(readWrite, command, size, smbusData);
        try {
            Ioctl.getInstance().ioctl(fd(), command, ioctlData);
        } catch (NativeIOException e) {
            throw new IOException("Unable to send command: '" + command + "' to " + iface.bus.getPath());
        }
    }

    /**
     * Gets SMBus {@link I2CInterfaceImpl}
     *
     * @return i2c interface
     */
    @Override
    public I2CInterface getInterface() {
        return iface;
    }

    /**
     * Gets SMBus {@link FileHandle}
     *
     * @return file handle
     */
    @Override
    public FileHandle fd() {
        return iface.getFD();
    }

    static int getUnsignedByte(byte[] p) {
        return p[0] & 0x000000FF;
    }

    static int getUnsignedShort(ByteBuffer p) {
        return p.getShort(0) & 0x0000FFFF;
    }

    static void setUnsignedByte(byte[] p, int b) {
        p[0] = (byte) b;
    }

    static void setUnsignedShort(byte[] p, int word) {
        ByteBuffer.wrap(p).putShort(0, (short) word);
    }

    Object synchLock() {
        return iface.synchLock();
    }

    void synchSelect() throws NativeIOException {
        iface.synchSelect();
    }
}
