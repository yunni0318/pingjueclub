package com.wedoops.pingjueclub.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

public class CallRefreshToken extends AsyncTask<Bundle, Void, Bundle> {

    private int module;
    private int origin;
    private Context context;
    private ProgressDialog progress;


    public CallRefreshToken(int module, Context context, int origin) {
        this.context = context;
        this.module = module;
        this.origin = origin;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bundle doInBackground(Bundle... bundles) {

        return RefreshTokenAPI.processRequest(bundles[0], context, origin);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        super.onPostExecute(bundle);
    }
}
