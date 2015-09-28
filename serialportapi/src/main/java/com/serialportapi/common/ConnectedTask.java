package com.serialportapi.common;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.serialportapi.mean.MessageHandler;
import com.serialportapi.serialport_api.SerialPort;
import com.serialportapi.thread.Event;
import com.serialportapi.thread.RunnableTask;
import com.serialportapi.thread.RunnableTaskIf;
import com.serialportapi.usbdriver_api.UsbSerialDriver;

/**
 * 通信类
 *
 * @author talkliu
 */
public class ConnectedTask extends RunnableTask implements RunnableTaskIf {
    private Context ct;
    private DataQueue outQueue;// 数据队列
    private SerialPort sp;// 串口
    private UsbSerialDriver sDriver;// usb 串口
    private InputStream mSPInput;// 数据输入
    private MessageHandler handler;
    private Context context;
    private int readBytes;
    private int sleep = 0;
    private int bufferLength = 1024;
    private byte[] buffer = new byte[bufferLength];// 缓存

    public ConnectedTask(Context context, MessageHandler handler) {
        super(context, handler);
        this.handler = handler;
        this.context = context;
        this.sleep = sleep;
        this.setRunnableTask(this);
    }

    /**
     * 指定队列
     *
     * @param outQueue
     */
    public void setDataQueue(DataQueue outQueue) {
        this.outQueue = outQueue;
    }

    /**
     * 指定缓存大小
     *
     * @param bufferLength
     */
    public void setBufferLength(int bufferLength) {
        this.bufferLength = bufferLength;
    }

    /**
     * 开启串口控制
     *
     * @param sp
     */
    public void setConnectedDevice(SerialPort sp) {
        if (sp != null) {
            this.sp = sp;
            this.mSPInput = sp.getInputStream();
        }
        startup();
    }

    /**
     * 开启usb串口控制
     *
     * @param sDriver
     */
    public void setConnectedDevice(UsbSerialDriver sDriver) {
        this.sDriver = sDriver;
        startup();
    }

    /**
     * 结束任务
     */
    public void disconnectDevice() {
        shutdown();
    }

//	/**
//	 * 清空队列
//	 */
//	public void clearQueue() {
//		outQueue.clear();
//	}

    @Override
    public boolean taskPreparing() {
        // TODO Auto-generated method stub
        this.event = Event.PREPARING;
        handler.messageEvent(context, event, "准备开始");
        return true;
    }

    @Override
    public void taskRunning() {
        // TODO Auto-generated method stub

        try {
            if (sp != null) {
                readBytes = mSPInput.read(buffer);
            } else {
                readBytes = sDriver.read(buffer, 0);
            }
            if (readBytes > 0) {
                if (outQueue != null) {
                    byte[] data = new byte[readBytes];
                    for (int i = 0; i < readBytes; i++) {
                        data[i] = buffer[i];
                    }
                    outQueue.put(data);
                }
            }
        } catch (IOException e) {
            handler.messageEvent(context, Event.EXCEPTION, "数据队列异常");
        }
    }

    @Override
    public void taskCompletion() {
        if (sp != null) {
            sp.close();
            sp = null;
        }
        this.event = Event.COMPLETION;
        handler.messageEvent(context, event, "任务完成");
    }

    @Override
    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    @Override
    public int getSleep() {
        return sleep;
    }

}
