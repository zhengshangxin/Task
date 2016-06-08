package com.sxzheng.tasklibrary;

import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zheng.
 */
public class TimerMessageStack<T> implements MessageStack<TimerStackTask<T>> {

    private Stack<TimerStackTask<T>> mTStack;
    private ReentrantLock            mLock;

    private int mSerialNumber;

    public TimerMessageStack() {
        mTStack = new Stack<>();
        mLock = new ReentrantLock();
    }

    @Override
    public boolean isEmpty() {
        return mTStack.isEmpty();
    }

    @Override
    public void clear() {
        try {
            mLock.lockInterruptibly();
            mTStack.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
    }

    @Override
    public TimerStackTask<T> peek() {
        return mTStack.peek();
    }

    @Override
    public TimerStackTask<T> pop() {
        return mTStack.pop();
    }

    @Override
    public TimerStackTask<T> push(TimerStackTask<T> tTimerStackTask) {
        TimerStackTask<T> stackTask = null;
        try {
            mLock.lockInterruptibly();
            stackTask = mTStack.push(tTimerStackTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }

        return stackTask;
    }

    @Override
    public TimerStackTask<T> findElementBy() {
        return null;
    }

    public int getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        mSerialNumber = serialNumber;
    }
}
