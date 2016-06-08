package com.sxzheng.tasklibrary;

import java.util.concurrent.Executor;

/**
 * @author zheng.
 */
public class LinearExecutor implements Executor {

    private static final boolean DEBUG = true;

    private Runnable mRunnable;
    private Thread   mThread;
    private int      mThreadId;

    @Override
    public synchronized void execute(Runnable command) {

        if (command == null) {
            if (DEBUG) throw new NullPointerException();
            return;
        }

        this.mRunnable = command;

        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Runnable r;
                        synchronized (LinearExecutor.this) {
                            if (LinearExecutor.this.mRunnable == null) {
                                mThread = null;
                                return;
                            } else {
                                r = LinearExecutor.this.mRunnable;
                                LinearExecutor.this.mRunnable = null;
                            }
                        }
                        r.run();
                    }
                }
            });
        }

        mThread.setDaemon(true);
        mThread.setName(getClass().getName() + ++mThreadId);
        mThread.start();
    }
}
