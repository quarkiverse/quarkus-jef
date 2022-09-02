package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalTime;

@SuppressWarnings("unused")
public class DHVRecord extends MarineRecord {
    private LocalTime time;
    private float speed3d;
    private float speedEcefX;
    private float speedEcefY;
    private float speedEcefZ;
    private float speed;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public LocalTime getTime() {
        return time;
    }

    public float getSpeed3d() {
        return speed3d;
    }

    public float getSpeedEcefX() {
        return speedEcefX;
    }

    public float getSpeedEcefY() {
        return speedEcefY;
    }

    public float getSpeedEcefZ() {
        return speedEcefZ;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1:
                time = ParseTools.parseTime(chars);
                break;
            case 2:
                speed3d = ParseTools.parseFloat(chars);
                break;
            case 3:
                speedEcefX = ParseTools.parseFloat(chars);
                break;
            case 4:
                speedEcefY = ParseTools.parseFloat(chars);
                break;
            case 5:
                speedEcefZ = ParseTools.parseFloat(chars);
                break;
            case 6:
                speed = ParseTools.parseFloat(chars);
                break;
        }
    }

    @Override
    public String toString() {
        return "DHVRecord{" +
                "time=" + time +
                ", speed3d=" + speed3d +
                ", speedEcefX=" + speedEcefX +
                ", speedEcefY=" + speedEcefY +
                ", speedEcefZ=" + speedEcefZ +
                ", speed=" + speed +
                '}';
    }
}
