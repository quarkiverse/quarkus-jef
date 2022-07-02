
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
        handle = fcntl.open(path, EnumSet.of(O_RDWR, O_NOCTTY, O_SYNC/*O_NONBLOCK*/));
        fcntl.fcntl(handle, Fcntl.F_SETFL, EnumSet.of(O_RDWR));
        setup();
        //setup1();
        is = new SerialInputStream();
        os = new SerialOutputStream();
    }

    private void setup() throws NativeIOException {
        TermiosStructure tty = termios.tcgetattr(handle);
        termios.cfmakeraw(tty);
        termios.cfsetispeed(tty, rate.value);
        termios.cfsetospeed(tty, rate.value);

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

        termios.tcsetattr(handle, TCSANOW, tty);
        termios.tcflush(handle, TCIFLUSH);
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
            fcntl.write(handle, new byte[] { (byte) b }, 1);
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
