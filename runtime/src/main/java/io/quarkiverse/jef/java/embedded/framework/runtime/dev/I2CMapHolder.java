package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBus;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusManager;

@SuppressWarnings("unused")
public class I2CMapHolder {
    private final List<Record> records;
    private final static String[] header7bits = new String[] { " ", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "0A", "0B", "0C", "0D", "0E", "0F" };

    public I2CMapHolder(I2CBusManager mgr) {
        records = new ArrayList<>();
        Map<String, I2CBus> all = mgr.getAll();
        for (Map.Entry<String, I2CBus> entry : all.entrySet()) {
            records.add(new Record(entry.getKey(), entry.getValue()));

        }
    }

    public List<Record> getRecords() {
        return records;
    }

    public static class Record {
        private final String name;
        private final List<List<String>> lines;

        public Record(String key, I2CBus value) {
            this.name = key;
            this.lines = new ArrayList<>();
            enumerate(value);
        }

        public String getName() {
            return name;
        }

        public List<List<String>> getLines() {
            return lines;
        }

        private void enumerate(I2CBus value) {
            List<I2CBus.Status> enumerate = value.enumerate();
            fixSize(enumerate);
            lines.add(List.of(header7bits));
            for (int i = 0; i < 128; i += 16) {
                List<String> line = new ArrayList<>();
                line.add(String.format("%02x: ", i));
                for (int j = 0; j < 16; j++) {
                    I2CBus.Status status = enumerate.get(i + j);
                    if (status == I2CBus.Status.AVAILABLE) {
                        line.add(String.format("%02x: ", i + j));
                    } else {
                        line.add("--");
                    }
                }
                lines.add(line);
            }
        }

        private void fixSize(List<I2CBus.Status> list) {
            int size = list.size();
            if (size < 128) {
                int offset = 128 - size;
                for (int i = 0; i < offset; i++) {
                    list.add(I2CBus.Status.SKIP);
                }
            }
        }
    }
}
