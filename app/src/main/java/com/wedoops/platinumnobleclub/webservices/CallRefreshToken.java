package com.wedoops.platinumnobleclub.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

public class CallRefreshToken extends AsyncTask<Bundle, Void, Bundle> {

    private int module;
    private int origin;
    private Context context;
    private Activity activity;
    private ProgressDialog progress;


    public CallRefreshToken(int module, Context context, Activity activity, int origin) {
        this.context = context;
        this.module = module;
        this.origin = origin;
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bundle doInBackground(Bundle... bundles) {

        return RefreshTokenAPI.processRequest(bundles[0], context, origin,activity);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        super.onPostExecute(bundle);
    }
}
