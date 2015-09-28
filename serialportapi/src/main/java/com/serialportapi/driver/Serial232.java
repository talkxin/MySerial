package com.serialportapi.driver;

import android.content.Context;

import com.serialportapi.SerialCommunication;
import com.serialportapi.common.ConnectedTask;
import com.serialportapi.common.DataQueue;
import com.serialportapi.mean.MessageDecoder;
import com.serialportapi.mean.MessageHandler;
import com.serialportapi.serialport_api.SerialPort;
import com.serialportapi.thread.Event;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 15-9-23.
 * 232串口子类，用于开启串口，并且与串口进行数据交互
 * 使用前提必须root
 */
public class Serial232 extends SerialCommunication {
    /**
     * 设备目录
     */
    private final String devurl = "/dev/";

    /**
     * 串口通讯类
     */
    private SerialPort mSerialPort;

    /**
     * 数据发送
     */
    private OutputStream mSPOutput;

    /**
     * 构造方法，接收处理的handler
     *
     * @param context
     */
    Serial232(Context context, MessageHandler handler) {
        super(context, handler);
    }

    @Override
    public void setDecoder(MessageDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public List<String> getDriverList(int driver) {
        File root = new File(devurl);
        File[] files = root.listFiles();
        List<String> drivers = new ArrayList<String>();
        for (File file : files) {
            if (file.isDirectory())
                drivers.add(file.getAbsolutePath());
        }
        return null;
    }

    @Override
    public boolean initSerialPort(String devName, int baudrate) {
        try {
            if (outQueue != null) {
                outQueue.shutdown();
                outQueue = null;
            }
            if (mConnectedTask != null) {
                mConnectedTask.shutdown();
                mConnectedTask = null;
            }
            if (mSerialPort != null) {
                mSerialPort.close();
                mSerialPort = null;
            }
            // 初始化端口
            mSerialPort = new SerialPort(new File(devName), baudrate, 0);
            // 输出io
            mSPOutput = mSerialPort.getOutputStream();
            // 开启队列
            outQueue = new DataQueue(context, handler);
            outQueue.setDecoder(decoder);
            outQueue.setRunnableTask(outQueue);
            outQueue.enable();
            // 开启通信
            mConnectedTask = new ConnectedTask(context, handler);
            mConnectedTask.setDataQueue(outQueue);
            mConnectedTask.setConnectedDevice(mSerialPort);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean sendCommend(int commend, byte[] data) {
        try {
            if (mSPOutput != null) {
                mSPOutput.write(data);
                return true;
            }
        } catch (IOException e) {
            // 信息发送失败
            handler.messageEvent(context, Event.EXCEPTION, "发送信息失败");
            return false;
        }
        return false;
    }

    @Override
    public boolean closeDevice() {
        try {
            if (mConnectedTask != null) {
                mConnectedTask.shutdown();
                mConnectedTask = null;
            }
            if (outQueue != null) {
                outQueue.shutdown();
                outQueue = null;
            }
            if (mSPOutput != null) {
                mSPOutput.close();
                mSPOutput = null;
            }
            if (mSerialPort != null) {
                mSerialPort.close();
                mSerialPort = null;
            }
        } catch (Exception e) {
            handler.messageEvent(context, Event.EXCEPTION, "通道关闭失败");
        }
        return false;
    }

}
