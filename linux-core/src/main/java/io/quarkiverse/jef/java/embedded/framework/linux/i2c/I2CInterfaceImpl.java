
package io.quarkiverse.jef.java.embedded.framework.linux.i2c;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils.dump;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils;

/**
 * I2CInterface represent auto-selecting functionality for the I2C device and provides
 * ability to read/write operation over I2C bus or {@link SMBusImpl} interface
 */
public class I2CInterfaceImpl implements I2CInterface {
    private static final Logger log = Logger.getLogger(I2CInterfaceImpl.class.getName());

    final I2CBusImpl bus;
    private final SMBusImpl smBus;
    private final FileHandle fd;
    private final int address;

    /**
     * Allocate new instance of I2C Interface
     *
     * @param bus parent {@link I2CBusImpl}
     * @param fd {@link FileHandle} to used {@link I2CBusImpl}
     * @param address address of device
     */
    I2CInterfaceImpl(I2CBusImpl bus, FileHandle fd, int address) {
        log.log(
                Level.FINEST,
                () -> String.format(
                        "create I2C Interface for bus '%s' file id '%d' and address '%d'",
                        bus.getPath(), fd.getHandle(), address));

        this.bus = bus;
        this.fd = fd;
        this.address = address;
        this.smBus = new SMBusImpl(this);
    }

    /**
     * Returns {@link SMBusImpl} interface for current I2C device
     *
     * @return SMBus interface
     */
    @Override
    public SMBus getSmBus() {
        return smBus;
    }

    /**
     * Reads data from I2C device to buffer capacity
     *
     * @param buf allocated buffer
     * @throws NativeIOException if I2C bus reject command
     */
    @Override
    public void read(ByteBuffer buf) throws NativeIOException {
        read(buf, buf.capacity());
    }

    /**
     * Reads data from I2C device to buffer capacity
     *
     * @param buf allocated buffer
     * @param length number of bytes to read
     * @throws NativeIOException if I2C bus reject command
     */
    @Override
    public void read(ByteBuffer buf, int length) throws NativeIOException {
        log.log(
                Level.FINEST,
                () -> String.format(
                        "reading '%d' bytes from bus '%s'",
                        length, bus.getPath()));
        synchronized (synchLock()) {
            synchSelect();
            int read = Fcntl.getInstance().read(getFD(), LinuxUtils.toBytes(buf), length);
            if (read < 0) {
                throw new NativeIOException("Unable to read from i2c bus: " + this.bus.getPath());
            }
        }
    }

    /**
     * Writes data from buffer to selected I2C device
     *
     * @param buf buffer with data
     * @throws NativeIOException if I2C bus reject command
     */
    @Override
    public void write(ByteBuffer buf) throws NativeIOException {
        write(buf, buf.capacity());
    }

    /**
     * Writes data from buffer to selected I2C device
     *
     * @param buf buffer with data
     * @param length amount of data to write
     * @throws NativeIOException if I2C bus reject command
     */
    @Override
    public void write(ByteBuffer buf, int length) throws NativeIOException {
        log.log(
                Level.FINEST,
                () -> String.format(
                        "write '%d' bytes to bus '%s'",
                        length, bus.getPath()));
        log.log(Level.FINEST, StringUtils.dump(buf));

        synchronized (synchLock()) {
            synchSelect();
            int result = Fcntl.getInstance().write(getFD(), LinuxUtils.toBytes(buf), length);
            if (result < 0) {
                throw new NativeIOException("Unable to write to i2c interface:" + this.bus.getPath());
            }
        }
    }

    /**
     * Lock decorator for selected device synchronization
     *
     * @return lock object
     */
    @Override
    public Object synchLock() {
        return bus;
    }

    /**
     * Selecting current device in I2C bus if it's changed
     *
     * @throws NativeIOException if I2C bus reject command
     */
    @Override
    public void synchSelect() throws NativeIOException {
        bus.selectSlave(address, false);
    }

    /**
     * Returns {@link FileHandle} to I2C bus
     *
     * @return file handle to current I2C bus
     */
    @Override
    public FileHandle getFD() {
        return fd;
    }

    /**
     * Returns path to I2C device
     *
     * @return path to I2C device
     */
    @Override
    public String getPath() {
        return bus.getPath();
    }

    /**
     * Returns parent {@link I2CBusImpl} for current interface
     *
     * @return parent I2C bus
     */
    @Override
    public I2CBus getI2CBus() {
        return bus;
    }

    /**
     * Returns I2C address for current interface device
     *
     * @return i2c address of device
     */
    @Override
    public int getAddress() {
        return address;
    }
}
