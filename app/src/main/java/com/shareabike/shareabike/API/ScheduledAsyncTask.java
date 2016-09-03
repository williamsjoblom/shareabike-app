package com.shareabike.shareabike.API;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wax on 9/3/16.
 */
public abstract class ScheduledAsyncTask<T0, T1, T2> extends AsyncTask<T0, T1, T2> {

    public void schedule(int interval) {
        final Handler handler = new Handler();
        Timer timer = new Timer();

        final AsyncTask task = this;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            task.execute();
                        } catch (Exception e) { }
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, interval);
    }

    @Override
    abstract protected void onPostExecute(T2 t2);
}
