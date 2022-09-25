package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public class Nmea0183 {
    private final ReadableByteChannel channel;
    private final Nmea0183Listener listener;

    public Nmea0183(InputStream is, Nmea0183Listener listener) {
        this(Channels.newChannel(is), listener);
    }

    public Nmea0183(ReadableByteChannel channel, Nmea0183Listener listener) {
        this.channel = channel;
        this.listener = listener;
    }

    public void start() throws IOException {
        ByteBuffer buf = ByteBuffer.allocateDirect(1).rewind();
        while (channel.read(buf) != -1) {
            byte b = buf.get(0);
            if (b == '$') {
                beginParse();
            }
            buf.clear();
        }
    }

    private void beginParse() throws IOException {
        ByteBuffer buf = ByteBuffer.allocateDirect(3);
        buf.limit(2).rewind();
        channel.read(buf);
        String sat = StandardCharsets.UTF_8.decode(buf.rewind()).toString();
        Satellite satellite = Satellite.valueOf(sat);

        buf.limit(3).rewind();
        channel.read(buf);
        String ident = StandardCharsets.UTF_8.decode(buf.rewind()).toString();
        //System.out.print(sat);
        //System.out.print(ident);

        MarineRecord record;
        switch (ident) {
            case "DHV":
                record = new DHVRecord();
                break;
            case "GGA":
                record = new GGARecord();
                break;
            case "GLL":
                record = new GLLRecord();
                break;
            case "GSA":
                record = new GSARecord();
                break;
            case "GST":
                record = new GSTRecord();
                break;
            case "GSV":
                record = new GSVRecord();
                break;
            case "RMC":
                record = new RMCRecord();
                break;
            case "TXT":
                record = new TXTRecord();
                break;
            case "VTG":
                record = new VTGRecord();
                break;
            case "ZDA":
                record = new ZDARecord();
                break;
            default:
                return;
            //throw new IOException("Unable to parse record with prefix: " + ident);
        }
        record.setSatellite(satellite);
        record.setChecksum(calculateChecksum(sat, ident));
        record.parse(channel);
        record.fire(listener);
    }

    private int calculateChecksum(String sat, String ident) {
        int i = 0;
        byte[] bytes = String.join("", sat, ident).getBytes();
        for (byte b : bytes) {
            i = i ^ b;
        }
        return i;
    }
}
