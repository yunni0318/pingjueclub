package com.wedoops.pingjueclub;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressCustom;
import cc.cloudist.acplibrary.ACProgressFlower;
import pl.droidsonroids.gif.GifImageView;

public class CustomProgressDialog {
    //    private static ACProgressCustom progressFlower;
    private static Dialog dialog;


    public CustomProgressDialog() {

    }

    public void showDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.loading_layout);


        GifImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
//        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        gifImageView.setImageResource(R.drawable.pj_loading);

//        Glide.with(activity)
//                .asGif()
//                .load(R.raw.pj_loading).into(gifImageView);

        dialog.show();


    }

    public void hideDialog() {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }


    }

    public boolean isShowing() {

        if (dialog != null) {
            if (dialog.isShowing()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;

        }
    }


//    public static void showProgressDialog(Context context) {
//
//        progressFlower = new ACProgressCustom.Builder(context)
//                .useImages(R.drawable.pj_loading_1, R.drawable.pj_loading_2)
//                .speed(4)
//                .build();
////        progressFlower = new ACProgressFlower.Builder(context)
////                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
////                .themeColor(Color.WHITE)
////                .text(context.getResources().getString(R.string.loading_please_wait))
////                .petalThickness(5)
////                .textColor(Color.WHITE)
////                .textSize(30)
////                .fadeColor(Color.DKGRAY).build();
//
//        progressFlower.setCancelable(false);
//        progressFlower.show();
//    }
//
//    public static void closeProgressDialog() {
//        if (progressFlower != null) {
//            progressFlower.dismiss();
//        }
//    }

}
