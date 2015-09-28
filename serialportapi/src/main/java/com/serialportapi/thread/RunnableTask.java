package com.serialportapi.thread;

import android.content.Context;
import android.util.Log;

import com.serialportapi.SerialCommunication;
import com.serialportapi.mean.MessageHandler;


/**
 * 任务核心
 *
 * @author talkliu
 */
public class RunnableTask implements Runnable {
    private RunnableTaskIf task;
    private MessageHandler handler;
    private Context context;
    public Event event;// 运行状态
    public Thread thread;

    public RunnableTask(Context context, MessageHandler handler) {
        this.handler = handler;
        this.context = context;
    }

    /**
     * 加入任务
     *
     * @param task
     */
    public void setRunnableTask(RunnableTaskIf task) {
        this.task = task;
    }

    @Override
    public void run() {
        if (threadPreparing()) {
            try {
                switch (event) {
                    case PREPARING:
                        event = Event.RUNNING;
                        break;
                }
                do {
                    task.taskRunning();
                    Thread.sleep(task.getSleep());
                } while (event == Event.RUNNING);
            } catch (Exception e) {
                handler.messageEvent(context, event, task.getClass().toString());
            }
            // 任务停止
            handler.messageEvent(context, event = Event.STOPPED, "任务停止");
        }
    }

    /**
     * 任务状态
     *
     * @return
     */
    public boolean threadPreparing() {
        if (task != null) {
            try {
                return task.taskPreparing();
            } catch (Exception e) {
                handler.messageEvent(context, Event.EXCEPTION, "任务异常1");
            }
        } else {
            handler.messageEvent(context, Event.EXCEPTION, "任务异常2");
        }
        return false;
    }

    /**
     * 开始任务
     */
    public void startup() {
        thread = new Thread(this);
        thread.setPriority(10);
        thread.start();
    }

    /**
     * 停止任务
     */
    public void shutdown() {
        event = Event.STOPPING;
        handler.messageEvent(context, event, "停止任务中");
    }
}
