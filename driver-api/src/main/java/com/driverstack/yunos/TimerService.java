package com.driverstack.yunos;

import com.driverstack.yunos.driver.device.TimerListener;

public interface TimerService {

	/**
	 * 
	 * @param listener
	 * @param interval
	 *            in milliseconds
	 */
	public abstract Object subscribe(TimerListener listener, int interval, int code);

	public abstract Object subscribe(TimerListener listener, String cronExpression, int code);

	public abstract void unsubscribe(Object jobId);

}