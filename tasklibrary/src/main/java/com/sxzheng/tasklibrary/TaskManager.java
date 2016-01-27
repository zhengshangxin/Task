package com.sxzheng.tasklibrary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhengshangxin
 *
 * Each task manager is correspondence with a task queue that is independent with
 * others.
 */
public class TaskManager {

    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    private final Map<String, Queue<Task<?>>> mWaitingTasks
            = new HashMap<>();

    private final Set<Task<?>> mCurrentTasks = new HashSet<>();

    private final PriorityBlockingQueue<Task<?>> mQueue
            = new PriorityBlockingQueue<>();

    private static final int DEFAULT_THREAD_POOL_SIZE = 4;

    private TaskDispatcher[] mTaskDispatchers;

    public TaskManager() {
        mTaskDispatchers = new TaskDispatcher[DEFAULT_THREAD_POOL_SIZE];
    }

    public TaskManager(int threadPoolSize) {

        mTaskDispatchers = new TaskDispatcher[threadPoolSize];
    }

    public void offer(Task<?> task) {
        if (task != null) {
            try {
                task.setTaskManager(this);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            task.setSequence(getSequenceNumber());
            mQueue.offer(task);
        }
    }

    public void start() {
        stop();
        for (int i = 0; i < mTaskDispatchers.length; i++) {
            TaskDispatcher taskDispatcher = new TaskDispatcher(mQueue);
            taskDispatcher.setName("TaskDispatcher " + i);
            mTaskDispatchers[i] = taskDispatcher;
            mTaskDispatchers[i].start();
        }
    }

    private void stop() {
        for (TaskDispatcher mTaskDispatcher : mTaskDispatchers) {
            if (mTaskDispatcher != null) mTaskDispatcher.quit();
        }
    }

    public int getSequenceNumber(){
        return mSequenceGenerator.incrementAndGet();
    }

    public void cancelAll() {
        synchronized (mCurrentTasks) {
            for (Task task : mCurrentTasks) {
                task.cancel();
            }
        }
    }
}
