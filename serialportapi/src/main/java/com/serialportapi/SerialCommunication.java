package com.serialportapi;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.serialportapi.common.ConnectedTask;
import com.serialportapi.common.DataQueue;
import com.serialportapi.mean.MessageDecoder;
import com.serialportapi.mean.MessageHandler;
import com.serialportapi.serialport_api.SerialPort;
import com.serialportapi.thread.Event;
import com.serialportapi.usbdriver_api.UsbSerialDriver;
import com.serialportapi.usbdriver_api.UsbSerialProber;


/**
 * 入口方法
 *
 * 测试tag1.3是否好用
 *
 * @author talkliu
 */
public class SerialCommunication {

    // context
    private Context context;
    // 数据处理handler
    private MessageHandler handler;
    // 数据过滤类
    private MessageDecoder decoder;
    // usb通讯
    private UsbSerialDriver sDriver = null;
    // 串口通讯
    private SerialPort mSerialPort;
    // usb列表
    private static List<String> usbDriver = null;
    // 串口发送数据
    private OutputStream mSPOutput;
    // 通信类
    private ConnectedTask mConnectedTask;
    private DataQueue outQueue;// 数据队列
    public static int sleep = 0;// 休眠时间

    /**
     * 构造方法 初始化context与数据处理handler
     *
     * @param context
     * @param handler
     */
    public SerialCommunication(Context context, MessageHandler handler) {
        this.context = context;
        this.handler = handler;
        if (usbDriver == null)
            usbDriver = new ArrayList<String>();
    }

    /**
     * 指定过滤
     *
     * @param decoder
     */
    public void setDecoder(MessageDecoder decoder) {
        this.decoder = decoder;
    }

    /**
     * 设置驱动与波特率并打开串口通讯
     *
     * @param devName
     * @param baudrate
     * @return
     */
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

            if (mSPOutput != null) {
                mSPOutput.close();
                mSPOutput = null;
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

    /**
     * 启用usb串口通讯
     *
     * @param baudrate
     * @return
     */
    @SuppressLint("NewApi")
    public boolean initUsbSerialPort(int baudrate) {
        UsbManager mUsbManager = (UsbManager) context
                .getSystemService(Context.USB_SERVICE);
        for (final UsbDevice device : mUsbManager.getDeviceList().values()) {
            final List<UsbSerialDriver> drivers = UsbSerialProber
                    .probeSingleDevice(mUsbManager, device);
            if (drivers.isEmpty()) {
            } else {
                // 不重复打开已开启的usb
                if (usbDriver.indexOf(device.getDeviceName()) != -1)
                    continue;
                for (UsbSerialDriver driver : drivers) {
                    try {
                        usbDriver.add(device.getDeviceName());
                        if (sDriver != null) {
                            try {
                                sDriver.close();
                            } catch (IOException e2) {
                            }
                            sDriver = null;
                        }
                        sDriver = driver;
                        // 打开usb
                        sDriver.open();
                        sDriver.setParameters(baudrate, 8,
                                UsbSerialDriver.STOPBITS_1,
                                UsbSerialDriver.PARITY_NONE);
                        if (mConnectedTask != null) {
                            mConnectedTask.shutdown();
                            mConnectedTask = null;
                        }
                        if (outQueue != null) {
                            outQueue.shutdown();
                            outQueue = null;
                        }
                        // 开启队列
                        outQueue = new DataQueue(context, handler);
                        outQueue.setDecoder(decoder);
                        outQueue.setRunnableTask(outQueue);
                        outQueue.enable();
                        // 开启通信
                        mConnectedTask = new ConnectedTask(context, handler);
                        mConnectedTask.setDataQueue(outQueue);
                        mConnectedTask.setConnectedDevice(sDriver);
                        return true;
                    } catch (Exception e) {
                        handler.messageEvent(context, Event.EXCEPTION,
                                "usb开启失败");
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 发送信息
     *
     * @param commend
     * @param data
     */
    public boolean sendCommend(int commend, byte[] data) {
        try {
            if (mSPOutput != null) {
                mSPOutput.write(data);
                return true;
            }
            if (sDriver != null) {
                int i = sDriver.write(data, commend);
                if (i > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (IOException e) {
            // 信息发送失败
            handler.messageEvent(context, Event.EXCEPTION, "发送信息失败");
            return false;
        }
        return false;
    }

    /**
     * 关闭通道
     */
    public void closeDevice() {
        try {
            if (mConnectedTask != null) {
                mConnectedTask.shutdown();
                mConnectedTask = null;
            }
            if (outQueue != null) {
                outQueue.shutdown();
                outQueue = null;
            }
            if (mSerialPort != null) {
                mSerialPort.close();
                mSerialPort = null;
            }
            if (mSPOutput != null) {
                mSPOutput.close();
                mSPOutput = null;
            }
            if (sDriver != null) {
                try {
                    usbDriver.clear();
                    sDriver.close();
                } catch (IOException e2) {
                }
                sDriver = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 設置休眠時間
     *
     * @param sleep
     */
    public void setSleep(final int sleep) {
        this.sleep = sleep;
    }
}
