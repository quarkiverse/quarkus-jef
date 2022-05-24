
package io.quarkiverse.jef.java.embedded.framework.linux.core.types;

@SuppressWarnings("unused")
public class ByteReference extends Reference {
    private byte value;

    public ByteReference() {
    }

    public ByteReference(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
