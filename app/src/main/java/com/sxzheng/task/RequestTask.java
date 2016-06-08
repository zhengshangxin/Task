package com.sxzheng.task;

import android.util.Log;

import com.sxzheng.tasklibrary.Task;

public class RequestTask extends Task<String> {

    @Override
    public void execute() {

        try {
            Log.e(this.getClass().getName(), "-- start run");
            Thread.sleep(1000);
            Log.e(this.getClass().getName(), "-- sleep 1000 ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
