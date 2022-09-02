package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GSVRecord extends MarineRecord {
    private int amountOfMessages;
    private int messageNumber;
    private int satelliteAmout;

    private List<SatelliteInfo> satelliteInfo = new ArrayList<>();

    private int additionalParameter;
    private SatelliteInfo current;

    private int frame = 1;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public int getAmountOfMessages() {
        return amountOfMessages;
    }

    public void setAmountOfMessages(int amountOfMessages) {
        this.amountOfMessages = amountOfMessages;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public int getSatelliteAmout() {
        return satelliteAmout;
    }

    public void setSatelliteAmout(int satelliteAmout) {
        this.satelliteAmout = satelliteAmout;
    }

    public List<SatelliteInfo> getSatelliteInfo() {
        return satelliteInfo;
    }

    public void setSatelliteInfo(List<SatelliteInfo> satelliteInfo) {
        this.satelliteInfo = satelliteInfo;
    }

    public int getAdditionalParameter() {
        return additionalParameter;
    }

    public void setAdditionalParameter(int additionalParameter) {
        this.additionalParameter = additionalParameter;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1:
                amountOfMessages = ParseTools.parseInt(chars, 1);
                return;
            case 2:
                messageNumber = ParseTools.parseInt(chars, 1);
                return;
            case 3:
                satelliteAmout = ParseTools.parseInt(chars, 0);
                return;
        }

        switch (frame) {
            case 1:
                current = new SatelliteInfo();
                current.id = ParseTools.parseInt(chars, -1);
                break;
            case 2:
                current.angle = ParseTools.parseInt(chars, -1);
                break;
            case 3:
                current.azimuth = ParseTools.parseInt(chars, -1);
                break;
            case 4:
                current.snr = ParseTools.parseInt(chars, -1);
                satelliteInfo.add(current);
                frame = 1;
                return;
        }
        frame++;
    }

    @Override
    protected void beforeEnd() {
        if (frame == 2) {
            additionalParameter = current.id;
        }
    }

    @Override
    public String toString() {
        return "GSVRecord{" +
                "amountOfMessages=" + amountOfMessages +
                ", messageNumber=" + messageNumber +
                ", satelliteAmout=" + satelliteAmout +
                ", satelliteInfo=" + satelliteInfo +
                ", additionalParameter=" + additionalParameter +
                ", current=" + current +
                ", frame=" + frame +
                '}';
    }

    public static final class SatelliteInfo {
        public int id;
        public int angle;
        public int azimuth;
        public int snr;

        public int getId() {
            return id;
        }

        public int getAngle() {
            return angle;
        }

        public int getAzimuth() {
            return azimuth;
        }

        public int getSnr() {
            return snr;
        }
    }
}
