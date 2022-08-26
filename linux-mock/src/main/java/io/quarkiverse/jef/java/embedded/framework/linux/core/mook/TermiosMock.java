package io.quarkiverse.jef.java.embedded.framework.linux.core.mook;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Termios;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.serial.TermiosStructure;

public class TermiosMock extends Termios {
    @Override
    public boolean isNativeSupported() {
        return true;
    }

    @Override
    public boolean isMock() {
        return true;
    }

    @Override
    public int tcgetattr(FileHandle handle, TermiosStructure ts) {
        ts.setC_cc(new byte[16]);
        return 0;
    }

    @Override
    public void cfmakeraw(TermiosStructure options) {

    }

    @Override
    public int cfsetispeed(TermiosStructure options, int value) {
        return 0;
    }

    @Override
    public int cfsetospeed(TermiosStructure options, int value) {
        return 0;
    }

    @Override
    public int tcsetattr(FileHandle handle, int tcsanow, TermiosStructure options) {
        return 0;
    }

    @Override
    public int cfgetispeed(TermiosStructure options) {
        return 0;
    }

    @Override
    public int cfgetospeed(TermiosStructure options) {
        return 0;
    }

    @Override
    public int tcflush(FileHandle handle, int queue_selector) {
        return 0;
    }
}
