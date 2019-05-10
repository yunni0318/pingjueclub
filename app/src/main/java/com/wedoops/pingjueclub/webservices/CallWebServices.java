package com.wedoops.pingjueclub.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

public class CallWebServices extends AsyncTask<Bundle, Void, Bundle> {

    private int module;
    private Context context;
    private boolean showProgress;
    private ProgressDialog progress;


    public CallWebServices(int module, Context context, boolean showProgress) {
        this.context = context;
        this.module = module;
        this.showProgress = showProgress;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        if (showProgress) {
//            progress = new ProgressDialog(context);
//            progress.setTitle("Loading");
//            progress.setMessage("Wait while loading...");
//            progress.setCancelable(false);
//            progress.show();
//        }

    }

    @Override
    protected Bundle doInBackground(Bundle... bundles) {

        return Api_Constants.processRequest(bundles[0],context);
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        super.onPostExecute(bundle);

//        if (showProgress) {
//            if (progress != null && progress.isShowing()) {
//                try {
//                    progress.dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }

    }
}
