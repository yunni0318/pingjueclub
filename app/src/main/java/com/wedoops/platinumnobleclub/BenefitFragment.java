package com.wedoops.platinumnobleclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;

public class BenefitFragment extends Fragment {

    private SubsamplingScaleImageView imageview_benefit;
    private static final String KEY_LANG = "key_lang";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.benefit_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageview_benefit = view.findViewById(R.id.imageview_benefit);

        String lang = new ApplicationClass().readFromSharedPreferences(getContext(), KEY_LANG);

        if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
            Glide.with(this)
                    .asBitmap()
                    .load("http://web.platinumnobleclub.com/Images/guide_en.jpg")
                    .placeholder(R.drawable.loading)
                    .timeout(10000)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageview_benefit.setImage(ImageSource.bitmap(resource));

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        }
        else{
            Glide.with(this)
                    .asBitmap()
                    .load("http://web.platinumnobleclub.com/Images/guide_cn.jpg")
                    .placeholder(R.drawable.loading)
                    .timeout(10000)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageview_benefit.setImage(ImageSource.bitmap(resource));

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }

}
