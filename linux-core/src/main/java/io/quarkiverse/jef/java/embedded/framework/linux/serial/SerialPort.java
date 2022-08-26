
package io.quarkiverse.jef.java.embedded.framework.linux.serial;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags.*;
import static io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl.TIOCMGET;
import static io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl.TIOCMSET;
import static io.quarkiverse.jef.java.embedded.framework.linux.core.Termios.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Termios;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.IntReference;

@SuppressWarnings("unused")
public class SerialPort implements SerialBus {
    private final String path;
    private final SerialBaudRate rate;
    private boolean closed = false;
    private final Fcntl fcntl;
    private final Ioctl ioctl;
    private final Termios termios;
    private final FileHandle handle;
    private final SerialInputStream is;
    private final SerialOutputStream os;

    public SerialPort(String path, SerialBaudRate rate) throws NativeIOException {
        this.path = path;
        this.rate = rate;
        fcntl = Fcntl.getInstance();
        ioctl = Ioctl.getInstance();
        termios = Termios.getInstance();
        int open = fcntl.open(path, EnumSet.of(O_RDWR, O_NOCTTY, O_SYNC/* O_NONBLOCK */));
        if (open < 0) {
            throw new NativeIOException("Unable to open serial port: " + path);
        }
        handle = FileHandle.create(open);

        int result = fcntl.fcntl(handle, Fcntl.F_SETFL, EnumSet.of(O_RDWR));
        if (result < 0) {
            throw new NativeIOException("Unable to set fcntl to serial port: " + path);
        }
        setup();
        //setup1();
        is = new SerialInputStream();
        os = new SerialOutputStream();
    }

    private void setup() throws NativeIOException {
        TermiosStructure tty = new TermiosStructure();

        int result = termios.tcgetattr(handle, tty);
        if (result < 0) {
            throw new NativeIOException("Unable to get termios structure for serial port: " + path());
        }
        termios.cfmakeraw(tty);

        result = termios.cfsetispeed(tty, rate.value);
        if (result < 0) {
            throw new NativeIOException("Unable to set input speed to serial port: " + path());
        }

        result = termios.cfsetospeed(tty, rate.value);
        if (result < 0) {
            throw new NativeIOException("Unable to set out speed to serial port: " + path());
        }

        tty.c_cflag |= (CLOCAL | CREAD);
        tty.c_cflag &= ~PARENB;
        tty.c_cflag &= ~CSTOPB;
        tty.c_cflag &= ~CSIZE;
        tty.c_cflag |= CS8;
        tty.c_cflag &= ~CRTSCTS; // new

        tty.c_iflag &= ~(IXON | IXOFF | IXANY);

        tty.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);
        tty.c_oflag &= ~OPOST;

        tty.c_cc[VMIN] = 0;
        tty.c_cc[VTIME] = 10;

        result = termios.tcsetattr(handle, TCSANOW, tty);
        if (result < 0) {
            throw new NativeIOException("Unable to set attributes to serial port: " + path());
        }

        result = termios.tcflush(handle, TCIFLUSH);
        if (result < 0) {
            throw new NativeIOException("Unable to flush serial port: " + path());
        }
        IntReference statusRef = new IntReference();

        ioctl.ioctl(handle, TIOCMGET, statusRef);

        int status = statusRef.getValue();
        status |= TIOCM_DTR;
        status |= TIOCM_RTS;
        statusRef.setValue(status);

        ioctl.ioctl(handle, TIOCMSET, statusRef);

        //usleep (10000) ;	// 10mS

    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public SerialBaudRate serialBaudRate() {
        return rate;
    }

    @Override
    public void close() throws IOException {
        checkClosed();
        closed = true;
        is.close();
        os.close();
        handle.close();
    }

    private void checkClosed() throws IOException {
        if (closed)
            throw new IOException("Input Stream is closed");
    }

    @Override
    public InputStream getInputStream() {
        return is;
    }

    @Override
    public FileHandle getHandle() {
        return handle;
    }

    @Override
    public OutputStream getOutputStream() {
        return os;
    }

    private final class SerialOutputStream extends OutputStream {
        private boolean closed = false;

        @Override
        public void write(int b) throws IOException {
            checkClosed();
            int write = fcntl.write(handle, new byte[] { (byte) b }, 1);
            if (write < 0) {
                throw new IOException("Unable to write to serial port: " + path());
            }
        }

        @Override
        public void flush() throws IOException {
            checkClosed();
            termios.tcflush(handle, Termios.TCOFLUSH);
        }

        @Override
        public void close() throws IOException {
            checkClosed();
            closed = true;
            handle.close();
        }

        private void checkClosed() throws IOException {
            if (closed)
                throw new IOException("Input Stream is closed");
        }
    }

    private final class SerialInputStream extends InputStream {
        private boolean closed = false;

        @Override
        public long skip(long n) throws IOException {
            checkClosed();
            return fcntl.lseek(handle, n, Fcntl.Whence.SEEK_CUR);
        }

        @Override
        public int read() throws IOException {
            checkClosed();
            byte[] b = new byte[1];
            if (read(b) != 1) {
                return -1;
            }
            return b[0] & 0xFF;
        }

        @Override
        public int read(byte[] b) throws IOException {
            checkClosed();
            return fcntl.read(handle, b, b.length);
        }

        @Override
        public int available() throws IOException {
            checkClosed();
            IntReference ref = new IntReference();
            if (ioctl.ioctl(handle, Ioctl.FIONREAD, ref) == -1) {
                return -1;
            }
            return ref.getValue();
        }

        private void checkClosed() throws IOException {
            if (closed)
                throw new IOException("Input Stream is closed");
        }

        @Override
        public void close() throws IOException {
            checkClosed();
            closed = true;
            handle.close();
        }
    }
}
