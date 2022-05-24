package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import java.util.Collections;
import java.util.List;

import org.graalvm.nativeimage.StackValue;
import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.constant.CConstant;
import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.function.CLibrary;
import org.graalvm.nativeimage.c.struct.CField;
import org.graalvm.nativeimage.c.struct.CFieldAddress;
import org.graalvm.nativeimage.c.struct.CStruct;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.word.PointerBase;

import io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Termios;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.serial.TermiosStructure;

@CContext(TermiosNative.TermiosNativeHeaders.class)
@CLibrary("c")
public class TermiosNative extends Termios {
    @CConstant
    private static native int NCCS();

    private static void fillStruct(termios2 t, TermiosStructure o) {
        t.setC_iflag(o.getC_iflag());
        t.setC_oflag(o.getC_oflag());
        t.setC_cflag(o.getC_cflag());
        t.setC_lflag(o.getC_lflag());
        t.setC_line(o.getC_line());
        t.setC_ispeed(o.getC_ispeed());
        t.setC_ospeed(o.getC_ospeed());

        byte[] c_cc = o.getC_cc();
        for (int i = 0; i < NCCS(); i++) {
            t.getC_cc().write(i, c_cc[i]);
        }
    }

    private static void updateStruct(TermiosStructure o, termios2 t) {
        o.setC_iflag(t.getC_iflag());
        o.setC_oflag(t.getC_oflag());
        o.setC_cflag(t.getC_cflag());
        o.setC_lflag(t.getC_lflag());
        o.setC_line(t.getC_line());
        o.setC_ispeed(t.getC_ispeed());
        o.setC_ospeed(t.getC_ospeed());
        byte[] c_cc = o.getC_cc();
        for (int i = 0; i < NCCS(); i++) {
            c_cc[i] = t.getC_cc().read(i);
        }
    }

    @Override
    public boolean isNativeSupported() {
        return true;
    }

    @Override
    public TermiosStructure tcgetattr(FileHandle handle) {
        TermiosStructure options = new TermiosStructure();
        options.setC_cc(new byte[NCCS()]);
        termios2 t = StackValue.get(termios2.class);//UnmanagedMemory.malloc(SizeOf.get(termios2.class));
        Delegate.tcgetattr(handle.getHandle(), t);
        updateStruct(options, t);
        return options;
    }

    @Override
    public void cfmakeraw(TermiosStructure options) {
        termios2 t = StackValue.get(termios2.class);//UnmanagedMemory.malloc(SizeOf.get(termios2.class));
        fillStruct(t, options);
        Delegate.cfmakeraw(t);
        updateStruct(options, t);
    }

    @Override
    public int cfsetispeed(TermiosStructure options, int value) {
        termios2 t = StackValue.get(termios2.class);//UnmanagedMemory.malloc(SizeOf.get(termios2.class));
        fillStruct(t, options);
        int result = Delegate.cfsetispeed(t, value);
        updateStruct(options, t);
        return result;

    }

    @Override
    public int cfsetospeed(TermiosStructure options, int value) {
        termios2 t = StackValue.get(termios2.class);//UnmanagedMemory.malloc(SizeOf.get(termios2.class));
        fillStruct(t, options);
        int result = Delegate.cfsetospeed(t, value);
        updateStruct(options, t);
        return result;
    }

    @Override
    public void tcsetattr(FileHandle handle, int tcsanow, TermiosStructure options) {
        termios2 t = StackValue.get(termios2.class);//UnmanagedMemory.malloc(SizeOf.get(termios2.class));
        fillStruct(t, options);
        Delegate.tcsetattr(handle.getHandle(), tcsanow, t);
        updateStruct(options, t);
    }

    @Override
    public int cfgetispeed(TermiosStructure options) {
        termios2 t = StackValue.get(termios2.class);//UnmanagedMemory.malloc(SizeOf.get(termios2.class));
        fillStruct(t, options);
        int result = Delegate.cfgetispeed(t);
        updateStruct(options, t);
        return result;
    }

    @Override
    public int cfgetospeed(TermiosStructure options) {
        termios2 t = StackValue.get(termios2.class);//UnmanagedMemory.malloc(SizeOf.get(termios2.class));
        fillStruct(t, options);
        int result = Delegate.cfgetospeed(t);
        updateStruct(options, t);
        return result;
    }

    @Override
    public int tcflush(FileHandle handle, int queue_selector) {
        return Delegate.tcflush(handle.getHandle(), queue_selector);
    }

    @CStruct("termios2")
    private interface termios2 extends PointerBase {
        @CField("c_iflag")
        int getC_iflag();

        @CField("c_iflag")
        void setC_iflag(int value);

        @CField("c_oflag")
        int getC_oflag();

        @CField("c_oflag")
        void setC_oflag(int value);

        @CField("c_cflag")
        int getC_cflag();

        @CField("c_cflag")
        void setC_cflag(int value);

        @CField("c_lflag")
        int getC_lflag();

        @CField("c_lflag")
        void setC_lflag(int value);

        @CField("c_line")
        byte getC_line();

        @CField("c_line")
        void setC_line(byte value);

        @CFieldAddress("c_cc")
        CCharPointer getC_cc();

        @CField("c_ispeed")
        int getC_ispeed();

        @CField("c_ispeed")
        void setC_ispeed(int value);

        @CField("c_ospeed")
        int getC_ospeed();

        @CField("c_ospeed")
        void setC_ospeed(int value);
    }

    private static class Delegate {
        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int tcflush(int handle, int queue_selector);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int cfgetospeed(termios2 t);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int cfgetispeed(termios2 t);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native void tcsetattr(int handle, int tcsanow, termios2 t);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int tcgetattr(int handle, termios2 t);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native void cfmakeraw(termios2 t);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int cfsetispeed(termios2 t, int value);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int cfsetospeed(termios2 t, int value);
    }

    public static class TermiosNativeHeaders implements CContext.Directives {
        public List<String> getHeaderFiles() {
            return Collections.singletonList(
                    LinuxUtils.resolveHeader("termios.h"));
        }
    }
}
