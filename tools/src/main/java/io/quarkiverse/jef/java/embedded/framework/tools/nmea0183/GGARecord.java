package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalTime;

@SuppressWarnings("unused")
public class GGARecord extends MarineRecord {
    private LocalTime time;
    private float latitude;
    private Direction latitudeDirection;

    private float longitude;

    private Direction longitudeDirection;
    private GGACalculationMethod calculationMethod;

    private int satellitesAmount;

    private float hdop;

    private float heightAboveSeaLevel;

    private Units heightAboveSeaLevelUnits;

    private float differenceBetweenSeaLevel;

    private Units differenceBetweenSeaLevelUnits;

    private int correctionSeconds;

    private String baseStationId;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public LocalTime getTime() {
        return time;
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

    public GGACalculationMethod getCalculationMethod() {
        return calculationMethod;
    }

    public int getSatellitesAmount() {
        return satellitesAmount;
    }

    public float getHdop() {
        return hdop;
    }

    public float getHeightAboveSeaLevel() {
        return heightAboveSeaLevel;
    }

    public Units getHeightAboveSeaLevelUnits() {
        return heightAboveSeaLevelUnits;
    }

    public float getDifferenceBetweenSeaLevel() {
        return differenceBetweenSeaLevel;
    }

    public Units getDifferenceBetweenSeaLevelUnits() {
        return differenceBetweenSeaLevelUnits;
    }

    public int getCorrectionSeconds() {
        return correctionSeconds;
    }

    public String getBaseStationId() {
        return baseStationId;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1: // Time
                time = ParseTools.parseTime(chars);
                break;
            case 2: // Latitude
                latitude = ParseTools.parseLongLat(chars);
                break;
            case 3: // Latitude direction
                latitudeDirection = Direction.valueOf(ParseTools.parseString(chars));
                break;
            case 4: // Longitude
                longitude = ParseTools.parseLongLat(chars);
                break;
            case 5: // Longitude direction
                longitudeDirection = Direction.valueOf(ParseTools.parseString(chars));
                break;
            case 6: // Calculation Method
                calculationMethod = GGACalculationMethod.value(ParseTools.parseInt(chars, 0));
                break;
            case 7: // satellites amount
                satellitesAmount = ParseTools.parseInt(chars, 0);
                break;
            case 8: // HDOP
                hdop = ParseTools.parseFloat(chars, Float.NaN);
                break;
            case 9: // Height above sea level
                heightAboveSeaLevel = ParseTools.parseFloat(chars, Float.NaN);
                break;
            case 10: // Height above sea level (unit)
                heightAboveSeaLevelUnits = ParseTools.parseUnits(chars);
                break;
            case 11: // Difference between earth ellipsoid and sea level
                differenceBetweenSeaLevel = ParseTools.parseFloat(chars, Float.NaN);
                break;
            case 12: // Difference between earth ellipsoid and sea level (units)
                differenceBetweenSeaLevelUnits = ParseTools.parseUnits(chars);
                break;
            case 13: // The number of seconds since the last DGPS correction was received
                correctionSeconds = ParseTools.parseInt(chars, -1);
                break;
            case 14: // The ID of the base station providing the DGPS corrections (if DGPS is enabled).
                baseStationId = ParseTools.parseString(chars);
                break;
            default:
                throw new IOException("Invalid token index: " + token);
        }
    }

    @Override
    public String toString() {
        return "GGARecord{" +
                "time=" + time +
                ", latitude=" + latitude +
                ", latitudeDirection=" + latitudeDirection +
                ", longitude=" + longitude +
                ", longitudeDirection=" + longitudeDirection +
                ", calculationMethod=" + calculationMethod +
                ", satellitesAmount=" + satellitesAmount +
                ", hdop=" + hdop +
                ", heightAboveSeaLevel=" + heightAboveSeaLevel +
                ", heightAboveSeaLevelUnits=" + heightAboveSeaLevelUnits +
                ", differenceBetweenSeaLevel=" + differenceBetweenSeaLevel +
                ", differenceBetweenSeaLevelUnits=" + differenceBetweenSeaLevelUnits +
                ", correctionSeconds=" + correctionSeconds +
                ", baseStationId='" + baseStationId + '\'' +
                '}';
    }

    public enum GGACalculationMethod {
        NOT_AVAILABLE,
        OFFLINE,
        DIFFERENTIALLY,
        PPS,
        FIXED_RTK,
        NON_FIXED_RTK,
        EXTRAPOLATION,
        FIXED_COORDS,
        SIMULATION_MODE;

        public static GGACalculationMethod value(int i) {
            return GGACalculationMethod.values()[i];
        }
    }
}
