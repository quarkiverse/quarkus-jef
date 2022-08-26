package io.quarkiverse.jef.java.embedded.framework.runtime.dev.tracing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public abstract class AbstractTracingAgent implements KernelTracingAgent {
    private final static Logger logger = LogManager.getLogger(AbstractTracingAgent.class);

    @Override
    public final boolean enabled() {
        String path = "/sys/kernel/debug/tracing/events/" + categories() + "/enable";
        Fcntl fcntl = Fcntl.getInstance();
        try (FileHandle open = fcntl.open(path, IOFlags.O_RDONLY)) {
            byte[] buf = new byte[1];
            fcntl.read(open, buf, 1);
            return buf[0] == 1;
        } catch (Exception e) {
            logger.error("Unable to read to file", e);
            return false;
        }
    }

    @Override
    public final boolean enable() {
        if (enabled()) {
            return true;
        }
        // https://linux.die.net/man/3/fputs
        Fcntl fcntl = Fcntl.getInstance();
        //String command = "echo '" + category() + ":*' > /sys/kernel/debug/tracing/set_event";
        try (FileHandle open = fcntl.open("/sys/kernel/debug/tracing/set_event", IOFlags.O_RDWR)) {
            byte[] b = ("'" + categories() + "':*").getBytes();
            fcntl.write(open, b, b.length);
            return true;
        } catch (Exception e) {
            logger.error("Unable to write to file", e);
            return false;
        }
    }
}
