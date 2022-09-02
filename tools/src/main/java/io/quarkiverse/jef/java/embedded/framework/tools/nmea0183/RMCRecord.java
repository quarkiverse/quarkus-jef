package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalTime;

@SuppressWarnings("unused")
public class RMCRecord extends MarineRecord {
    private LocalTime time;
    private ReliabilityOfReceivedCoordinates reliability;
    private float latitude;
    private Direction latitudeDirection;
    private float longitude;
    private Direction longitudeDirection;
    private float speedKnots;
    private float path;
    private LocalDate date;
    private float magneticDeclination;
    private String directionOfMagneticDeclination;
    private CoordinateCalculationMethod method;
    private String status;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public LocalTime getTime() {
        return time;
    }

    public ReliabilityOfReceivedCoordinates getReliability() {
        return reliability;
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

    public float getSpeedKnots() {
        return speedKnots;
    }

    public float getPath() {
        return path;
    }

    public LocalDate getDate() {
        return date;
    }

    public float getMagneticDeclination() {
        return magneticDeclination;
    }

    public String getDirectionOfMagneticDeclination() {
        return directionOfMagneticDeclination;
    }

    public CoordinateCalculationMethod getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1:
                time = ParseTools.parseTime(chars);
                break;
            case 2:
                reliability = ReliabilityOfReceivedCoordinates.valueOf(ParseTools.parseString(chars));
                break;
            case 3:
                latitude = ParseTools.parseLongLat(chars);
                break;
            case 4:
                latitudeDirection = Direction.valueOf(ParseTools.parseString(chars));
                break;
            case 5:
                longitude = ParseTools.parseLongLat(chars);
                break;
            case 6:
                longitudeDirection = Direction.valueOf(ParseTools.parseString(chars));
                break;
            case 7:
                speedKnots = ParseTools.parseFloat(chars);
                break;
            case 8:
                path = ParseTools.parseFloat(chars);
                break;
            case 9:
                date = ParseTools.parseDate(chars);
                break;
            case 10:
                magneticDeclination = ParseTools.parseFloat(chars);
                break;
            case 11:
                directionOfMagneticDeclination = ParseTools.parseString(chars);
                break;
            case 12:
                method = CoordinateCalculationMethod.valueOf(ParseTools.parseString(chars));
                break;
            case 13:
                status = ParseTools.parseString(chars);
                break;
        }
    }

    @Override
    public String toString() {
        return "RMCRecord{" +
                "time=" + time +
                ", reliability=" + reliability +
                ", latitude=" + latitude +
                ", latitudeDirection=" + latitudeDirection +
                ", longitude=" + longitude +
                ", longitudeDirection=" + longitudeDirection +
                ", speedKnots=" + speedKnots +
                ", path=" + path +
                ", date=" + date +
                ", magneticDeclination=" + magneticDeclination +
                ", directionOfMagneticDeclination='" + directionOfMagneticDeclination + '\'' +
                ", method=" + method +
                ", status='" + status + '\'' +
                '}';
    }
}
