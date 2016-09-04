package com.shareabike.shareabike.API;

import android.os.AsyncTask;

/**
 * Created by wax on 9/3/16.
 */
public abstract class NiceAsyncTask<T1, T2> extends AsyncTask<Object, T1, T2> {

    @Override
    abstract protected void onPostExecute(T2 t2);
}
