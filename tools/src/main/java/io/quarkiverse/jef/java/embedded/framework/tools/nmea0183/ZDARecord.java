package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalTime;

@SuppressWarnings("unused")
public class ZDARecord extends MarineRecord {
    private LocalTime time;
    private int day;
    private int month;
    private int year;
    private int timezoneHours;
    private int timezoneMinutes;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public LocalTime getTime() {
        return time;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getTimezoneHours() {
        return timezoneHours;
    }

    public int getTimezoneMinutes() {
        return timezoneMinutes;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1:
                time = ParseTools.parseTime(chars);
                break;
            case 2:
                day = ParseTools.parseInt(chars);
                break;
            case 3:
                month = ParseTools.parseInt(chars);
                break;
            case 4:
                year = ParseTools.parseInt(chars);
                break;
            case 5:
                timezoneHours = ParseTools.parseInt(chars);
                break;
            case 6:
                timezoneMinutes = ParseTools.parseInt(chars);
                break;
        }
    }

    @Override
    public String toString() {
        return "ZDARecord{" +
                "time=" + time +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", timezoneHours=" + timezoneHours +
                ", timezoneMinutes=" + timezoneMinutes +
                '}';
    }
}
