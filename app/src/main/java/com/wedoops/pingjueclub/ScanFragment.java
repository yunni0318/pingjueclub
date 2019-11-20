package com.wedoops.pingjueclub;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orm.StringUtil;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.wedoops.pingjueclub.database.CurrencyList;
import com.wedoops.pingjueclub.database.UserDetails;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    private TextView amount;
    private View view;
//    private PowerMenu pm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.scan_fragment, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        setupDeclaration();
        setupCustomFont();
//        setupPowerMenu();

        List<CurrencyList> cl = CurrencyList.listAll(CurrencyList.class);
        changeCurrencyRate(cl.get(0).getCurrencyName());


        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        } else {
                            if (getActivity() != null) {
                                ((MainActivity) getActivity()).loadHomeFragment();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }


    private void setupDeclaration() {
        zXingScannerView = view.findViewById(R.id.zxscan);
        amount = view.findViewById(R.id.amount);

    }

    private void setupCustomFont() {


        Typeface typeface2 = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/NUNITOSANS-SEMIBOLD.TTF");
        amount.setTypeface(typeface2);

    }

//    private void setupPowerMenu() {
//        pm = new PowerMenu.Builder(view.getContext()).build();
//        pm.setAnimation(MenuAnimation.SHOWUP_TOP_LEFT);
//        pm.setMenuRadius(10f); // sets the corner radius.
//        pm.setMenuShadow(10f); // sets the shadow.
//        pm.setMenuColor(Color.parseColor("#000000"));
//        pm.setTextColor(Color.parseColor("#ffffff"));
//        pm.setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
//            @Override
//            public void onItemClick(int position, PowerMenuItem item) {
//                changeCurrencyRate(item.getTitle());
//                zXingScannerView.stopCamera();
//                startCamera();
//                pm.dismiss();
//            }
//        });
//
//        final List<CurrencyList> cl = CurrencyList.listAll(CurrencyList.class);
//
//        if (cl.size() > 0) {
//            for (int i = 0; i < cl.size(); i++) {
//
//                final int ii = i;
//                pm.addItem(new PowerMenuItem(cl.get(ii).getCurrencyName()));
//
//            }
//        }
//
//    }

    private void changeCurrencyRate(String currencyName) {

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        BigDecimal cash_wallet_decimal = new BigDecimal(ud.get(0).getCashWallet());
        cash_wallet_decimal = cash_wallet_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);


        NumberFormat formatter = new DecimalFormat("#,###,###.00");
        String cash_wallet_string = formatter.format(cash_wallet_decimal.doubleValue());

        amount.setText(cash_wallet_string);

    }


    @Override
    public void handleResult(Result rawResult) {
        if (getActivity() != null) {

            if (rawResult.getBarcodeFormat().equals(BarcodeFormat.QR_CODE)) {
                if (rawResult.getText().length() > 0) {
                    String[] split_scanned_value = rawResult.getText().split("\\|");

                    if (split_scanned_value.length == 7) {
                        ((MainActivity) getActivity()).payScreen(rawResult.getText());

                    } else {
                        Toast.makeText(view.getContext(), "Invalid QR Code, Please Try Again", Toast.LENGTH_LONG).show();
                        zXingScannerView.stopCamera();
                        startCamera();
                    }

//                    ((MainActivity) getActivity()).payScreen("22764751");

                }
            }

        }
    }

    private void startCamera() {
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    public void onResume() {
        super.onResume();
        startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).loadHomeFragment();
                }
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).loadHomeFragment();
                }
            } else {
                startCamera();
            }
        }
    }
}
