package com.serialportapi.driver;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.serialportapi.SerialCommunication;
import com.serialportapi.common.ConnectedTask;
import com.serialportapi.common.DataQueue;
import com.serialportapi.mean.MessageDecoder;
import com.serialportapi.mean.MessageHandler;
import com.serialportapi.thread.Event;
import com.serialportapi.usbdriver_api.UsbSerialDriver;
import com.serialportapi.usbdriver_api.UsbSerialProber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by young on 15-9-23.
 * 基于android系统提供的usb串口进行通讯，采用了开源的usb支持
 * 不需要root
 */
public class USBSystm extends SerialCommunication {
    /**
     * 列表
     */
    private Map<String, UsbSerialDriver> drivermap;
    /**
     * usb通讯
     */
    private UsbSerialDriver sDriver = null;

    /**
     * 构造方法，接收处理的handler
     *
     * @param context
     */
    public USBSystm(Context context, MessageHandler handler) {
        super(context, handler);
        UsbManager mUsbManager = (UsbManager) context
                .getSystemService(Context.USB_SERVICE);
        drivermap = new LinkedHashMap<String, UsbSerialDriver>();
        for (final UsbDevice device : mUsbManager.getDeviceList().values()) {
            final List<UsbSerialDriver> drivers = UsbSerialProber
                    .probeSingleDevice(mUsbManager, device);
            if (!drivers.isEmpty()) {
                //排除为空的usb设备
                for (UsbSerialDriver driver : drivers) {
                    //识别设备
                    drivermap.put(device.getDeviceName(), driver);
                }
            }
        }
    }

    @Override
    public void setDecoder(MessageDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public List<String> getDriverList(int driver) {
        List<String> list = new ArrayList<String>();
        list.addAll(drivermap.keySet());
        return list;
    }

    /**
     * 可以链接指定的设备,devName为null时连接第一个usb设备
     *
     * @param devName  可以通过驱动器列表获取，也可以直接声明
     * @param baudrate 波特率
     * @return
     */
    @Override
    public boolean initSerialPort(String devName, int baudrate) {
        try {
            if (devName == null || devName.equals("")) {
                sDriver = drivermap.values().iterator().next();
            } else {
                sDriver = drivermap.get(devName);
            }
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
            handler.messageEvent(context, Event.EXCEPTION, "开启通道失败");
            return false;
        }
    }

    @Override
    public boolean sendCommend(int commend, byte[] data) {
        try {
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
            if (sDriver != null) {
                try {
                    sDriver.close();
                } catch (IOException e2) {
                }
                sDriver = null;
            }
            return true;
        } catch (Exception e) {
            handler.messageEvent(context, Event.EXCEPTION, "关闭端口失败");
            return false;
        }
    }
}
