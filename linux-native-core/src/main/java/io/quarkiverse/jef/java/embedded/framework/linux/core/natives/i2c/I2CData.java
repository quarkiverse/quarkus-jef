package io.quarkiverse.jef.java.embedded.framework.linux.core.natives.i2c;

import java.util.Collections;
import java.util.List;

import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.function.CLibrary;
import org.graalvm.nativeimage.c.struct.CField;
import org.graalvm.nativeimage.c.struct.CFieldAddress;
import org.graalvm.nativeimage.c.struct.CPointerTo;
import org.graalvm.nativeimage.c.struct.CStruct;
import org.graalvm.word.Pointer;
import org.graalvm.word.PointerBase;

import io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils;

// https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.tutorial/src/com/oracle/svm/tutorial/CInterfaceTutorial.java
// https://cornerwings.github.io/2018/07/graal-native-methods/
// https://github.com/fivdi/i2c-bus/blob/master/src/i2c-dev.h
@SuppressWarnings("unused")
@CContext(I2CData.Headers.class)
@CLibrary("c")
public class I2CData {
    public static class Headers implements CContext.Directives {
        @Override
        public List<String> getHeaderFiles() {
            return Collections.singletonList(
                    LinuxUtils.resolveHeader("i2c-dev.h"));
        }
    }

    @CPointerTo(I2CSmbusData.class)
    public interface I2CSmbusDataPtr extends PointerBase {
        I2CSmbusData read();

        void write(I2CSmbusData data);
    }

    @CStruct("union i2c_smbus_data")
    public interface I2CSmbusData extends PointerBase {
        @CField("byte")
        byte getByte();

        @CField("byte")
        void setByte(byte b);

        @CField("word")
        short getWord();

        @CField("word")
        void setWord(short word);

        @CFieldAddress("block")
        Pointer getBlock();
    }

    @CStruct("i2c_smbus_ioctl_data")
    public interface I2CSmbusIoctlData extends PointerBase {
        @CField("read_write")
        byte getReadWrite();

        @CField("read_write")
        void setReadWrite(byte rw);

        @CField("command")
        byte getCommand();

        @CField("command")
        void setCommand(byte command);

        @CField("size")
        int getSize();

        @CField("size")
        void setSize(int size);

        @CFieldAddress("data")
        I2CSmbusDataPtr getData();
    }
}
