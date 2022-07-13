package io.quarkiverse.jef.java.embedded.framework.linux.core.natives.spi;

import java.util.Arrays;
import java.util.List;

import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.function.CLibrary;
import org.graalvm.nativeimage.c.struct.CField;
import org.graalvm.nativeimage.c.struct.CStruct;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.word.PointerBase;

import io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils;

@SuppressWarnings("unused")
@CContext(SpiIocTransferNative.Headers.class)
@CLibrary("c")
public class SpiIocTransferNative {
    public static class Headers implements CContext.Directives {
        @Override
        public List<String> getHeaderFiles() {
            //System.out.println("SpiIocTransfer.Headers.getHeaderFiles");
            return Arrays.asList(
                    LinuxUtils.resolveHeader("spidev_impl.h"),
                    "\"/usr/include/linux/spi/spidev.h\"");
        }
    }

    /*
     * struct spi_ioc_transfer {
     * __u64 tx_buf;
     * __u64 rx_buf;
     *
     * __u32 len;
     * __u32 speed_hz;
     *
     * __u16 delay_usecs;
     * __u8 bits_per_word;
     * __u8 cs_change;
     * __u32 pad;
     *
     * If the contents of 'struct spi_ioc_transfer' ever change
     * incompatibly, then the ioctl number (currently 0) must change;
     * ioctls with constant size fields get a bit more in the way of
     * error checking than ones (like this) where that field varies.
     *
     * NOTE: struct layout is the same in 64bit and 32bit userspace.
     *
     * };
     */

    @CStruct("spi_ioc_transfer_impl")
    public interface spi_ioc_transfer extends PointerBase {
        //@CFieldAddress("tx_buf")
        //Pointer getTxBufAddress();

        @CField("tx_buf")
        CCharPointer getTxBuffer();

        @CField("tx_buf")
        void setTxBuffer(CCharPointer ptr);

        //@CFieldAddress("rx_buf")
        //Pointer getRxBufAddress();

        @CField("rx_buf")
        CCharPointer getRxBuffer();

        @CField("rx_buf")
        void setRxBuffer(CCharPointer ptr);

        @CField("len")
        int getLength();

        @CField("len")
        void setLength(int i);

        @CField("speed_hz")
        int getSpeed();

        @CField("speed_hz")
        void setSpeed(int i);

        @CField("delay_usecs")
        short getDelay();

        @CField("delay_usecs")
        void setDelay(short s);

        @CField("bits_per_word")
        byte getBitsPerWord();

        @CField("bits_per_word")
        void setBitsPerWord(byte b);

        @CField("cs_change")
        byte getCsChange();

        @CField("cs_change")
        void setCsChange(byte b);

        @CField("tx_nbits")
        byte getTxNbits();

        @CField("tx_nbits")
        void setTxNbits(byte b);

        @CField("rx_nbits")
        byte getRxNbits();

        @CField("rx_nbits")
        void setRxNbits(byte b);

        @CField("pad")
        byte getPad();

        @CField("pad")
        void setPad(byte i);
    }

}
