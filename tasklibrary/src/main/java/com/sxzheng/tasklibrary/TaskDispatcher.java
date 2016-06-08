package com.sxzheng.tasklibrary;

import java.util.concurrent.BlockingQueue;

import android.os.Process;
import android.util.Log;

/**
 * @author zheng.
 */
public class TaskDispatcher extends Thread {

    private final BlockingQueue<Task<?>> mQueue;

    private volatile boolean mQuit = false;

    private int i;

    public TaskDispatcher(BlockingQueue<Task<?>> queue) {
        mQueue = queue;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        while (true) {
            Log.e(this.getClass().getName(), "Thread: " +
                    getName() + " is running!" + "" + (i++));
            Task task;
            try {
                task = mQueue.take();
            } catch (InterruptedException e) {
                if (mQuit) {
                    return;
                }
                continue;
            }

            if (task.isCancelled()) {
                continue;
            }

            task.run();
        }
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }
}
