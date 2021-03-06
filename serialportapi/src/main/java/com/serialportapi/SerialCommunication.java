package com.serialportapi;

import android.content.Context;

import com.serialportapi.common.ConnectedTask;
import com.serialportapi.common.DataQueue;
import com.serialportapi.mean.MessageDecoder;
import com.serialportapi.mean.MessageHandler;

import java.util.List;

/**
 * 串口接口类，接收usb请求与232串口请求
 * 可以根据串口借口类进行自定义数据接收
 */
public abstract class SerialCommunication {
    /**
     * 过滤器
     */
    protected MessageDecoder decoder = null;

    /**
     * usb驱动器标识
     */
    public final static int USBDRIVER = 1;
    /**
     * 232串口驱动器标识
     */
    public final static int SERIALDRIVER = 0;
    /**
     * Context
     */
    protected Context context;
    /**
     * 处理的handler，接收过滤后的数据
     */
    protected MessageHandler handler;

    /**
     * 通信类
     */
    protected ConnectedTask mConnectedTask;
    /**
     * 数据队列
     */
    protected DataQueue outQueue;

    /**
     * 构造方法，接收处理的handler
     *
     * @param context
     */
    protected SerialCommunication(Context context, MessageHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    /**
     * 設置自定义过滤器
     *
     * @param decoder
     */
    public abstract void setDecoder(MessageDecoder decoder);

    /**
     * 设置默认过滤器
     *
     * @param md
     */
    public void setDecoder(int md) {

    }

    /**
     * 获取驱动器列表
     *
     * @param driver 使用驱动器标识
     * @return
     */
    public abstract List<String> getDriverList(int driver);

    /**
     * 开始串口通讯
     *
     * @param devName  可以通过驱动器列表获取，也可以直接声明
     * @param baudrate 波特率
     * @return
     */
    public abstract boolean initSerialPort(String devName, int baudrate);

    /**
     * 发送信息
     *
     * @param commend 延迟时间
     * @param data    数据
     */
    public abstract boolean sendCommend(int commend, byte[] data);

    /**
     * 关闭通道
     *
     * @return
     */
    public abstract boolean closeDevice();

    /**
     * 设置休眠时间
     *
     * @param sleep
     * @return
     */
    public boolean setSleep(int sleep) {
        if (mConnectedTask != null) {
            mConnectedTask.setSleep(sleep);
            return true;
        } else {
            return false;
        }
    }

}
