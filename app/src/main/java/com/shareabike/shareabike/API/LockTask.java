package com.shareabike.shareabike.API;

/**
 * Created by wax on 9/4/16.
 */
public class LockTask extends NiceAsyncTask<Void, Void> {

    final private int id;
    final private boolean lock;

    public LockTask(int id, boolean lock) {
        this.id = id;
        this.lock = lock;
    }

    @Override
    protected Void doInBackground(Object... params) {
        if(lock)
            API.read("bike/" + id + "/lock");
        else
            API.read("bike/" + id + "/unlock");

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) { }
}
