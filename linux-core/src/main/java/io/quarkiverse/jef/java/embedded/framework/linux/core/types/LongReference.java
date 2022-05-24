
package io.quarkiverse.jef.java.embedded.framework.linux.core.types;

@SuppressWarnings("unused")
public class LongReference extends Reference {
    private long value;

    public LongReference(long value) {
        this.value = value;
    }

    public LongReference() {
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongReference{" +
                "value=" + value +
                '}';
    }
}
