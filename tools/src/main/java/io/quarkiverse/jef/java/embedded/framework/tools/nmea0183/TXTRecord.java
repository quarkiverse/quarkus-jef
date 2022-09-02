package io.quarkiverse.jef.java.embedded.framework.tools.nmea0183;

import java.io.IOException;
import java.nio.ByteBuffer;

@SuppressWarnings("unused")
public class TXTRecord extends MarineRecord {
    private int lines;
    private int line;
    private MessageType type;
    private String message;

    @Override
    public void fire(Nmea0183Listener listener) {
        listener.handle(this);
    }

    public int getLines() {
        return lines;
    }

    public int getLine() {
        return line;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected void parseToken(int token, ByteBuffer chars) throws IOException {
        switch (token) {
            case 1:
                lines = ParseTools.parseInt(chars);
                break;
            case 2:
                line = ParseTools.parseInt(chars);
                break;
            case 3:
                type = MessageType.values()[ParseTools.parseInt(chars, 0)];
                break;
            case 4:
                message = ParseTools.parseString(chars);
                break;
        }
    }

    @Override
    public String toString() {
        return "TXTRecord{" +
                "lines=" + lines +
                ", line=" + line +
                ", type=" + type +
                ", message='" + message + '\'' +
                '}';
    }

    public enum MessageType {
        ERROR,
        WARNING,
        NOTIFICATION,
        CUSTOM;
    }
}
