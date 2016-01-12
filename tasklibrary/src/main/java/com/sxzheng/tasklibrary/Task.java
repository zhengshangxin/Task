package com.sxzheng.tasklibrary;

/**
 * @Author zhengsx.
 */
public abstract class Task<T> implements Comparable<Task<T>>, Runnable {

    private Integer mSequence;

    private TaskManager mTaskManager;

    private Object mTag;
    private boolean mCancelled;

    @Override
    public int compareTo(Task<T> another) {
        Priority left = this.getPriority();
        Priority right = another.getPriority();

        // High-priority requests are "lesser" so they are sorted to the front.
        // Equal priorities are sorted by sequence number to provide FIFO ordering.
        return left == right?
                this.mSequence - another.mSequence
                : right.ordinal() - left.ordinal();
    }

    public Priority getPriority () {
        return Priority.NORMAL;
    }

    public boolean isCancelled() {
        return mCancelled;
    }

    public void cancel() {
        mCancelled = true;
    }

    @Override
    public final void run() {
        execute();
    }

    public abstract void execute();

    public void setSequence(int sequence) {
        mSequence = sequence;
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }
}
