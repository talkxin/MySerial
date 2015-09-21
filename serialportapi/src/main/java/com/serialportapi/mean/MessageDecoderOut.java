package com.serialportapi.mean;

/**
 * 用于解码后输出至handler
 * 
 * @author talkliu
 * 
 */
public interface MessageDecoderOut {
	/**
	 * 将数据写入至Handler
	 * @param obj
	 */
	public void write(Object obj);
}
