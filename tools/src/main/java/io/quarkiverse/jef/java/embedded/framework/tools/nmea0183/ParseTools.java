package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ParseTools {
    private final static DateTimeFormatter time = DateTimeFormatter.ofPattern("HHmmss.SSS");

    private final static DateTimeFormatter date = DateTimeFormatter.ofPattern("ddMMyy");

    public static LocalTime parseTime(ByteBuffer buf) {
        return LocalTime.parse(parseString(buf), time);
    }

    public static LocalDate parseDate(ByteBuffer buf) {
        return LocalDate.parse(parseString(buf), date);
    }

    public static String parseString(ByteBuffer buf) {
        return StandardCharsets.UTF_8.decode(buf).toString();
    }

    public static int parseInt(ByteBuffer chars) {
        return parseInt(chars, -1);
    }

    public static int parseInt(ByteBuffer chars, int defaultValue) {
        try {
            return Integer.parseInt(parseString(chars));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static float parseFloat(ByteBuffer chars) {
        return parseFloat(chars, Float.NaN);
    }

    public static float parseFloat(ByteBuffer chars, float defaultValue) {
        try {
            return Float.parseFloat(parseString(chars));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Units parseUnits(ByteBuffer chars) {
        return Units.valueOf(parseString(chars));
    }

    public static float parseLongLat(ByteBuffer chars) {
        float f = parseFloat(chars);
        int i = (int) f;
        return (f - i) * 100 / 60;
    }

}
