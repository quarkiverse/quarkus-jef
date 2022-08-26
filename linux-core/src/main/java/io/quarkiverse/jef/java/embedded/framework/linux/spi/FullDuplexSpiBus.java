
package io.quarkiverse.jef.java.embedded.framework.linux.spi;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils.dump;

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

        int result = Ioctl.getInstance().ioctl(fd, data);
        if(result < 0) {
            throw new NativeIOException("Unable to read/write data to: " + bus);
        }

        log.log(Level.FINEST, () -> StringUtils.dump(output));

        return output.asReadOnlyBuffer();
    }
}
