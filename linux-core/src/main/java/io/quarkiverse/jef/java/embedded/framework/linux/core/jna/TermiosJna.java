
package io.quarkiverse.jef.java.embedded.framework.linux.core.jna;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Termios;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.serial.TermiosStructure;

@SuppressWarnings("unused")
public class TermiosJna extends Termios {
    private final static int NCCS = 32;

    @Override
    public boolean isNativeSupported() {
        return false;
    }

    @Override
    public int tcgetattr(FileHandle handle, TermiosStructure structure) {
        termios t = new termios();
        int result = Delegate.tcgetattr(handle.getHandle(), t);
        if (result > -1) {
            t.generalize(structure);
        }
        return result;
    }

    @Override
    public void cfmakeraw(TermiosStructure options) {
        termios t = new termios(options);
        Delegate.cfmakeraw(t);
        t.generalize(options);
    }

    @Override
    public int cfsetispeed(TermiosStructure options, int value) {
        termios t = new termios(options);
        int result = Delegate.cfsetispeed(t, value);
        if (result > -1) {
            t.generalize(options);
        }
        return result;
    }

    @Override
    public int cfsetospeed(TermiosStructure options, int value) {
        termios t = new termios(options);
        int result = Delegate.cfsetospeed(t, value);
        if (result > -1) {
            t.generalize(options);
        }
        return result;
    }

    @Override
    public int tcsetattr(FileHandle handle, int tcsanow, TermiosStructure options) {
        termios t = new termios(options);
        int result = Delegate.tcsetattr(handle.getHandle(), tcsanow, t);
        if (result > -1) {
            t.generalize(options);
        }
        return result;
    }

    @Override
    public int cfgetispeed(TermiosStructure options) {
        termios t = new termios(options);
        int result = Delegate.cfgetispeed(t);
        t.generalize(options);
        return result;
    }

    @Override
    public int cfgetospeed(TermiosStructure options) {
        termios t = new termios(options);
        int result = Delegate.cfgetospeed(t);
        t.generalize(options);
        return result;
    }

    @Override
    public int tcflush(FileHandle handle, int queue_selector) {
        return Delegate.tcflush(handle.getHandle(), queue_selector);
    }

    @Structure.FieldOrder({ "c_iflag", "c_oflag", "c_cflag", "c_lflag", "c_line", "c_cc", "c_ispeed", "c_ospeed" })
    public static class termios extends Structure {
        public int c_iflag;
        public int c_oflag;
        public int c_cflag;
        public int c_lflag;
        public byte c_line;
        public byte[] c_cc = new byte[NCCS];
        public int c_ispeed;
        public int c_ospeed;

        public termios() {
        }

        public termios(Pointer p) {
            super(p);
            read();
        }

        public termios(TermiosStructure options) {
            c_iflag = options.getC_iflag();
            c_oflag = options.getC_oflag();
            c_cflag = options.getC_cflag();
            c_lflag = options.getC_lflag();
            c_line = options.getC_line();
            c_cc = options.getC_cc();
            c_ispeed = options.getC_ispeed();
            c_ospeed = options.getC_ospeed();
        }

        public TermiosStructure generalize(TermiosStructure ts) {
            ts.setC_iflag(c_iflag);
            ts.setC_oflag(c_oflag);
            ts.setC_cflag(c_cflag);
            ts.setC_lflag(c_lflag);
            ts.setC_line(c_line);
            ts.setC_cc(c_cc);
            ts.setC_ispeed(c_ispeed);
            ts.setC_ospeed(c_ospeed);
            return ts;
        }

        public TermiosStructure generalize() {
            return generalize(new TermiosStructure());
        }
    }

    static class Delegate {
        static {
            Native.register("c");
        }

        public static native int tcgetattr(int fd, termios t);

        public static native void cfmakeraw(termios termios);

        public static native int cfsetispeed(termios t, int value);

        public static native int cfsetospeed(termios t, int value);

        public static native int tcsetattr(int handle, int tcsanow, termios t);

        public static native int cfgetispeed(termios t);

        public static native int cfgetospeed(termios t);

        public static native int tcflush(int handle, int queue_selector);
    }

}
