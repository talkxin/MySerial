package com.serialportapi.common;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.impl.conn.Wire;

import android.content.Context;
import android.os.IBinder;

import com.serialportapi.iobuffer.IoBuffer;
import com.serialportapi.mean.MessageDecoder;
import com.serialportapi.mean.MessageDecoderOut;
import com.serialportapi.mean.MessageHandler;
import com.serialportapi.thread.Event;
import com.serialportapi.thread.RunnableTask;
import com.serialportapi.thread.RunnableTaskIf;


public class DataQueue extends RunnableTask implements RunnableTaskIf,
		MessageDecoderOut {
	private MessageHandler handler;
	private Context context;
	private MessageDecoder decoder;
	protected IoBuffer queue;

	public DataQueue(Context context, MessageHandler handler) {
		super(context, handler);
		setRunnableTask(this);
		// 初始化缓存
		this.context = context;
		queue = IoBuffer.allocate(0);
		queue.setAutoExpand(true);
		queue.setAutoShrink(true);
		this.handler = handler;
	}

	/**
	 * 设置过滤器
	 * 
	 * @param decoder
	 */
	public void setDecoder(MessageDecoder decoder) {
		this.decoder = decoder;
	}

	@Override
	public boolean taskPreparing() {
		// TODO Auto-generated method stub
		this.event = Event.PREPARING;
		handler.messageEvent(context, event, "任务准备");
		return true;
	}

	/**
	 * 数据队列添加数据
	 * 
	 * @param data
	 */
	public void put(byte[] data) {
		try {
			synchronized (queue) {
				if (data != null) {
					queue.put(data);
					queue.flip();
				}
			}
		} catch (Exception e) {
			queue.clear();
		}
	}

	/**
	 * 清空数据队列
	 */
	public void clear() {
		queue.clear();
	}

	/**
	 * 开始任务
	 */
	public void enable() {
		queue.clear();
		startup();
	}

	/**
	 * 结束任务
	 */
	public void disable() {
		shutdown();
	}

	@Override
	public void taskRunning() {
		synchronized (queue) {
			IoBuffer in = queue;
			try {
				if (in.remaining() != 0) {
					if (decoder != null) {
						// 过滤数据
						if (!decoder.doDecode(in, this)) {

							byte[] out = new byte[in.remaining()];
							in.get(out);
							queue.clear();
							queue.put(out);
							queue.flip();
						} else {
							queue = IoBuffer.allocate(0);
							queue.setAutoExpand(true);
							queue.setAutoShrink(true);
						}
					} else {
						// 直接输出
						this.write(in.array());
						queue.clear();
					}
				}
			} catch (Exception e) {
				queue.clear();
			}
		}
	}

	@Override
	public void taskCompletion() {
		this.event = Event.COMPLETION;
		handler.messageEvent(context, event, "任务完成");
	}

	@Override
	public int getSleep() {
		return 0;
	}

	/**
	 * 输出至handler
	 */
	@Override
	public void write(final Object obj) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				handler.messageReceived(context, obj);
			}
		}.start();
	}
}
