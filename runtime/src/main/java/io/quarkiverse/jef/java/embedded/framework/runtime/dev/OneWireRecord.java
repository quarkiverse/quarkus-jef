package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import io.quarkiverse.jef.java.embedded.framework.linux.core.OneWireDevice;

import java.io.File;

public class OneWireRecord {
    private final String name;

    private final String path;
    private final String status;
    public OneWireRecord(String key, OneWireDevice value) {
        this.name = key;
        this.path = value.getPath();
        this.status = new File(path).exists()?"Available":"Not Available";
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getStatus() {
        return status;
    }
}
