package io.quarkiverse.jef.java.embedded.framework.devices.library.rohm.bh750fvi;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBus;
import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CInterface;
import io.quarkiverse.jef.java.embedded.framework.linux.i2c.SMBus;

// https://github.com/endail/BH1750
// https://github.com/enjoyneering/BH1750FVI/blob/master/src/BH1750FVI.cpp
public class BH1750FVI {
    private static final int BH1750_POWER_DOWN = 0x00; //low power state
    private static final int BH1750_POWER_ON = 0x01; //wating for measurment command
    private static final int BH1750_RESET = 0x07; //soft reset

    private static final int BH1750_MEASUREMENT_TIME_H = 0x40; //changing measurement time register MSB bits
    private static final int BH1750_MEASUREMENT_TIME_L = 0x60; //changing measurement time register LSB bits

    private static final int BH1750_MTREG_DEFAULT = 0x45; //default integration/measurement time, 69
    private static final int BH1750_MTREG_MIN = 0x1F; //minimun integration/measurement time, 31
    private static final int BH1750_MTREG_MAX = 0xFE; //maximum integration/measurement time, 254
    private static final float BH1750_SENSITIVITY_MIN = 0.45f; //minimun sensitivity
    private static final float BH1750_SENSITIVITY_MAX = 3.68f; //maximum sensitivity
    private static final float BH1750_SENSITIVITY_DEFAULT = 1.00f; //default sensitivity

    private static final float BH1750_ACCURACY_DEFAULT = 1.2f; //typical measurement accuracy, sensor calibration
    private static final int BH1750_ERROR = 0x00; //returns 0, if communication error is occurred

    private I2CAddress address;
    private Resolution resolution;
    private I2CBus bus;
    private final SMBus smbus;

    private float _sensitivity;
    private float _accuracy;

    public BH1750FVI(I2CBus bus, I2CAddress address) throws IOException {
        this.bus = bus;
        this.address = address;
        this.smbus = bus.select(address.value).getSmBus();
        this.resolution = Resolution.BH1750_CONTINUOUS_HIGH_RES_MODE;
        this._accuracy = 1;
        this._sensitivity = 1.2f;
    }

    public void setResolution(Resolution res) {
        this.resolution = res;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setSensitivity(float sensitivity) throws IOException {
        float current = _sensitivity;

        float valueMTreg = sensitivity * BH1750_MTREG_DEFAULT;

        /* safety check, make sure valueMTreg never exceeds the limits */
        if (valueMTreg < BH1750_MTREG_MIN) {
            valueMTreg = BH1750_MTREG_MIN;
            _sensitivity = BH1750_SENSITIVITY_MIN;
        } else if (valueMTreg > BH1750_MTREG_MAX) {
            valueMTreg = BH1750_MTREG_MAX;
            _sensitivity = BH1750_SENSITIVITY_MAX;
        } else {
            _sensitivity = sensitivity;
        }

        /* high bit manipulation */
        int measurnentTimeHighBit = (int) valueMTreg >> 5;
        measurnentTimeHighBit = (int) valueMTreg | BH1750_MEASUREMENT_TIME_H; //0,1,0,0  0,7-bit,6-bit,5-bit

        /* low bit manipulation */
        int measurnentTimeLowBit = (int) valueMTreg << 3;
        measurnentTimeLowBit = (int) valueMTreg >> 3;
        measurnentTimeLowBit |= BH1750_MEASUREMENT_TIME_L; //0,1,1,4-bit  3-bit,2-bit,1-bit,0-bit

        try {
            smbus.writeByte(measurnentTimeHighBit);
            smbus.writeByte(measurnentTimeLowBit);
        } catch (IOException e) {
            _sensitivity = current;
            throw new IOException("Unable to set sensitivity", e);
        }
    }

    public float getSensitivity() {
        return _sensitivity;
    }

    public float readLightLevel() throws IOException {
        float integrationTime = 0;
        float lightLevel = 0;

        /* send measurement instruction */
        I2CInterface face = smbus.getInterface();

        try {

            face.write(ByteBuffer.wrap(new byte[] { resolution.getValue() }));
            //smbus.writeByte(resolution.getValue());
        } catch (IOException e) {
            return BH1750_ERROR; //error handler, collision on the i2c bus

        }

        /* measurement delay */
        switch (resolution) //"switch-case" faster & has smaller footprint than "if-else", see Atmel AVR4027 Application Note
        {
            case BH1750_CONTINUOUS_HIGH_RES_MODE:
            case BH1750_CONTINUOUS_HIGH_RES_MODE_2:
            case BH1750_ONE_TIME_HIGH_RES_MODE:
            case BH1750_ONE_TIME_HIGH_RES_MODE_2:
                integrationTime = _sensitivity * 180; //120..180msec * (0.45 .. 3.68)
                break;

            case BH1750_CONTINUOUS_LOW_RES_MODE:
            case BH1750_ONE_TIME_LOW_RES_MODE:
                integrationTime = _sensitivity * 24; //16..24msec * (0.45 .. 3.68)
                break;
        }

        try {
            Thread.sleep((int) integrationTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //delay(integrationTime);

        /*
         * Wire.requestFrom(address.value, 2, true); //"true" to stop message after transmission & releas I2C bus
         *
         * if (Wire.available() != 2) return BH1750_ERROR; //check "wire.h" rxBuffer & collision on the i2c bus
         */
        /* reads MSB byte, LSB byte from "wire.h" rxBuffer */
        byte[] buf = new byte[2];
        face.read(ByteBuffer.wrap(buf));
        //int rawLightLevel  = smbus.readByte() << 8 | smbus.readByte();
        int rawLightLevel = buf[0] & 0xFF << 8 | buf[1] & 0xFF;

        /* light level calculation, p.11 */
        switch (resolution) {
            case BH1750_ONE_TIME_HIGH_RES_MODE_2:
            case BH1750_CONTINUOUS_HIGH_RES_MODE_2:
                lightLevel = (float) 0.5 * (float) rawLightLevel / _accuracy * _sensitivity;
                break;

            case BH1750_ONE_TIME_LOW_RES_MODE:
            case BH1750_ONE_TIME_HIGH_RES_MODE:
            case BH1750_CONTINUOUS_LOW_RES_MODE:
            case BH1750_CONTINUOUS_HIGH_RES_MODE:
                lightLevel = (float) rawLightLevel / _accuracy * _sensitivity;
                break;
        }

        return lightLevel;
    }

    public void powerDown() throws IOException {
        smbus.writeByte(BH1750_POWER_DOWN);
    }

    public void powerOn() throws IOException {
        smbus.writeByte(BH1750_POWER_ON);
    }

    public void reset() throws IOException {
        smbus.writeByte(BH1750_RESET);
    }

    public void setCalibration(float value) {
        /* safety check, make sure value never exceeds calibration range */
        if (value < 0.96)
            _accuracy = 0.96f;
        else if (value > 1.44)
            _accuracy = 1.44f;
        else
            _accuracy = value;
    }

    public float getCalibration() {
        return _accuracy;
    }

    public enum I2CAddress {
        I2C_ADDRESS_1(0x23),
        I2C_ADDRESS_2(0x5C);

        private final int value;

        I2CAddress(int value) {
            this.value = value;
        }
    }
}
