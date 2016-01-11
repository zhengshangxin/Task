package com.sxzheng.tasklibrary;

import android.os.Process;

import java.util.concurrent.BlockingQueue;

/**
 * Author zhengsx.
 */
public class TaskDispatcher extends Thread {

    private final BlockingQueue<Task<?>> mQueue;

    private volatile boolean mQuit = false;

    public TaskDispatcher(BlockingQueue<Task<?>> queue) {
        mQueue = queue;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        while (true) {

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
