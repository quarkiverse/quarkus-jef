package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalTime;

@SuppressWarnings("unused")
public class GSTRecord extends MarineRecord {
    private LocalTime time;
    private float rms;
    private float errorEllipseSemiMajorAxis;
    private float errorEllipseSemiMinorAxis;
    private float errorEllipseOrientation;
    private float latitudeSigmaError;
    private float longitudeSigmaError;
    private float heightSigmaError;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public LocalTime getTime() {
        return time;
    }

    public float getRms() {
        return rms;
    }

    public float getErrorEllipseSemiMajorAxis() {
        return errorEllipseSemiMajorAxis;
    }

    public float getErrorEllipseSemiMinorAxis() {
        return errorEllipseSemiMinorAxis;
    }

    public float getErrorEllipseOrientation() {
        return errorEllipseOrientation;
    }

    public float getLatitudeSigmaError() {
        return latitudeSigmaError;
    }

    public float getLongitudeSigmaError() {
        return longitudeSigmaError;
    }

    public float getHeightSigmaError() {
        return heightSigmaError;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1:
                time = ParseTools.parseTime(chars);
                break;
            case 2:
                rms = ParseTools.parseFloat(chars);
                break;
            case 3:
                errorEllipseSemiMajorAxis = ParseTools.parseFloat(chars);
                break;
            case 4:
                errorEllipseSemiMinorAxis = ParseTools.parseFloat(chars);
                break;
            case 5:
                errorEllipseOrientation = ParseTools.parseFloat(chars);
                break;
            case 6:
                latitudeSigmaError = ParseTools.parseFloat(chars);
                break;
            case 7:
                longitudeSigmaError = ParseTools.parseFloat(chars);
                break;
            case 8:
                heightSigmaError = ParseTools.parseFloat(chars);
                break;
        }
    }

    @Override
    public String toString() {
        return "GSTRecord{" +
                "time=" + time +
                ", rms=" + rms +
                ", errorEllipseSemiMajorAxis=" + errorEllipseSemiMajorAxis +
                ", errorEllipseSemiMinorAxis=" + errorEllipseSemiMinorAxis +
                ", errorEllipseOrientation=" + errorEllipseOrientation +
                ", latitudeSigmaError=" + latitudeSigmaError +
                ", longitudeSigmaError=" + longitudeSigmaError +
                ", heightSigmaError=" + heightSigmaError +
                '}';
    }
}
