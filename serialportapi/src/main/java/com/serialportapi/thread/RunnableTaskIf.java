package com.serialportapi.thread;

/**
 * 线程接口
 *
 * @author talkliu
 */
public interface RunnableTaskIf {
    /**
     * 任务准备
     */
    public boolean taskPreparing();

    /**
     * 任务开始
     */
    public void taskRunning();

    /**
     * 任务完成
     */
    public void taskCompletion();

    /**
     * 設置休眠時間
     */
    public void setSleep(int sleep);

    /**
     * 获取休眠时间
     *
     * @return
     */
    public int getSleep();
}
