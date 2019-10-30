package com.wedoops.pingjueclub;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wedoops.pingjueclub.database.CurrencyList;
import com.wedoops.pingjueclub.database.UserDetails;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    private TextView amount, textview_currency_rate;
    private View view;

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

        zXingScannerView.setSquareViewFinder(true);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        BigDecimal cash_wallet_decimal = new BigDecimal(ud.get(0).getCashWallet());
        cash_wallet_decimal = cash_wallet_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);

        List<CurrencyList> cl = CurrencyList.listAll(CurrencyList.class);
        BigDecimal currency_rate = new BigDecimal(cl.get(0).getCurrencyRate());
        currency_rate = cash_wallet_decimal.multiply(currency_rate);
        currency_rate.setScale(2, BigDecimal.ROUND_HALF_UP);


        amount.setText(cash_wallet_decimal.toString());
        textview_currency_rate.setText(String.format("= %s %s", cl.get(0).getCurrencyCode(), currency_rate.toString()));

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
        textview_currency_rate = view.findViewById(R.id.textview_currency_rate);
    }


    @Override
    public void handleResult(Result rawResult) {
        if (getActivity() != null) {

            if (rawResult.getBarcodeFormat().equals(BarcodeFormat.QR_CODE)) {
                if (rawResult.getText().length() > 0) {

//                    ((MainActivity) getActivity()).payScreen(rawResult.getText());
                    ((MainActivity) getActivity()).payScreen("22764751");

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
