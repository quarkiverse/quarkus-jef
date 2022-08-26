package io.quarkiverse.jef.java.embedded.framework.linux.core.mook;

import java.util.*;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Sys;

public class SysMock extends Sys {
    private final static Map<Integer, Group> GROUP_MAP = new HashMap<>() {
        {
            put(0, new Group("i2c", "", 0));
            put(1, new Group("spi", "", 1));
            put(2, new Group("gpio", "", 2));
        }
    };

    @Override
    public boolean isMock() {
        return true;
    }

    private final static List<Integer> IDS = new ArrayList<>(GROUP_MAP.keySet());

    @Override
    public boolean isNativeSupported() {
        return true;
    }

    @Override
    public long getuid() {
        return 0;
    }

    @Override
    public long geteuid() {
        return 0;
    }

    @Override
    public int getpid() {
        return 0;
    }

    @Override
    public boolean access(String filename, EnumSet<AccessFlag> flags) {
        return true;
    }

    @Override
    public String getcwd() throws NativeIOException {
        return "";
    }

    @Override
    public int execl(String command, String... params) {
        return 0;
    }

    @Override
    public int system(String command) {
        return 0;
    }

    @Override
    protected passwd getpwuid(long uid) {
        return new passwd("test", "test", 0, 0, "test", "test", "test");
    }

    @Override
    protected List<Integer> getgroups() {
        return IDS;
    }

    @Override
    protected Group getgrgid(int gid) {
        return GROUP_MAP.get(gid);
    }

    @Override
    public int uname(UtcName u) {
        u.fill("test", "test", "test", "test", "test", "test");
        return 0;
    }
}
