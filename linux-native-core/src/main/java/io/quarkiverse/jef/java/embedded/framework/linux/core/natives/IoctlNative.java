package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils.checkIOResult;
import static io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils.dump;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.graalvm.nativeimage.PinnedObject;
import org.graalvm.nativeimage.UnmanagedMemory;
import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.constant.CConstant;
import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.function.CLibrary;
import org.graalvm.nativeimage.c.struct.SizeOf;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CIntPointer;
import org.graalvm.nativeimage.c.type.CLongPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.word.PointerBase;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.SmBusConstants;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.gpio.GpioNativeStructures.gpiochip_info;
import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.gpio.GpioNativeStructures.gpiohandle_data;
import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.gpio.GpioNativeStructures.gpiohandle_request;
import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.gpio.GpioNativeStructures.gpioline_info;
import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.i2c.I2CData;
import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.spi.SpiIocTransferNative;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.*;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioChipInfo;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioHandleData;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioHandleRequest;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioLineInfo;

@CContext(IoctlNativeHeaders.class)
@CLibrary("c")
public class IoctlNative extends Ioctl {
    private static final Logger log = Logger.getLogger(IoctlNative.class.getName());

    @CConstant
    private static native long _IOC_NRBITS();

    @CConstant
    private static native long _IOC_TYPEBITS();

    @CConstant
    private static native long _IOC_SIZEBITS();

    @CConstant
    private static native long _IOC_DIRBITS();

    @CConstant
    private static native long _IOC_NRSHIFT();

    @CConstant
    private static native long _IOC_NONE();

    @CConstant
    private static native long _IOC_READ();

    @CConstant
    private static native long _IOC_WRITE();

    @CConstant
    private static native long SPI_IOC_MAGIC();

    /*
     * @CConstant
     * private static native int GPIO_MAX_NAME_SIZE();
     */

    @Override
    public boolean isNativeSupported() {
        return true;
    }

    @Override
    public int ioctl(FileHandle fd, long command, long arg) throws NativeIOException {
        log.log(
                Level.FINEST,
                String.format(
                        "ioctl by long fd is '%d' command: '%8h' arg: '0x%8h'",
                        fd.getHandle(), command, arg));
        int result = Delegate.ioctl(fd.getHandle(), command, arg);
        checkIOResult("ioctl:long", result);
        return result;
    }

    @Override
    public int ioctl(FileHandle fd, long command, LongReference arg) throws NativeIOException {
        log.log(
                Level.FINEST,
                String.format(
                        "ioctl by long reference fd is '%d' command is '%8h' arg is '0x%8h'",
                        fd.getHandle(), command, arg.getValue()));

        CLongPointer ptr = UnmanagedMemory.malloc(8);
        try {
            int result = Delegate.ioctl(fd.getHandle(), command, ptr);
            checkIOResult("ioctl:LongRef", result);

            long refValue = ptr.read();
            arg.setValue(refValue);

            log.log(
                    Level.FINEST,
                    String.format(
                            "ioctl by long reference fd is '%d' command is '%8h' arg is '0x%8h' results '%d'",
                            fd.getHandle(), command, arg.getValue(), refValue));

            return result;
        } finally {
            UnmanagedMemory.free(ptr);
        }
    }

    @Override
    public int ioctl(FileHandle fd, long command, IntReference arg) throws NativeIOException {
        log.log(
                Level.FINEST,
                String.format(
                        "ioctl by int reference fd is '%d' command is '%8h' arg is '0x%8h'",
                        fd.getHandle(), command, arg.getValue()));

        CIntPointer ptr = UnmanagedMemory.malloc(4);
        try {
            int result = Delegate.ioctl(fd.getHandle(), command, ptr);
            log.log(Level.FINEST, () -> String.format("ioctl result is '%s'", result));
            checkIOResult("ioctl:IntRef", result);
            int refValue = ptr.read();
            arg.setValue(refValue);

            log.log(
                    Level.FINEST,
                    String.format(
                            "ioctl by int reference fd is '%d' command is '%8h' arg is '0x%8h' results '%d'",
                            fd.getHandle(), command, arg.getValue(), refValue));

            return result;
        } finally {
            UnmanagedMemory.free(ptr);
        }
    }

