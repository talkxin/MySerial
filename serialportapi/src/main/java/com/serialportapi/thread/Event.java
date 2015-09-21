package com.serialportapi.thread;

/**
 * 各种状态及状态值
 * 
 * @author talkliu
 *
 */
public enum Event {
	/**
	 * 停止状态
	 */
	STOPPED(0),
	/**
	 * 等待运行状态
	 */
	WAITING(1),
	/**
	 * 准备运行状态
	 */
	PREPARING(2),
	/**
	 * 运行状态
	 */
	RUNNING(3),
	/**
	 * 正在停止状态
	 */
	STOPPING(4),
	/**
	 * 运行完成状态
	 */
	COMPLETION(5),
	/**
	 * 异常状态
	 */
	EXCEPTION(-1);

	private int status;

	private Event(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(status);
	}
}
