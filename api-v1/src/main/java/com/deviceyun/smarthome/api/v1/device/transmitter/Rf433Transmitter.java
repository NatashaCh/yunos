package com.deviceyun.smarthome.api.v1.device.transmitter;

import com.deviceyun.smarthome.api.v1.device.Device;

public interface Rf433Transmitter extends Device {
	void transmit(int pulseLength, long code, int bits);
}
