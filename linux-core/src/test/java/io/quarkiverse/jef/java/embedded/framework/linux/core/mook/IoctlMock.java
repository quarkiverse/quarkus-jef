package io.quarkiverse.jef.java.embedded.framework.linux.core.mook;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.IntReference;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.LongReference;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.SmbusIoctlData;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.SpiIocTransfer;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioChipInfo;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioHandleData;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioHandleRequest;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioLineInfo;

public class IoctlMock extends Ioctl {
    @Override
    public boolean isNativeSupported() {
        return true;
    }

    @Override
    public boolean isMock() {
        return true;
    }

    @Override
    public int ioctl(FileHandle fd, long command, long arg) throws NativeIOException {
        return 0;
    }

    @Override
    public int ioctl(FileHandle fd, long command, LongReference arg) throws NativeIOException {
        return 0;
    }

    @Override
    public int ioctl(FileHandle fd, long command, IntReference arg) throws NativeIOException {
        return 0;
    }

    @Override
    public int ioctl(FileHandle fd, long command, SmbusIoctlData ptr) throws NativeIOException {
        return 0;
    }

    @Override
    public int ioctl(FileHandle fd, SpiIocTransfer ptr) throws NativeIOException {
        return 0;
    }

    @Override
    public int ioctl(FileHandle handle, long command, GpioHandleRequest request) throws NativeIOException {
        return 0;
    }

    @Override
    public int ioctl(FileHandle handle, long command, GpioChipInfo info) throws NativeIOException {
        return 0;
    }

    @Override
    public int ioctl(FileHandle handle, long command, GpioLineInfo line) throws NativeIOException {
        return 0;
    }

    @Override
    public int ioctl(int fd, long command, GpioHandleData data) throws NativeIOException {
        return 0;
    }

    @Override
    protected long GET_SPI_IOC_MAGIC() {
        return 0;
    }

    @Override
    protected long IOC_NRBITS() {
        return 0;
    }

    @Override
    protected long IOC_TYPEBITS() {
        return 0;
    }

    @Override
    protected long IOC_SIZEBITS() {
        return 0;
    }

    @Override
    protected long IOC_DIRBITS() {
        return 0;
    }

    @Override
    protected long IOC_NRSHIFT() {
        return 0;
    }

    @Override
    protected long IOC_NONE() {
        return 0;
    }

    @Override
    protected long IOC_READ() {
        return 0;
    }

    @Override
    protected long IOC_WRITE() {
        return 0;
    }

    @Override
    protected int SPI_MSGSIZE(int N) {
        return 0;
    }
}
