
package io.quarkiverse.jef.java.embedded.framework.linux.core;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class LinuxUtils {
    private static Boolean isNative = null;
    private static Boolean isMock = null;
    private static final AtomicBoolean lock = new AtomicBoolean(false);

    public static boolean isNative() {
        init();
        return isNative;
    }

    public static boolean isMock() {
        init();
        return isMock;
    }

    private static void init() {
        if (!lock.get() && isNative == null) {
            synchronized (LinuxUtils.class) {
                if (!lock.get() && isNative == null) {
                    isNative = "executable".equals(System.getProperty("org.graalvm.nativeimage.kind"));
                    isMock = "test".equals(System.getProperty("java.embedded.framework.mode"));
                    lock.set(true);
                }
            }
        }
    }

    public static void checkIOResult(String method, int result) throws NativeIOException {
        if (result < 0) {
            String err = Errno.getInstance().strerror();
            //System.out.println("err = " + err);
            //String strerror = errno.get().strerror(result);
            ErrnoCode errnoCode = ErrnoCode.valueOf(-result).orElse(null);

            String strerror = errnoCode != null ? errnoCode.getMessage() : "Unknown error - " + result;
            //System.out.println("error out:" + result + " value:" + strerror);
            throw new NativeIOException(method + " failed(" + result + "): " + strerror, result);
        }
    }

    public static void dump(String name, ByteBuffer buf) {
        buf.position(0);
        System.out.println("****** dump '" + name + "' ******");
        byte[] b = new byte[buf.capacity()];
        buf.get(b);
        for (int i = 0; i < b.length; i++) {
            System.out.printf("buf[%d]=0x%2h\n", i, (b[i] & 0xFF));
        }
        buf.position(0);
        System.out.println("*************" + name + "********");
    }

    public static String resolveHeader(String fileName) {
        final ProtectionDomain pd = LinuxUtils.class.getProtectionDomain();
        final CodeSource cs = pd.getCodeSource();

        InputStream resource;

        resource = pd.getClassLoader().getResourceAsStream("headers/" + fileName);

        if (resource != null) {
            return copyResource(fileName, resource);
        }

        throw new RuntimeException("File " + fileName + " not found");
    }

    private static String copyResource(String fileName, InputStream resource) {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        Path path = Paths.get(tmpDir.getAbsolutePath() + File.separator + "jef");

        try {
            Files.createDirectories(path);

            int available = resource.available();
            byte[] buffer = new byte[available];
            int readed = resource.read(buffer);
            if (available != readed) {
                throw new RuntimeException("Expected " + available + " bytes but readed " + readed);
            }

            File targetFile = new File(path.toFile(), fileName);
            if (targetFile.exists()) {
                boolean delete = targetFile.delete();
                if (!delete) {
                    throw new RuntimeException("Unable to delete old file: " + targetFile.getAbsolutePath());
                }
            }
            boolean newFile = targetFile.createNewFile();
            if (!newFile) {
                throw new RuntimeException("Unable to create file: " + targetFile.getAbsolutePath());
            }

            try (OutputStream outStream = new FileOutputStream(targetFile)) {
                outStream.write(buffer);
            }
            String absolutePath = targetFile.getAbsolutePath();
            return "\"" + absolutePath + "\"";
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Stream<Path> find(String fileName, String absolutePath) throws IOException {
        return Files.find(
                Paths.get(absolutePath),
                Integer.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.endsWith(fileName));
    }

    public static byte[] toBytes(ByteBuffer buf) {
        if (buf.hasArray()) {
            return buf.array();
        }
        byte[] b = new byte[buf.capacity()];
        buf.get(b);
        return b;
    }
}