    @Override
    public int ioctl(FileHandle fd, long command, SmbusIoctlData ptr) throws NativeIOException {
        log.log(Level.FINEST, () -> String.format("ioctl.smbus fd is '%d' data is '%s'", fd.getHandle(), ptr));

        I2CData.I2CSmbusIoctlData ioctl = UnmanagedMemory.malloc(
                SizeOf.get(I2CData.I2CSmbusIoctlData.class));

        I2CData.I2CSmbusData smbusData = UnmanagedMemory.malloc(
                SizeOf.get(I2CData.I2CSmbusData.class));

        try {
            SmbusData data = ptr.getData();
            ByteBuffer block = ByteBuffer.wrap(data.getBlock());
            ByteBuffer source = CTypeConversion.asByteBuffer(smbusData, SmBusConstants.BUFFER_SIZE);

            /*
             * if (ptr.getReadWrite() == I2C_SMBUS_WRITE) {
             * smbusData.setByte(data.getByte());
             * }
             */
            log.log(Level.FINEST, "ioctl.smbus input block: ");
            log.log(Level.FINEST, () -> dump(block));

            block.position(0);
            source.put(block);

            ioctl.setReadWrite(ptr.getReadWrite());
            ioctl.setCommand((byte) command); // read register
            ioctl.setSize(ptr.getSize());
            ioctl.getData().write(smbusData);

            int result = Delegate.ioctl(fd.getHandle(), SmBusConstants.I2C_SMBUS, ioctl);
            checkIOResult("ioctl:SmBus", result);

            block.position(0);
            source.position(0);
            block.put(source);

            log.log(Level.FINEST, "ioctl.smbus input block: ");
            log.log(Level.FINEST, () -> dump(block));

            return result;
        } finally {
            UnmanagedMemory.free(smbusData);
            UnmanagedMemory.free(ioctl);
        }
    }

    @Override
    public int ioctl(FileHandle fd, SpiIocTransfer ptr) throws NativeIOException {
        log.log(Level.FINEST, () -> String.format("ioctl.spi fd is '%d' data is '%s'", fd.getHandle(), ptr));

        ByteBuffer txBuffer = ptr.getTxBuffer();
        ByteBuffer rxBuffer = ptr.getRxBuffer();

        int txSize = txBuffer.capacity();
        int rxSize = rxBuffer.capacity();

        log.log(Level.FINEST, () -> String.format("dump input array \n%s", dump(txBuffer)));

        byte[] buffer = new byte[txSize + rxSize];
        System.arraycopy(LinuxUtils.toBytes(txBuffer), 0, buffer, 0, txSize);

        SpiIocTransferNative.spi_ioc_transfer spi = UnmanagedMemory.malloc(
                SizeOf.get(SpiIocTransferNative.spi_ioc_transfer.class));

        log.log(Level.FINEST, () -> String.format("pinned array \n%s", dump(buffer)));

        try (PinnedObject pin = PinnedObject.create(buffer)) {
            CCharPointer buf = pin.addressOfArrayElement(0);
            spi.setTxBuffer(buf);
            spi.setRxBuffer(buf);
            spi.setLength(ptr.getLength());
            spi.setSpeed(ptr.getSpeed());
            spi.setDelay(ptr.getDelay());
            spi.setBitsPerWord(ptr.getBitsPerWord());
            //spi.setCsChange(ptr.getCsChange());
            //spi.setPad(ptr.getPad());

            long ioc_message = SPI_IOC_MESSAGE(1);
            log.log(Level.FINEST, () -> String.format("ioc_message is '%s'", ioc_message));

            int result = Delegate.ioctl(fd.getHandle(), ioc_message/* command */, spi);
            log.log(Level.FINEST, () -> String.format("ioctl result is '%s'", result));

            checkIOResult("ioctl:SPI", result);

            ByteBuffer out = CTypeConversion.asByteBuffer(buf.addressOf(txSize), rxSize);

            rxBuffer.put(out).position(0);

            log.log(Level.FINEST, () -> String.format("dump output array \n%s", dump(rxBuffer)));

            return result;
        } finally {
            UnmanagedMemory.free(spi);
        }
    }

