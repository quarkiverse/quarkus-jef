package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;

@SuppressWarnings("unused")
public class VTGRecord extends MarineRecord {
    private float path;
    private boolean courseValidityFlag;
    private float magneticDeclination;
    private String magnetic;
    private float speed1;
    private Units speed1Type;
    private float speed2;
    private Units speed2Type;
    private CoordinateCalculationMethod method;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public float getPath() {
        return path;
    }

    public boolean isCourseValidityFlag() {
        return courseValidityFlag;
    }

    public float getMagneticDeclination() {
        return magneticDeclination;
    }

    public String getMagnetic() {
        return magnetic;
    }

    public float getSpeed1() {
        return speed1;
    }

    public Units getSpeed1Type() {
        return speed1Type;
    }

    public float getSpeed2() {
        return speed2;
    }

    public Units getSpeed2Type() {
        return speed2Type;
    }

    public CoordinateCalculationMethod getMethod() {
        return method;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1:
                path = ParseTools.parseFloat(chars);
                break;
            case 2:
                courseValidityFlag = "T".equals(ParseTools.parseString(chars));
                break;
            case 3:
                magneticDeclination = ParseTools.parseFloat(chars);
                break;
            case 4:
                magnetic = ParseTools.parseString(chars);
                break;
            case 5:
                speed1 = ParseTools.parseFloat(chars);
                break;
            case 6:
                speed1Type = Units.valueOf(ParseTools.parseString(chars));
                break;
            case 7:
                speed2 = ParseTools.parseFloat(chars);
                break;
            case 8:
                speed2Type = Units.valueOf(ParseTools.parseString(chars));
                break;
            case 9:
                method = CoordinateCalculationMethod.valueOf(ParseTools.parseString(chars));
                break;
        }
    }

    @Override
    public String toString() {
        return "VTGRecord{" +
                "path=" + path +
                ", courseValidityFlag=" + courseValidityFlag +
                ", magneticDeclination=" + magneticDeclination +
                ", magnetic='" + magnetic + '\'' +
                ", speed1=" + speed1 +
                ", speed1Type=" + speed1Type +
                ", speed2=" + speed2 +
                ", speed2Type=" + speed2Type +
                ", method=" + method +
                '}';
    }
}
