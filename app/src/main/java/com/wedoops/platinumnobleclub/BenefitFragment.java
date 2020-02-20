package com.wedoops.platinumnobleclub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ortiz.touchview.TouchImageView;

public class BenefitFragment extends Fragment {

    private TouchImageView touchImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.benefit_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        touchImageView = view.findViewById(R.id.imageViewBenefit);

        Glide.with(this).load("http://web.platinumnobleclub.com/Images/guide.jpg").placeholder(R.drawable.empty_image).timeout(10000).into(touchImageView);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float scWidth = outMetrics.widthPixels;
        float height = outMetrics.heightPixels;
        touchImageView.getLayoutParams().width = (int) (scWidth * 2.2f);
        touchImageView.getLayoutParams().height = (int) (height * 2.2f);


    }
}
