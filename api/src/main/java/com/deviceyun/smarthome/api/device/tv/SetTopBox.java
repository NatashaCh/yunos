package com.deviceyun.smarthome.api.device.tv;

import com.deviceyun.smarthome.api.device.Device;

public interface SetTopBox extends Device {
	// =========manufacturer specification keys begin===================
	static final String SPEC_ABC = "abc";

	// =========functions begin===================

	void on();

	void suspend();

	void setVolume(int vol);

	void liveTv(int channel);

	void recordedTv(String program);

	void movie(String name);

}
