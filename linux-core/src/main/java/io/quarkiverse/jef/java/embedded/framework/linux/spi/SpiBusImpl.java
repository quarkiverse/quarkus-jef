
package io.quarkiverse.jef.java.embedded.framework.linux.spi;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils.dump;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.IntReference;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.SpiIocTransfer;
import io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils;

/**
 * The {@link SpiBusImpl} class provides methods for transmitting and receiving data to/from an SPI slave device.
 * On an SPI bus, data is transferred between the SPI master device and an SPI slave device in full duplex. That is, data is
 * transmitted by the SPI master to the SPI slave device at the same time data is received from the SPI slave device by the SPI
 * master.
 *
 */
@SuppressWarnings("unused")
public class SpiBusImpl implements SpiBus {
    private static final Logger log = Logger.getLogger(SpiBusImpl.class.getName());

    private final String bus;
    private final FileHandle fd;

    private final Ioctl console = Ioctl.getInstance();
    private SpiMode currentClockMode;
    private int currentBitOrdering;
    private int currentWordLength;
    private int currentClockFrequency;

    public SpiBusImpl(int busNumber) throws NativeIOException {
        this("/dev/spidev0." + busNumber);
    }

    public SpiBusImpl(String bus) throws NativeIOException {
        this.bus = bus;
        log.log(Level.INFO, () -> String.format("Open SPI Bus '%s'", bus));
        fd = Fcntl.getInstance().open(bus, EnumSet.of(IOFlags.O_RDWR));
        reload();
    }

    /**
     * Allocates new instance of {@link SpiBusImpl} based on interface ID
     *
     * @param busNumber number of available bus (0, 1, 2, ...)
     * @param clockFrequency working frequency
     * @param clockMode clock mode. see {@link SpiMode}
     * @param wordLength length of word. Typically it's 8-bits.
     * @param bitOrdering bit ordering. 0 - {@link java.nio.ByteOrder#BIG_ENDIAN} or 1 -
     *        {@link java.nio.ByteOrder#LITTLE_ENDIAN}
     * @throws NativeIOException if got error from Linux subsystem during initialization
     */
    public SpiBusImpl(int busNumber, int clockFrequency, SpiMode clockMode, int wordLength, int bitOrdering)
            throws NativeIOException {
        this("/dev/spidev0." + busNumber, clockFrequency, clockMode, wordLength, bitOrdering);
    }

    /**
     * Allocates new instance of {@link SpiBusImpl} based on interface name
     *
     * @param bus path to bus in Linux file system i.e '/dev/spidev0.x'
     * @param clockFrequency working frequency
     * @param clockMode clock mode. see {@link SpiMode}
     * @param wordLength length of word. Typically it's 8-bits.
     * @param bitOrdering bit ordering. 0 - {@link java.nio.ByteOrder#BIG_ENDIAN} or 1 -
     *        {@link java.nio.ByteOrder#LITTLE_ENDIAN}
     * @throws NativeIOException if got error from Linux subsystem during initialization
     */
    public SpiBusImpl(String bus, int clockFrequency, SpiMode clockMode, int wordLength, int bitOrdering)
            throws NativeIOException {
        this(bus);
        log.log(Level.INFO,
                () -> String.format(
                        "Create SPI bus with Bus: '%s' Clock Frequency: '%d' Spi Mode: '%s' Word Length: '%d' Bit Ordering: '%d'",
                        bus, clockFrequency, clockMode, wordLength, bitOrdering));
        initSPIHandler(clockFrequency, clockMode, wordLength, bitOrdering);
    }

    /**
     * Path to bus in Linux
     *
     * @return path to bus in Linux file system
     */
    @Override
    public String getBus() {
        return bus;
    }

    /**
     * {@link FileHandle of bus in Linux file system}
     *
     * @return handler of bus in Linux FS
     */
    @Override
    public FileHandle getFd() {
        return fd;
    }

    /**
     * Bus clock frequency
     *
     * @return clock frequency
     */
    @Override
    public int getClockFrequency() throws NativeIOException {
        return currentClockFrequency;

    }

    @Override
    public void setClockFrequency(int value) throws NativeIOException {
        IntReference arg = new IntReference();
        arg.setValue(value);
        console.ioctl(fd, console.getSpiIocWrMaxSpeedHz(), arg);
        currentClockFrequency = value;
    }

    /**
     * Bus clock mode {@link SpiMode}
     *
     * @return clock mode
     */
    @Override
    public SpiMode getClockMode() throws NativeIOException {
        return this.currentClockMode;
    }

