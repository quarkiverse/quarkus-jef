
package io.quarkiverse.jef.java.embedded.framework.linux.core.jna;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils.checkIOResult;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Sys;

@SuppressWarnings({ "UnusedDeclaration" })
public class SysJna extends Sys {
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
        int flag = flagToInt(flags);
        int result = Delegate.access(filename, flag);
        return result == 0;
    }

    @Override
    public String getcwd() throws NativeIOException {
        byte[] b = new byte[Sys.PATH_MAX];
        int result = Delegate.readlink("/proc/self/exe", b, b.length);
        checkIOResult("getcwd", result);
        return new String(b, 0, result);
    }

    @Override
    public int execl(String command, String... params) {
        return Delegate.execl(command, new StringArray(params));
    }

    @Override
    public int system(String command) {
        return Delegate.system(command);
    }

    @Override
    protected passwd getpwuid(long uid) {
        Pointer ptr = Delegate.getpwuid((int) uid);
        PasswdJna obj = new PasswdJna(ptr);
        return obj.generalize();
    }

    @Override
    protected List<Integer> getgroups() {
        int[] groups = new int[50];
        List<Integer> result = new ArrayList<>();
        int amount = Delegate.getgroups(50, groups);
        //System.out.println("amount = " + amount);
        for (int i = 0; i < amount; i++) {
            //System.out.println("group[" + i + "]=" + groups[i]);
            result.add(groups[i]);
        }
        return result;
    }

    @Override
    protected Group getgrgid(int gid) {
        Pointer ptr = Delegate.getgrgid(gid);
        group g = new group(ptr);
        return g.generalize();
    }

    @Override
    public int uname(UtcName u) {
        new_utsname param = new new_utsname();
        int result = Delegate.uname(param);
        if (result > -1) {
            u.fill(new String(param.sysname),
                    new String(param.nodename),
                    new String(param.release).trim(),
                    new String(param.version),
                    new String(param.machine),
                    new String(param.domainname));
        }
        return result;
    }

    @Override
    public boolean isNativeSupported() {
        return false;
    }

    @Structure.FieldOrder({ "sysname", "nodename", "release", "version", "machine", "domainname" })
    public static class new_utsname extends Structure {
        private static final int __NEW_UTS_LEN = 64;
        public byte[] sysname = new byte[__NEW_UTS_LEN + 1];
        public byte[] nodename = new byte[__NEW_UTS_LEN + 1];
        public byte[] release = new byte[__NEW_UTS_LEN + 1];
        public byte[] version = new byte[__NEW_UTS_LEN + 1];
        public byte[] machine = new byte[__NEW_UTS_LEN + 1];
        public byte[] domainname = new byte[__NEW_UTS_LEN + 1];
    }

    @Structure.FieldOrder({ "gr_name", "gr_passwd", "gr_gid", "gr_mem" })
    public static class group extends Structure {
        public String gr_name; /* Group name. */
        public String gr_passwd; /* Password. */
        public int gr_gid; /* Group ID. */
        public PointerByReference gr_mem; /* Member list. */

        public group() {
        }

        public group(Pointer p) {
            super(p);
            read();
        }

        public Group generalize() {
            return new Group(gr_name, gr_passwd, gr_gid);
        }
    }

    @Structure.FieldOrder({ "pw_name", "pw_passwd", "pw_uid", "pw_gid", "pw_gecos", "pw_dir", "pw_shell" })
    public static class PasswdJna extends Structure {
        public String pw_name;
        public String pw_passwd;
        public int pw_uid;
        public int pw_gid;
        public String pw_gecos;
        public String pw_dir;
        public String pw_shell;

        public PasswdJna() {
        }

        public PasswdJna(Pointer p) {
            super(p);
            read();
        }

        public passwd generalize() {
            return new passwd(pw_name, pw_passwd, pw_uid, pw_gid, pw_gecos, pw_dir, pw_shell);
        }
    }

    static class Delegate {
        public static native int getuid();

        public static native int geteuid();

        public static native int getpid();

        //public static native String getcwd(String p, int size);

        public static native int readlink(String path, byte[] s, int size);

        public static native int access(String path, int flags);

        public static native Pointer getpwuid(int uid);

        //public static native int getgrouplist(String user, int group, int[] groupIDs, IntByReference ngroups);

        public static native Pointer getgrgid(int gid);

        public static native String getlogin();

        public static native int getgroups(int size, int[] list);

        public static native int uname(new_utsname uname);

        public static native int execl(String command, StringArray params);

        public static native int system(String command);

        static {
            Native.register("c");
        }

    }
}
