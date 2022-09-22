package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public abstract class MarineRecord {
    protected Satellite satellite;
    private int checksum;

    public void setSatellite(Satellite satellite) {
        this.satellite = satellite;
    }

    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    public Satellite satellite() {
        return satellite;
    }

    public abstract void fire(Nmea0183Listener listener);

    public void parse(ReadableByteChannel channel) throws IOException {
        int token = 0;
        // TODO: move ch and chars to top class
        ByteBuffer ch = ByteBuffer.allocateDirect(1).rewind();
        ByteBuffer chars = ByteBuffer.allocateDirect(0xFF).rewind();
        beforeStart();
        boolean check = false;
        int read;
        while ((read = channel.read(ch)) != -1) {
            byte b = ch.get(0);
            ch.rewind();
            //System.out.print((char) b);
            if (b == '\r' || b == '\n') {
                validateChecksum(chars.limit(chars.position()).rewind());
                //System.out.println();
                break;
            } else if (b == ',') {
                checksum = checksum ^ b;
                if (chars.position() != 0) { // empty value or first ','
                    // TODO: parse checksum after tokens
                    parseToken(token, chars.limit(chars.position()).rewind());
                }
                token++;
                chars.clear();
            } else {
                if (b == '*') {
                    check = true;
                    parseToken(token, chars.limit(chars.position()).rewind());
                    chars.clear();
                    beforeEnd();
                    continue;
                }
                if (!check) {
                    checksum = checksum ^ b;
                }
                chars.put(b);
            }
        }
    }

    private void validateChecksum(ByteBuffer chars) throws IOException {
        // TODO validate checksum
        String hex = ParseTools.parseString(chars);
        int value = Integer.parseInt(hex, 16);

        if (value != checksum) {
            throw new IOException("Invalid checksum. Expected '" + checksum + "' received '" + value + "'");
        }
    }

    protected void beforeStart() {

    }

    protected void beforeEnd() {
    }

    protected abstract void parseToken(int token, ByteBuffer chars) throws IOException;
}
