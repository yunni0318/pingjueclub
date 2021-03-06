package com.wedoops.platinumnobleclub.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.wedoops.platinumnobleclub.EventDetailActivity;
import com.wedoops.platinumnobleclub.LoginActivity;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.fragment.ReservationFragment;

import java.util.List;

public class DisplayAlertDialog {
    private static AlertDialog alert;

    public void displayAlertDialogError(final int errorCode, final Context context,  final Activity activity) {
        String errorMessage = getMessage(errorCode, context);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

        View customLayout = activity.getLayoutInflater().inflate(R.layout.dialog_ok_only_custom_layout, null);
        TextView textview_title = customLayout.findViewById(R.id.textview_title);
        TextView textview_message = customLayout.findViewById(R.id.textview_message);
        Button button_ok = customLayout.findViewById(R.id.button_ok);

        textview_title.setText(context.getString(R.string.error_title));
        textview_message.setText(errorMessage);
        button_ok.setText(context.getString(R.string.Ok));

        builder.setView(customLayout);

        builder.setCancelable(false);


        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                if (errorCode == 1506) {

                    //Logout and clear everything
                    new ApplicationClass(context).writeIntoSharedPreferences(context, CONSTANTS_VALUE.SHAREDPREFECENCE_MEMBER_LOGIN_USERNAME, "");
                    new ApplicationClass(context).writeIntoSharedPreferences(context, CONSTANTS_VALUE.SHAREDPREFECENCE_MEMBER_LOGIN_PASSWORD, "");

                    List<UserDetails> ud_all = UserDetails.listAll(UserDetails.class);
                    if (ud_all.size() > 0) {
                        UserDetails.deleteAll(UserDetails.class);
                    }


                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            }
        });

        alert = builder.create();

        alert.show();


