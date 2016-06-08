package com.sxzheng.tasklibrary;

import android.util.Log;

/**
 * @author zheng.
 */
public class TaskTimerDispatcher<T> extends Thread {

    private static final boolean DEBUG = true;

    // 1000ms
    private          long    mIntervalMills = 1000;
    private volatile boolean isStop         = false;

    private TimerMessageStack<T> mMessageStack;

    public TaskTimerDispatcher() {
        mMessageStack = new TimerMessageStack<>();
    }

    public boolean addMessage(TimerStackTask<T> message) {

        return !(mMessageStack.push(message) == null);
    }

    public synchronized boolean isStop() {
        return isStop;
    }

    public synchronized long getIntervalMills() {
        return mIntervalMills;
    }

    public synchronized void setIntervalMills(long intervalMills) {
        mIntervalMills = intervalMills;
    }

    @Override
    public void run() {
        super.run();

        while (!isStop()) {
            try {

                Thread.sleep(getIntervalMills());

                if (DEBUG) Log.e(getName(), "Sleep!");
            } catch (InterruptedException e) {
                if (DEBUG) e.printStackTrace();
                return;
            }

            try {

                if (mMessageStack.isEmpty()) {
                    continue;
                }

                TimerStackTask<T> message = mMessageStack.pop();
                if (message != null) {

                    if (DEBUG) Log.e(getName(), "Stack clear!");
                    mMessageStack.clear();

                    if (DEBUG) Log.e(getName(), "Stack clear successfully!");
                }
                message.run();

                if (DEBUG) Log.e(getName(), "Task in the stack top execute successfully!");
            } catch (Exception e) {

                e.printStackTrace();
                if (DEBUG) throw e;
            }
        }
    }

    public synchronized void quit() {
        isStop = true;
        interrupt();
    }
}
