package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;

import java.io.File;

public class SpiRecord {
    private final String name;
    private String status;
    private String frequency;
    private String clockMode;
    private String wordLength;
    private String bitOrdering;

    public SpiRecord(String key, SpiBus value) {
        this.name = key;
        fill(value);
    }

    private void fill(SpiBus value) {
        try {
            if(new File(value.getBus()).exists()) {
                value.reload();
                status = "Available";
                frequency = String.valueOf(value.getClockFrequency());
                clockMode = String.valueOf(value.getClockMode());
                wordLength = String.valueOf(value.getWordLength());
                bitOrdering = String.valueOf(value.getBitOrdering());
                return;
            }
        } catch (NativeIOException ignored) {
        }
        // bus not available
        status = "Not available";
        frequency = "N/A";
        clockMode = "N/A";
        wordLength = "N/A";
        bitOrdering = "N/A";
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getClockMode() {
        return clockMode;
    }

    public String getWordLength() {
        return wordLength;
    }

    public String getBitOrdering() {
        return bitOrdering;
    }
}