    @Override
    public int ioctl(FileHandle handle, long command, GpioHandleRequest request) throws NativeIOException {
        gpiohandle_request struct = UnmanagedMemory.malloc(
                SizeOf.get(gpiohandle_request.class));

        try {
            //TODO fill gpiohandle_request params
            int[] ints = request.getLineOffsets();
            if (ints != null) {
                for (int index = 0; index < ints.length; index++) {
                    struct.lineOffsets().write(index, ints[index]);
                }
            }

            struct.flags(request.getFlags());
            struct.lines(request.getLines());

            byte[] label = request.getConsumerLabel();
            if (label != null) {
                for (int index = 0; index < label.length; index++) {
                    struct.consumerLabel().write(index, label[index]);
                }
            }

            byte[] defs = request.getDefaultValues();
            if (defs != null) {
                for (int index = 0; index < defs.length; index++) {
                    struct.defaultValues().write(index, defs[index]);
                }
            }

            int result = Delegate.ioctl(handle.getHandle(), command, struct);
            checkIOResult("ioctl:gpio_handle_request", result);
            request.setFd(struct.fd());
            return result;
        } finally {
            UnmanagedMemory.free(struct);
        }
    }

    @Override
    public int ioctl(FileHandle handle, long command, GpioChipInfo info) throws NativeIOException {
        gpiochip_info struct = UnmanagedMemory.malloc(
                SizeOf.get(gpiochip_info.class));
        try {
            int result = Delegate.ioctl(handle.getHandle(), command, struct);
            info.setName(CTypeConversion.toJavaString(struct.name()));
            info.setLabel(CTypeConversion.toJavaString(struct.label()));
            info.setLines(struct.lines());
            checkIOResult("ioctl:gpio_chip_info", result);
            return result;
        } finally {
            UnmanagedMemory.free(struct);
        }
    }

    @Override
    public int ioctl(FileHandle handle, long command, GpioLineInfo line) throws NativeIOException {
        gpioline_info struct = UnmanagedMemory.malloc(
                SizeOf.get(gpioline_info.class));
        try {
            struct.offset(line.getOffset());
            int result = Delegate.ioctl(handle.getHandle(), command, struct);

            checkIOResult("ioctl:gpio_line_info", result);

            line.setFlags(struct.flags());
            line.setName(CTypeConversion.toJavaString(struct.name()));
            line.setConsumer(CTypeConversion.toJavaString(struct.consumer()));

            return result;
        } finally {
            UnmanagedMemory.free(struct);
        }
    }

    @Override
    public int ioctl(int fd, long command, GpioHandleData handleData) throws NativeIOException {
        Objects.requireNonNull(handleData);
        gpiohandle_data data = UnmanagedMemory.malloc(
                SizeOf.get(gpiohandle_data.class));

        byte[] from = handleData.getValues();
        CCharPointer to = data.values();
        for (int i = 0; i < from.length; i++) {
            to.write(i, from[i]);
        }

        int result = Delegate.ioctl(fd, command, data);
        checkIOResult("ioctl:byte_array_ref", result);

        for (int i = 0; i < from.length; i++) {
            from[i] = to.read(i);
        }

        return result;
    }

    @Override
    protected long GET_SPI_IOC_MAGIC() {
        return SPI_IOC_MAGIC();
    }

    @Override
    protected long IOC_NRBITS() {
        return _IOC_NRBITS();
    }

    @Override
    protected long IOC_TYPEBITS() {
        return _IOC_TYPEBITS();
    }

    @Override
    protected long IOC_SIZEBITS() {
        return _IOC_SIZEBITS();
    }

    @Override
    protected long IOC_DIRBITS() {
        return _IOC_DIRBITS();
    }

    @Override
    protected long IOC_NRSHIFT() {
        return _IOC_NRSHIFT();
    }

    @Override
    protected long IOC_NONE() {
        return _IOC_NONE();
    }

    @Override
    protected long IOC_READ() {
        return _IOC_READ();
    }

    @Override
    protected long IOC_WRITE() {
        return _IOC_WRITE();
    }

    @Override
    protected int SPI_MSGSIZE(int N) {
        return ((((N) * (SizeOf.get(SpiIocTransferNative.spi_ioc_transfer.class))) < (1 << IOC_SIZEBITS()))
                ? ((N) * (SizeOf.get(SpiIocTransferNative.spi_ioc_transfer.class)))
                : 0);
    }

    private static class Delegate {
        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        private static native int ioctl(int fd, long command, long arg);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        private static native int ioctl(int fd, long command, CLongPointer p);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int ioctl(int fd, long command, PointerBase ptr);
    }
}
