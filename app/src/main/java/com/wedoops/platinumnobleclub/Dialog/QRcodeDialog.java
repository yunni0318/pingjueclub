package com.wedoops.platinumnobleclub.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.MainActivity;
import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.CountryList;
import com.wedoops.platinumnobleclub.database.MemberIDEncryted;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.helper.CONSTANTS_VALUE;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QRcodeDialog extends Dialog implements View.OnClickListener {

    public Context c;
    public ImageView qrcode, btn_close, userImage;
    private TextView memberName, memberID, member_num;

    public QRcodeDialog(@NonNull Context context) {
        super(context);
        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qrcode_dialog);
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.99);


        getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        qrcode = findViewById(R.id.QRCode_image);
        btn_close = findViewById(R.id.btn_close);
        userImage = findViewById(R.id.qr_user_profile);
        memberName = findViewById(R.id.member_name);
//        memberID = findViewById(R.id.member_ID);
        member_num = findViewById(R.id.member_ID_number);
        btn_close.setOnClickListener(this);
        displayResult();
        genQRcode();
    }

    private void displayResult() {

        List<UserDetails> ud_list = UserDetails.listAll(UserDetails.class);

//        Glide.with(view.getContext()).load(ud_list.get(0).getProfilePictureImagePath()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).timeout(10000).placeholder(R.drawable.default_profile).into(imageview_user_profile);
        Glide.with(c)
                .asBitmap()
                .load(ud_list.get(0).getProfilePictureImagePath())
                .timeout(10000)
                .placeholder(R.drawable.default_profile)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        userImage.setImageBitmap(resource);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        memberName.setText(ud_list.get(0).getNickName());
//        memberID.setText("ID: "+ud_list.get(0).getLoginID());
        String text = ud_list.get(0).getCardID();
        text = text.substring(0, 4) + "-" +text.substring(4, 8) + "-" + text.substring(8, text.length());

        member_num.setText(text);

    }

    private void genQRcode() {

        List<MemberIDEncryted> memID = MemberIDEncryted.listAll(MemberIDEncryted.class);
        String s = memID.get(0).getEncryptedString();

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<EncodeHintType, Object>();
            hintMap.put(EncodeHintType.MARGIN, new Integer(1));
            BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.QR_CODE, 800, 800, hintMap);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public QRcodeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected QRcodeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
               MainActivity.redirectDashboard();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();

    }
}
