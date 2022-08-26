package io.quarkiverse.jef.java.embedded.framework.linux.gpio;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags.O_CLOEXEC;
import static io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags.O_RDONLY;

import java.io.IOException;
import java.util.EnumSet;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

public class GpioPinImpl implements GpioPin {
    private final int flags;
    private final String key;
    private final String path;
    private final int number;
    private final String name;
    private final String consumer;
    private final boolean locked;

    private Direction direction;
    private int handle;
    private boolean closed;

    GpioPinImpl(String key, String path, int number) throws NativeIOException {
        this.path = path;
        this.number = number;
        this.key = key;
        this.closed = false;

        try (FileHandle handle = openHandle(path)) {
            GpioLineInfo line = new GpioLineInfo();
            line.setOffset(number);
            Ioctl ioctl = Ioctl.getInstance();
            ioctl.ioctl(handle, Ioctl.getGpioGetLineInfoIoctl(), line);

            this.flags = line.getFlags();
            this.name = line.getName();
            this.consumer = line.getConsumer();

            this.locked = (flags & GpioLineInfo.Flags.GPIOLINE_FLAG_KERNEL.value) > 0;
        }

    }

    @Override
    public int getPinNumber() {
        return number;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getConsumer() {
        return consumer;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    @Override
    public State read() throws IOException {
        checkClosed();
        checkLocked();
        checkDirection();

        /*
         * if(this.handle < 0) {
         * return (flags & GpioLineInfo.Flags.GPIOLINE_FLAG_ACTIVE_LOW.value) != 0
         * ? State.LOW : State.HIGH;
         * }
         */

        if (Direction.OUTPUT.equals(this.direction)) {
            throw new IOException("Can't read from output pin " + path + "-" + number);
        }

        GpioHandleData data = new GpioHandleData(new byte[1]);
        Ioctl ioctl = Ioctl.getInstance();
        ioctl.ioctl(handle, ioctl.getGpioHandleGetLineValuesIoctl(), data);

        byte value = data.getValues()[0];
        return value == 0 ? State.LOW : State.HIGH;
    }

    @Override
    public void write(State state) throws IOException {
        checkClosed();
        checkLocked();
        checkDirection();
        if (Direction.INPUT.equals(this.direction)) {
            throw new IOException("Can't write to input pin " + path + "-" + number);
        }

        byte value = (byte) (state == State.HIGH ? 1 : 0);
        GpioHandleData data = new GpioHandleData(new byte[] { value });

        Ioctl ioctl = Ioctl.getInstance();
        ioctl.ioctl(handle, ioctl.getGpioHandleSetLineValuesIoctl(), data);
    }

    public void setDirection(Direction direction) throws IOException {
        checkClosed();
        checkLocked();
        if (this.handle > 0 && this.direction.equals(direction)) {
            return;
        }
        try (FileHandle fd = openHandle(this.path)) {
            freeHandle();

            int mode = Direction.INPUT.equals(direction) ? GpioHandleRequest.Flags.GPIOHANDLE_REQUEST_INPUT.value
                    : GpioHandleRequest.Flags.GPIOHANDLE_REQUEST_OUTPUT.value;

            GpioHandleRequest request = new GpioHandleRequest();
            request.setLinesOffset(new int[] { number });
            request.setFlags(mode);
            request.setConsumerLabel("jef-gpio-manager");
            request.setLines(1);
            Ioctl ioctl = Ioctl.getInstance();

            ioctl.ioctl(fd, ioctl.getGpioGetLineHandleIoctl(), request);
            this.handle = request.getFd();
            this.direction = direction;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void close() throws Exception {
        freeHandle();
        GpioManager.closePin(key);
        closed = true;
    }

    private void checkDirection() throws IOException {
        if (direction == null) {
            throw new IOException("Gpio Pin direction not established");
        }
    }

    private void checkClosed() throws IOException {
        if (closed) {
            throw new IOException("Gpio Pin '" + key + "' is closed");
        }
    }

    private void checkLocked() throws IOException {
        if (locked) {
            throw new IOException("Gpio Pin '" + key + "' locked by kernel");
        }
    }

    private FileHandle openHandle(String path) throws NativeIOException {
        int open = Fcntl.getInstance().open(path, EnumSet.of(O_RDONLY, O_CLOEXEC));
        if (open < 0) {
            throw new NativeIOException("Unable to open gpio path: " + path);
        }
        return FileHandle.create(open);
    }

    private void freeHandle() {
        if (handle > 0) {
            Fcntl.getInstance().close(handle);
        }
    }
}