//        AlertDialog.Builder builder = new AlertDialog.Builder(
//                context);
//        builder.setTitle(context.getString(R.string.error_title));
//        builder.setMessage(errorMessage);
//        builder.setCancelable(false);
//        builder.setPositiveButton(context.getString(R.string.Ok),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//
//                        dialog.dismiss();
//
//                        if (errorCode == 1506) {
//
//                            //Logout and clear everything
//                            new ApplicationClass(context).writeIntoSharedPreferences(context, CONSTANTS_VALUE.SHAREDPREFECENCE_MEMBER_LOGIN_USERNAME, "");
//                            new ApplicationClass(context).writeIntoSharedPreferences(context, CONSTANTS_VALUE.SHAREDPREFECENCE_MEMBER_LOGIN_PASSWORD, "");
//
//                            List<UserDetails> ud_all = UserDetails.listAll(UserDetails.class);
//                            if (ud_all.size() > 0) {
//                                UserDetails.deleteAll(UserDetails.class);
//                            }
//
//
//                            Intent intent = new Intent(context, LoginActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
//
//                        }
//
//                    }
//                });
//        builder.show();

    }

    public void displayAlertDialogSuccess(int statusCode, Context context, Activity activity) {
        String statusMessage = getMessage(statusCode, context);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

        View customLayout = activity.getLayoutInflater().inflate(R.layout.dialog_ok_only_custom_layout, null);
        TextView textview_title = customLayout.findViewById(R.id.textview_title);
        TextView textview_message = customLayout.findViewById(R.id.textview_message);
        Button button_ok = customLayout.findViewById(R.id.button_ok);

        textview_title.setText(context.getString(R.string.success_title));
        textview_message.setText(statusMessage);
        button_ok.setText(context.getString(R.string.Ok));

        builder.setView(customLayout);

        builder.setCancelable(false);


        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert = builder.create();

        alert.show();


//        AlertDialog.Builder builder = new AlertDialog.Builder(
//                context);
//        builder.setTitle(context.getString(R.string.success_title));
//        builder.setCancelable(false);
//        builder.setMessage(statusMessage);
//        builder.setPositiveButton(context.getString(R.string.Ok),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                        dialog.dismiss();
//                    }
//                });
//        builder.show();
    }

    public void displayAlertDialogString(final int errorCode, String errorMessage, boolean success, final Context context, final Activity activity) {

        String titleMessage = "";
        if (success) {
            titleMessage = context.getResources().getString(R.string.success_title);
        } else {
            titleMessage = context.getResources().getString(R.string.error_title);
        }

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

        View customLayout = activity.getLayoutInflater().inflate(R.layout.dialog_ok_only_custom_layout, null);
        TextView textview_title = customLayout.findViewById(R.id.textview_title);
        TextView textview_message = customLayout.findViewById(R.id.textview_message);
        Button button_ok = customLayout.findViewById(R.id.button_ok);

        textview_title.setText(titleMessage);
        textview_message.setText(errorMessage);
        button_ok.setText(context.getString(R.string.Ok));

        builder.setView(customLayout);

        builder.setCancelable(false);


        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                if (errorCode == 3001) {
                    ((EventDetailActivity) context).finish();
                } else {
                    alert.dismiss();

                }
            }
        });

        alert = builder.create();

        alert.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(
//                context);
//        builder.setTitle(titleMessage);
//        builder.setMessage(errorMessage);
//        builder.setCancelable(false);
//        builder.setPositiveButton(context.getString(R.string.Ok),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                        if (errorCode == 3001) {
//                            ((EventDetailActivity) context).finish();
//                        } else {
//                            dialog.dismiss();
//
//                        }
//                    }
//                });
//        builder.show();
    }

    public void displayImageSelectDialog(Context context, final IImagePickerLister imagePickerLister) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle(context.getString(R.string.change_profile_title));
        builder.setCancelable(true);
        builder.setItems(R.array.imagePicker, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)
                    imagePickerLister.onOptionSelected(ImagePickerEnum.FROM_GALLERY);
                else if (which == 1)
                    imagePickerLister.onOptionSelected(ImagePickerEnum.FROM_CAMERA);
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private String getMessage(int code, Context context) {

        String message = "";
        switch (code) {
            case 1502: {
                message = context.getString(R.string.error_1502);
                break;
            }
            case 1503: {
                message = context.getString(R.string.error_1503);
                break;

            }
            case 1504: {
                message = context.getString(R.string.error_1504);
                break;

            }
            case 1506: {
                message = context.getString(R.string.error_1506);
                break;

            }
            case 1509: {
                message = context.getString(R.string.error_1509);
                break;

            }
            case 1510: {
                message = context.getString(R.string.error_1510);
                break;

            }
            case 1511: {
                message = context.getString(R.string.error_1511);
                break;

            }
            case 1512: {
                message = context.getString(R.string.error_1512);
                break;

            }
            case 503: {
                message = "Service Unavailable, Please Try Again Later";
                break;
            }
            case 2501: {
                message = context.getString(R.string.error_2501);
                break;
            }
            case 2502: {
                message = context.getString(R.string.error_2502);
                break;
            }
            case 2503: {
                message = context.getString(R.string.error_2503);
                break;
            }
            case 2504: {
                message = context.getString(R.string.error_2504);
                break;
            }
            case 2505: {
                message = context.getString(R.string.error_2505);
                break;
            }
            case 2506: {
                message = context.getString(R.string.error_2506);
                break;
            }
            case 2507: {
                message = context.getString(R.string.error_2507);
                break;
            }
            case 2508: {
                message = context.getString(R.string.error_2508);
                break;
            }
            case 2509: {
                message = context.getString(R.string.error_2509);
                break;
            }
            case 2510: {
                message = context.getString(R.string.error_2510);
                break;
            }
            case 2511: {
                message = context.getString(R.string.error_2511);
                break;
            }
            case 2512: {
                message = context.getString(R.string.error_2512);
                break;
            }
            case 2513: {
                message = context.getString(R.string.error_2513);
                break;
            }
            case 2514: {
                message = context.getString(R.string.error_2514);
                break;
            }
            case 2515: {
                message = context.getString(R.string.error_2515);
                break;
            }
            case 2516: {
                message = context.getString(R.string.error_2516);
                break;
            }
            case 2517: {
                message = context.getString(R.string.error_2517);
                break;
            }
            case 2518: {
                message = context.getString(R.string.error_2518);
                break;
            }
            case 2519: {
                message = context.getString(R.string.error_2519);
                break;
            }
            case 2520: {
                message = context.getString(R.string.error_2520);
                break;
            }
            case 2521: {
                message = context.getString(R.string.error_2521);
                break;
            }
            case 2522: {
                message = context.getString(R.string.error_2522);
                break;
            }
            case 3501: {
                message = context.getString(R.string.error_3501);
                break;
            }
            case 3502: {
                message = context.getString(R.string.error_3502);
                break;
            }
            case 3503: {
                message = context.getString(R.string.error_3503);
                break;
            }
            case 3504: {
                message = context.getString(R.string.error_3504);
                break;
            }
            case 3505: {
                message = context.getString(R.string.error_3505);
                break;
            }
            case 200: {
                message = context.getString(R.string.success_title);
                break;
            }

            default: {
                message = context.getString(R.string.error_unknown);
                break;
            }
        }
        return message;
    }
}
