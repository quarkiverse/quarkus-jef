
package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import java.util.Collections;
import java.util.List;

import org.graalvm.nativeimage.UnmanagedMemory;
import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.function.CLibrary;
import org.graalvm.nativeimage.c.struct.CField;
import org.graalvm.nativeimage.c.struct.CFieldAddress;
import org.graalvm.nativeimage.c.struct.CStruct;
import org.graalvm.nativeimage.c.type.CIntPointer;
import org.graalvm.word.PointerBase;
import org.graalvm.word.WordFactory;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Cap;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Capability;
import io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils;

// sudo apt-get install libcap-dev
@CContext(CapNative.CapNativeHeaders.class)
@CLibrary("cap")
public class CapNative extends Cap {
    @Override
    public boolean isNativeSupported() {
        return true;
    }

    @Override
    public int cap_free(CapStruct cap_t) {
        return Delegate.cap_free(cap_t.getPointer());
    }

    @Override
    public CapStruct cap_get_pid(int pid) {
        _cap_struct cap = Delegate.cap_get_pid(pid);
        return new CapStruct(
                new CapHeaderStruct(cap.getHead().getVersion(), cap.getHead().getPid()),
                new CapDataStruct(cap.getSet().getEffective(), cap.getSet().getPermitted(), cap.getSet().getInheritable()),
                cap.rawValue());
    }

    @Override
    public boolean cap_get_flag(CapStruct cap_t, Capability capability, CapabilityFlag flag) {
        CIntPointer ptr = UnmanagedMemory.malloc(4);
        try {
            _cap_struct cap = WordFactory.pointer(cap_t.getPointer());
            Delegate.cap_get_flag(cap, capability.getValue(), flag.getValue(), ptr);
            return ptr.read() == 1;
        } finally {
            UnmanagedMemory.free(ptr);
        }
    }

    @CStruct("__user_cap_header_struct")
    public interface __user_cap_header_struct extends PointerBase {
        @CField("version")
        int getVersion();

        @CField("pid")
        int getPid();
    }

    @CStruct("__user_cap_data_struct")
    public interface __user_cap_data_struct extends PointerBase {
        @CField("effective")
        int getEffective();

        @CField("permitted")
        int getPermitted();

        @CField("inheritable")
        int getInheritable();
    }

    @CStruct("_cap_struct")
    public interface _cap_struct extends PointerBase {
        @CFieldAddress("head")
        __user_cap_header_struct getHead();

        @CFieldAddress("set")
        __user_cap_data_struct getSet();
    }

    public static class CapNativeHeaders implements CContext.Directives {
        public List<String> getHeaderFiles() {
            return Collections.singletonList(
                    LinuxUtils.resolveHeader("libcap.h")
            //"\"/usr/include/linux/capability.h\""//,
            );
        }
    }

    private static class Delegate {
        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native _cap_struct cap_get_pid(int pid);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int cap_free(long cap_t);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int cap_get_flag(PointerBase cap_t, int cap_value_t, int cap_flag_t, PointerBase result);
    }
}
