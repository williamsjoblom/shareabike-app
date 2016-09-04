package com.shareabike.shareabike.API;

import com.shareabike.shareabike.Bike;
import com.shareabike.shareabike.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wax on 9/4/16.
 */
public abstract class GetBorrowedTask extends NiceAsyncTask<Void, Integer> {

    private final int userID;

    public GetBorrowedTask(int userID) {
        this.userID = userID;
    }

    @Override
    protected Integer doInBackground(Object... params) {
        String data = API.read("bikeBorrowing/" + userID);

        if(data.equals("false"))
            return -1;
        else
            return Integer.valueOf(data);
    }

    @Override
    protected abstract void onPostExecute(Integer result);
}
