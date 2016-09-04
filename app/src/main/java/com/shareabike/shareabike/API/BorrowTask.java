package com.shareabike.shareabike.API;

/**
 * Created by wax on 9/4/16.
 */
public class BorrowTask extends NiceAsyncTask<Void, Void> {

    private final int userID;
    private final int bikeID;
    private final boolean borrow;

    public BorrowTask(int userID, int bikeID, boolean borrow) {
        this.userID = userID;
        this.bikeID = bikeID;
        this.borrow = borrow;
    }

    @Override
    protected Void doInBackground(Object... params) {
        String op;

        if(borrow) op = "/rent/";
        else op = "/return/";

        API.read("bike/" + bikeID + op + userID);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) { }
}
