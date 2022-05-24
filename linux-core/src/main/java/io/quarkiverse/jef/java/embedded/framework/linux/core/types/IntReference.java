
package io.quarkiverse.jef.java.embedded.framework.linux.core.types;

@SuppressWarnings("unused")
public class IntReference extends Reference {
    private int value;

    public IntReference() {
    }

    public IntReference(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
