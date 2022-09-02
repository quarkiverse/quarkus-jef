package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GSARecord extends MarineRecord {
    private final List<Integer> satelliteIDs = new ArrayList<>();
    private FormatSelectionMode formatSelectionMode;
    private SelectedFormatMode selectedFormatMode;
    private float pdop;
    private float hdop;
    private float vdop;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public List<Integer> getSatelliteIDs() {
        return satelliteIDs;
    }

    public FormatSelectionMode getFormatSelectionMode() {
        return formatSelectionMode;
    }

    public SelectedFormatMode getSelectedFormatMode() {
        return selectedFormatMode;
    }

    public float getPdop() {
        return pdop;
    }

    public float getHdop() {
        return hdop;
    }

    public float getVdop() {
        return vdop;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1:
                formatSelectionMode = FormatSelectionMode.valueOf(ParseTools.parseString(chars));
                break;
            case 2:
                selectedFormatMode = SelectedFormatMode.values()[ParseTools.parseInt(chars, 1) - 1];
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                int i = ParseTools.parseInt(chars, -1);
                if (i != -1) {
                    satelliteIDs.add(i);
                }
                break;
            case 15:
                pdop = ParseTools.parseFloat(chars);
                break;
            case 16:
                hdop = ParseTools.parseFloat(chars);
                break;
            case 17:
                vdop = ParseTools.parseFloat(chars);
                break;
            case 18:
                satellite = Satellite.values()[ParseTools.parseInt(chars, 1) - 1];
                break;
            default:
                throw new IOException("Invalid token index: " + token);
        }
    }

    public enum FormatSelectionMode {
        A("auto"),
        M("manual");

        private final String value;

        FormatSelectionMode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    public String toString() {
        return "GSARecord{" +
                "satelliteIDs=" + satelliteIDs +
                ", formatSelectionMode=" + formatSelectionMode +
                ", selectedFormatMode=" + selectedFormatMode +
                ", pdop=" + pdop +
                ", hdop=" + hdop +
                ", vdop=" + vdop +
                '}';
    }

    public enum SelectedFormatMode {
        ONE("no resolution"),
        TWO("2D"),
        THREE("3D");

        private final String value;

        SelectedFormatMode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
