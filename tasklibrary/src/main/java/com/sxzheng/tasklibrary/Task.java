package com.sxzheng.tasklibrary;

/**
 * @Author zhengsx.
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
            throw new IllegalAccessException("Task has bindded with a task manager!");
        }

        mTaskManager = manager;
    }

    @Override
    public int compareTo(Task<T> another) {
        Priority left = this.getPriority();
        Priority right = another.getPriority();

        // High-priority requests are "lesser" so they are sorted to the front.
        // Equal priorities are sorted by sequence number to provide FIFO ordering.
        return left == right ? this.mSequence - another.mSequence :
                right.ordinal() - left.ordinal();
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }
}
