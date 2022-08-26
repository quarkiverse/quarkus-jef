package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils.checkIOResult;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.graalvm.nativeimage.PinnedObject;
import org.graalvm.nativeimage.UnmanagedMemory;
import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.function.CLibrary;
import org.graalvm.nativeimage.c.struct.CField;
import org.graalvm.nativeimage.c.struct.CFieldAddress;
import org.graalvm.nativeimage.c.struct.CStruct;
import org.graalvm.nativeimage.c.struct.SizeOf;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CCharPointerPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.word.PointerBase;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Sys;

@CContext(SysNativeHeaders.class)
@CLibrary("c")
public class SysNative extends Sys {
    @Override
    public long getuid() {
        return Integer.toUnsignedLong(Delegate.getuid());
    }

    @Override
    public long geteuid() {
        return Integer.toUnsignedLong(Delegate.geteuid());
    }

    @Override
    public int getpid() {
        return Delegate.getpid();
    }

    @Override
    public boolean access(String filename, EnumSet<AccessFlag> flags) {
        try (CTypeConversion.CCharPointerHolder c = CTypeConversion.toCString(filename)) {
            int result = Delegate.access(
                    c.get(),
                    flagToInt(flags));
            return result == 0;
        }
    }

    @Override
    public String getcwd() throws NativeIOException {
        byte[] bytes = new byte[Sys.PATH_MAX];
        try (PinnedObject pin = PinnedObject.create(bytes)) {
            try (CTypeConversion.CCharPointerHolder url = CTypeConversion.toCString("/proc/self/exe")) {
                CCharPointer rawData = pin.addressOfArrayElement(0);
                int result = Delegate.readlink(url.get(), rawData, bytes.length);
                checkIOResult("getcwd", result);
                return new String(bytes, 0, result);
            }
        }
    }

    @Override
    protected passwd getpwuid(long uid) {
        __passwd read = Delegate.getpwuid((int) uid);
        return new passwd(
                CTypeConversion.toJavaString(read.getName()),
                CTypeConversion.toJavaString(read.getPassword()),
                read.getUID(),
                read.getGID(),
                CTypeConversion.toJavaString(read.getRealName()),
                CTypeConversion.toJavaString(read.getHomeDirectory()),
                CTypeConversion.toJavaString(read.getShell()));

    }

    @Override
    protected List<Integer> getgroups() {
        int[] groups = new int[50];
        List<Integer> result = new ArrayList<>();
        try (PinnedObject pin = PinnedObject.create(groups)) {
            int amount = Delegate.getgroups(50, pin.addressOfArrayElement(0));
            //System.out.println("amount = " + amount);
            for (int i = 0; i < amount; i++) {
                //System.out.println("group[" + i + "]=" + groups[i]);
                result.add(groups[i]);
            }
        }
        return result;
    }

    @Override
    protected Group getgrgid(int gid) {
        __group g = Delegate.getgrgid(gid);
        return new Group(
                CTypeConversion.toJavaString(g.getName()),
                CTypeConversion.toJavaString(g.getPassword()),
                g.getGroupId());
    }

    @Override
    public int execl(String command, String... params) {
        try (CTypeConversion.CCharPointerHolder cmd = CTypeConversion.toCString(command)) {
            try (CTypeConversion.CCharPointerPointerHolder pr = CTypeConversion.toCStrings(params)) {
                return Delegate.execl(cmd.get(), pr.get());
            }
        }
    }

    @Override
    public int system(String command) {
        try (CTypeConversion.CCharPointerHolder cmd = CTypeConversion.toCString(command)) {
            return Delegate.system(cmd.get());
        }
    }

    @Override
    public int uname(UtcName u) {
        __new_utsname struct = UnmanagedMemory.malloc(
                SizeOf.get(__new_utsname.class));
        try {
            int result = Delegate.uname(struct);
            if (result > -1) {
                u.fill(
                        CTypeConversion.toJavaString(struct.sysname()),
                        CTypeConversion.toJavaString(struct.nodename()),
                        CTypeConversion.toJavaString(struct.release()).trim(),
                        CTypeConversion.toJavaString(struct.version()),
                        CTypeConversion.toJavaString(struct.machine()),
                        CTypeConversion.toJavaString(struct.domainname()));
            }
            return result;
        } finally {
            UnmanagedMemory.free(struct);
        }
    }

    @Override
    public boolean isNativeSupported() {
        return true;
    }

    @CStruct("__new_utsname")
    private interface __new_utsname extends PointerBase {
        @CFieldAddress("sysname")
        CCharPointer sysname();

        @CFieldAddress("nodename")
        CCharPointer nodename();

        @CFieldAddress("release")
        CCharPointer release();

        @CFieldAddress("version")
        CCharPointer version();

        @CFieldAddress("machine")
        CCharPointer machine();

        @CFieldAddress("domainname")
        CCharPointer domainname();

    }

    @CStruct("__group")
    private interface __group extends PointerBase {
        @CField("gr_name")
        CCharPointer getName();

        @CField("gr_passwd")
        CCharPointer getPassword();

        @CField("gr_gid")
        int getGroupId();

        @CFieldAddress("gr_mem")
        CCharPointer getMemberList();
    }

    @CStruct("__passwd")
    private interface __passwd extends PointerBase {
        @CField("pw_name")
        CCharPointer getName();

        @CField("pw_passwd")
        CCharPointer getPassword();

        @CField("pw_uid")
        int getUID();

        @CField("pw_gid")
        int getGID();

        @CField("pw_gecos")
        CCharPointer getRealName();

        @CField("pw_dir")
        CCharPointer getHomeDirectory();

        @CField("pw_shell")
        CCharPointer getShell();
    }

    private static class Delegate {
        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int getuid();

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int geteuid();

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int getpid();

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int readlink(CCharPointer path, CCharPointer p, int size);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int access(CCharPointer path, int flags);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native CCharPointer getlogin();

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native __passwd getpwuid(int uid);

        //@CFunction(transition = CFunction.Transition.NO_TRANSITION)
        //public static native int getgrouplist(CCharPointer user, int group, PointerBase groups, CIntPointer ngroups);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native __group getgrgid(int gid);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int getgroups(int size, PointerBase list);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int execl(CCharPointer command, CCharPointerPointer arg);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int system(CCharPointer command);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int uname(PointerBase utsname);
    }
}
