
package io.quarkiverse.jef.java.embedded.framework.linux.gpio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.core.*;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

@SuppressWarnings("unused")
public class GpioManager {
    private final static Map<String, GpioPin> pinsets = new HashMap<>();

    public static GpioPin getPin(String path, int number) throws IOException {
        synchronized (GpioManager.class) {
            GpioPin pin;
            String key = getKey(path, number);
            if ((pin = getCachedKey(key)) != null) {
                return pin;
            }
            pin = new GpioPinImpl(key, path, number);
            pinsets.put(key, pin);
            return pin;
        }
        //return getPin(path, number, null);
    }

    public static GpioChipInfo getChipInfo(String path) throws NativeIOException {
        Fcntl fcntl = Fcntl.getInstance();

        int fd = fcntl.open(path, IOFlags.O_RDONLY);

        if (fd < 0) {
            throw new NativeIOException("Unable to open gpio file: " + path);
        }
        try (FileHandle handle = FileHandle.create(fd)) {
            Ioctl ioctl = Ioctl.getInstance();
            GpioChipInfo result = new GpioChipInfo();
            try {
                ioctl.ioctl(handle, IoctlBase.getGpioGetChipInfoIoctl(), result);
                return result;
            } catch (NativeIOException e) {
                throw new NativeIOException("Unable to access to gpio '" + path + "'");
            }
        }
    }

    public static boolean isUsed(String path, int number) {
        synchronized (GpioManager.class) {
            return getCachedKey(getKey(path, number)) != null;
        }
    }

    private static String getKey(String path, int number) {
        return path + "-" + number;
    }

    private static GpioPin getCachedKey(String cacheKey) {
        return pinsets.get(cacheKey);
    }

    @SuppressWarnings("EmptyTryBlock")
    static void closePin(String key) {
        synchronized (GpioManager.class) {
            try (GpioPin remove = pinsets.remove(key)) {

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
