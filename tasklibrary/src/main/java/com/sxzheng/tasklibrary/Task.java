package com.sxzheng.tasklibrary;

import android.support.annotation.NonNull;

/**
 * @author zhengshangxin.
 */
public abstract class Task<T> implements Comparable<Task<T>>, Runnable {

    private Object mTag;

    private Integer mSequence;
    private TaskManager mTaskManager;
    private volatile boolean mCancelled;

    private Priority mPriority = Priority.NORMAL;

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public boolean isCancelled() {
        return mCancelled;
    }

    public void cancel() {
        mCancelled = true;
    }

    @Override
    public final void run() {
        try {
            execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void execute();

    public void setSequence(int sequence) {
        mSequence = sequence;
    }

    public void setTaskManager(TaskManager manager) throws Exception {
        if (mTaskManager != null) {
            throw new IllegalAccessException("Task has been bound with a task " +
                    "manager!");
        }

        mTaskManager = manager;
    }

    @Override
    public int compareTo(@NonNull Task<T> another) {
        Priority left = this.getPriority();
        Priority right = another.getPriority();

        return left == right ? this.mSequence - another.mSequence :
                right.ordinal() - left.ordinal();
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    public Object getTag() {
        return mTag;
    }
}
