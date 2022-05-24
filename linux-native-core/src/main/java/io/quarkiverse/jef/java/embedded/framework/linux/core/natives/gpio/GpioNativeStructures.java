package io.quarkiverse.jef.java.embedded.framework.linux.core.natives.gpio;

import java.util.List;

import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.function.CLibrary;
import org.graalvm.nativeimage.c.struct.CField;
import org.graalvm.nativeimage.c.struct.CFieldAddress;
import org.graalvm.nativeimage.c.struct.CStruct;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CIntPointer;
import org.graalvm.word.PointerBase;

import io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils;

@CContext(GpioNativeStructures.Headers.class)
@CLibrary("c")
public class GpioNativeStructures {
    public static class Headers implements CContext.Directives {
        @Override
        public List<String> getHeaderFiles() {
            //System.out.println("SpiIocTransfer.Headers.getHeaderFiles");
            return List.of(
                    LinuxUtils.resolveHeader("gpio.h")
            //,
            //"\"/usr/include/linux/gpio.h\""
            );
        }
    }

    /**
     * struct gpiochip_info {
     * char name[GPIO_MAX_NAME_SIZE];
     * char label[GPIO_MAX_NAME_SIZE];
     * __u32 lines;
     * };
     */
    @CStruct("gpiochip_info")
    public interface gpiochip_info extends PointerBase {
        @CFieldAddress("name")
        CCharPointer name();

        @CFieldAddress("label")
        CCharPointer label();

        @CField("lines")
        int lines();
    }

    /**
     * struct gpioline_info {
     * __u32 line_offset;
     * __u32 flags;
     * char name[GPIO_MAX_NAME_SIZE];
     * char consumer[GPIO_MAX_NAME_SIZE];
     * };
     */
    @CStruct("gpioline_info")
    public interface gpioline_info extends PointerBase {
        @CField("line_offset")
        int lineOffset();

        @CField("line_offset")
        void offset(int offset);

        @CField("flags")
        int flags();

        @CFieldAddress("name")
        CCharPointer name();

        @CFieldAddress("consumer")
        CCharPointer consumer();
    }

    /**
     * struct gpiohandle_request {
     * __u32 lineoffsets[GPIOHANDLES_MAX];
     * __u32 flags;
     * __u8 default_values[GPIOHANDLES_MAX];
     * char consumer_label[GPIO_MAX_NAME_SIZE];
     * __u32 lines;
     * int fd;
     * };
     */
    @CStruct("gpiohandle_request")
    public interface gpiohandle_request extends PointerBase {
        @CFieldAddress("lineoffsets")
        CIntPointer lineOffsets();

        @CField("flags")
        int flags();

        @CField("flags")
        void flags(int flags);

        @CFieldAddress("default_values")
        CCharPointer defaultValues();

        @CFieldAddress("consumer_label")
        CCharPointer consumerLabel();

        @CField("lines")
        int lines();

        @CField("lines")
        void lines(int lines);

        @CField("fd")
        int fd();
    }

    /**
     * struct gpiohandle_data {
     * __u8 values[GPIOHANDLES_MAX];
     * };
     */
    @CStruct("gpiohandle_data")
    public interface gpiohandle_data extends PointerBase {
        @CFieldAddress("values")
        CCharPointer values();
    }
}
