package io.quarkiverse.jef.java.embedded.framework.devices.library.nxp;

public enum PCD_Version {
    Version_Counterfeit(0x12),
    Version_FM17522(0x88),
    Version_FM17522_1(0xb2),
    Version_FM17522E(0x89),
    Version_0_0(0x90),
    Version_1_0(0x91),
    Version_2_0(0x92),
    Version_Unknown(0xff);

    final byte value;

    PCD_Version(int value) {
        this.value = (byte) value;
    }
}
