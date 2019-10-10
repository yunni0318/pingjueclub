package com.wedoops.pingjueclub;

import android.content.Context;
import android.graphics.Color;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class CustomProgressDialog {
    private static ACProgressFlower progressFlower;

    public CustomProgressDialog() {
    }

    public static void showProgressDialog(Context context) {
        progressFlower = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(context.getResources().getString(R.string.loading_please_wait))
                .petalThickness(5)
                .textColor(Color.WHITE)
                .textSize(30)
                .fadeColor(Color.DKGRAY).build();

        progressFlower.setCancelable(false);
        progressFlower.show();
    }

    public static void closeProgressDialog() {
        if(progressFlower!=null){
            progressFlower.dismiss();
        }
    }

}
