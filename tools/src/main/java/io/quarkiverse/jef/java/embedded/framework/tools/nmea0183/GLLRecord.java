package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalTime;

@SuppressWarnings("unused")
public class GLLRecord extends MarineRecord {
    private float latitude;
    private Direction latitudeDirection;
    private float longitude;
    private Direction longitudeDirection;
    private LocalTime time;
    private ReliabilityOfReceivedCoordinates reliability;
    private CoordinateCalculationMethod method = CoordinateCalculationMethod.NA;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public float getLatitude() {
        return latitude;
    }

    public Direction getLatitudeDirection() {
        return latitudeDirection;
    }

    public float getLongitude() {
        return longitude;
    }

    public Direction getLongitudeDirection() {
        return longitudeDirection;
    }

    public LocalTime getTime() {
        return time;
    }

    public ReliabilityOfReceivedCoordinates getReliability() {
        return reliability;
    }

    public CoordinateCalculationMethod getMethod() {
        return method;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1:
                latitude = ParseTools.parseLongLat(chars);
                break;
            case 2:
                latitudeDirection = Direction.valueOf(ParseTools.parseString(chars));
                break;
            case 3:
                longitude = ParseTools.parseLongLat(chars);
                break;
            case 4:
                longitudeDirection = Direction.valueOf(ParseTools.parseString(chars));
                break;
            case 5:
                time = ParseTools.parseTime(chars);
                break;
            case 6:
                reliability = ReliabilityOfReceivedCoordinates.valueOf(ParseTools.parseString(chars));
                break;
            case 7:
                method = CoordinateCalculationMethod.valueOf(ParseTools.parseString(chars));
                break;
            default:
                throw new IOException("Invalid token index: " + token);
        }
    }

    @Override
    public String toString() {
        return "GLLRecord{" +
                "latitude=" + latitude +
                ", latitudeDirection=" + latitudeDirection +
                ", longitude=" + longitude +
                ", longitudeDirection=" + longitudeDirection +
                ", time=" + time +
                ", reliability=" + reliability +
                ", method=" + method +
                '}';
    }
}
