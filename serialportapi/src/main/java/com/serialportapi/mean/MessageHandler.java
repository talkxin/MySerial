package com.serialportapi.mean;


import android.content.Context;

/**
 * 获取byte处理的handler
 *
 * @author talkliu
 */
public interface MessageHandler {
    /**
     * 返回信息的处理
     *
     * @param message
     */
    public void messageReceived(Context context, Object message);

    /**
     * 处理事件的信息
     *
     * @param event
     */
    public void messageEvent(Context context, Event event, String action);
}
