package com.serialportapi.mean;


import com.serialportapi.iobuffer.IoBuffer;

/**
 * 信息解码器 自定义处理流传过来的包
 * 
 * @author talkliu
 * 
 */
public abstract class MessageDecoder {

	/**
	 * 处理解码 这个方法的返回值是重点： 1、当内容刚好时，返回false，告知主类接收下一批内容
	 * 2、内容不够时需要下一批发过来的内容，此时返回false，这样主类 会将内容放进队列中，等下次来数据后就自动拼装再交给本类的doDecode
	 * 3、当返回true时，不论队列中有多少等待数据均一次清空 4、若处理完依旧含有数据则返回false，等待下次数据再次拼装
	 * 
	 * @param in
	 * @param out
	 * @return
	 * @throws Exception
	 */
	public abstract boolean doDecode(IoBuffer in, MessageDecoderOut out);
}