    @Override
    public void setClockMode(SpiMode clockMode) throws NativeIOException {
        IntReference arg = new IntReference();
        arg.setValue(clockMode.value);
        console.ioctl(fd, console.getSpiIocWrMode(), arg);
        this.currentClockMode = clockMode;
    }

    /**
     * SPI Bus word length
     *
     * @return word length
     */
    @Override
    public int getWordLength() throws NativeIOException {
        return currentWordLength;
    }

    /**
     * SPI bus bit ordering. 0 - {@link java.nio.ByteOrder#BIG_ENDIAN} or 1 - {@link java.nio.ByteOrder#LITTLE_ENDIAN}
     *
     * @return bus bit ordering
     */
    @Override
    public int getBitOrdering() throws NativeIOException {
        return this.currentBitOrdering;
    }

    @Override
    public void setBitOrdering(int bitOrdering) throws NativeIOException {
        IntReference arg = new IntReference();
        arg.setValue(bitOrdering);
        //        SPI_IOC_WR_LSB_FIRST
        console.ioctl(fd, console.getSpiIocWrLsbFirst(), arg);
        this.currentBitOrdering = bitOrdering;
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
    public void setWordLength(int wordLength) throws NativeIOException {
        IntReference arg = new IntReference();
        arg.setValue(wordLength);
        console.ioctl(fd, console.getSpiIocWrBitsPerWord(), arg);
        this.currentWordLength = wordLength;
    }

    private void initSPIHandler(int clockFrequency, SpiMode clockMode, int wordLength, int bitOrdering)
            throws NativeIOException {
        reload();

        log.log(Level.INFO, () -> "Read current clock value");
        this.currentClockMode = getClockMode();
        log.log(Level.INFO, () -> "current clock is " + this.currentClockMode);

        if (!currentClockMode.equals(clockMode)) {
            log.log(Level.INFO, () -> "Setup clock variable");
            setClockMode(clockMode);
        } else {
            log.log(Level.INFO, () -> "Current clock is the same");
        }

        log.log(Level.INFO, () -> "Read current bits per word");
        this.currentWordLength = getWordLength();

        log.log(Level.INFO, () -> "current bits per word is " + this.currentWordLength);

        if (this.currentWordLength != wordLength) {
            log.log(Level.INFO, () -> "Setup bits per word");
            setWordLength(wordLength);
        } else {
            log.log(Level.INFO, () -> "Current bits per word is the same");
        }

        log.log(Level.INFO, () -> "Read current max speed");
        this.currentClockFrequency = getClockFrequency();
        //console.ioctl(fd, console.getSpiIocRdMaxSpeedHz(), variable);
        log.log(Level.INFO, () -> "current max speed is " + this.currentClockFrequency);

        if (this.currentClockFrequency != clockFrequency) {
            SpiBusImpl.log.log(Level.INFO, () -> "Setup max speed");
            setClockFrequency(clockFrequency);
        } else {
            log.log(Level.INFO, () -> "Current max speed is the same");
        }

        log.log(Level.INFO, () -> "Read bit ordering");
        this.currentBitOrdering = getBitOrdering();
        //console.ioctl(fd, console.getSpiIocRdMaxSpeedHz(), variable);
        log.log(Level.INFO, () -> "current bit ordering is " + this.currentBitOrdering);

        if (this.currentBitOrdering != bitOrdering) {
            log.log(Level.INFO, () -> "Setup bit ordering");
            setBitOrdering(bitOrdering);
        }

        log.log(Level.INFO, () -> String.format("SPI bus '%s' setup finished", bus));
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

    @Override
    public void reload() throws NativeIOException {
        // Clock mode
        IntReference arg = new IntReference();
        console.ioctl(fd, console.getSpiIocRdMode(), arg);
        this.currentClockMode = SpiMode.valueOf(arg.getValue());

        //SPI_IOC_RD_LSB_FIRST
        // Bit Ordering
        arg.setValue(0);
        console.ioctl(fd, console.getSpiIocRdLsbFirst(), arg);
        currentBitOrdering = arg.getValue();

        // Word Length
        arg.setValue(0);
        console.ioctl(fd, console.getSpiIocRdBitsPerWord(), arg);
        currentWordLength = arg.getValue();

        // Clock Frenq
        arg.setValue(0);
        console.ioctl(fd, console.getSpiIocRdMaxSpeedHz(), arg);
        currentClockFrequency = arg.getValue();
    }
}
