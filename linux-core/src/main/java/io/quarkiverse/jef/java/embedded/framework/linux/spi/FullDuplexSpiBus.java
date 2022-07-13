
package io.quarkiverse.jef.java.embedded.framework.linux.spi;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils.dump;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.SpiIocTransfer;
import io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils;

/**
 * The {@link FullDuplexSpiBus} class provides methods for transmitting and receiving data to/from an SPI slave device.
 * On an SPI bus, data is transferred between the SPI master device and an SPI slave device in full duplex. That is, data is
 * transmitted by the SPI master to the SPI slave device at the same time data is received from the SPI slave device by the SPI
 * master.
 *
 */
@SuppressWarnings("unused")
public class FullDuplexSpiBus extends AbstractSpiBus {
    private static final Logger log = Logger.getLogger(FullDuplexSpiBus.class.getName());

    public FullDuplexSpiBus(int busNumber) throws NativeIOException {
        super(busNumber);
    }

    public FullDuplexSpiBus(String bus) throws NativeIOException {
        super(bus);
    }

    public FullDuplexSpiBus(int busNumber, int clockFrequency, SpiMode clockMode, int wordLength, int bitOrdering)
            throws NativeIOException {
        super(busNumber, clockFrequency, clockMode, wordLength, bitOrdering);
    }

    public FullDuplexSpiBus(String bus, int clockFrequency, SpiMode clockMode, int wordLength, int bitOrdering)
            throws NativeIOException {
        super(bus, clockFrequency, clockMode, wordLength, bitOrdering);
    }

    /**
     * Read data from SPI bus based on requested parameters
     *
     * @param inputParams request with input parameters. See {@link SpiInputParams}
     * @return buffer with response from SPI bus
     * @throws NativeIOException if SPI bus discard request
     */
    @Override
    public int readByteData(SpiInputParams inputParams) throws IOException {
        log.log(Level.FINEST, () -> "read byte data");
        return readWriteData(inputParams.getFinal(), 1).get();
    }

    /**
     * Write data to SPI bus
     *
     * @param inputParams request with input parameters. See {@link SpiInputParams}
     * @throws NativeIOException if SPI bus discard request
     */
    @Override
    public void writeByteData(SpiInputParams inputParams) throws NativeIOException {
        log.log(Level.FINEST, () -> "write byte data");
        readWriteData(inputParams.getFinal(), 0);
    }

    /**
     * Read array from SPI bus
     *
     * @param inputParams request with input parameters. See {@link SpiInputParams}
     * @param outputSize expected size of response
     * @return buffer with response from SPI bus
     * @throws NativeIOException if SPI bus discard request
     */
    @Override
    public ByteBuffer readArray(SpiInputParams inputParams, int outputSize) throws NativeIOException {
        log.log(Level.FINEST, () -> "read array");
        return readWriteData(
                inputParams.getFinal(),
                outputSize);
    }

    @Override
    public ByteBuffer readWriteData(ByteBuffer input, int outputSize) throws NativeIOException {
        input.position(0);

        ByteBuffer output = ByteBuffer
                .allocateDirect(outputSize);

        log.log(Level.FINEST, () -> StringUtils.dump(input));

        SpiIocTransfer data = new SpiIocTransfer(
                input,
                output,
                input.capacity() + outputSize,
                getClockFrequency(),
                //clockFrequency,
                (short) 0,
                //(byte) wordLength
                (byte) getWordLength());

        Ioctl.getInstance().ioctl(fd, data);

        log.log(Level.FINEST, () -> StringUtils.dump(output));

        return output.asReadOnlyBuffer();
    }
}
