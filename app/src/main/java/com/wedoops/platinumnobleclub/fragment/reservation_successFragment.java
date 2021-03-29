package com.wedoops.platinumnobleclub.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wedoops.platinumnobleclub.R;

public class reservation_successFragment extends Fragment {


    private View view;
    private TextView gotobookinghistory;
    public reservation_successFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservation_success, container, false);



        gotobookinghistory = view.findViewById(R.id.gotobookinghistory);
        SpannableString content = new SpannableString("Check Booking History >>");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        gotobookinghistory.setText(content);

        gotobookinghistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MyReservationFragment();
                Bundle args = new Bundle();
                args.putString("YourKey", "YourValue");
                fragment.setArguments(args);

                FragmentTransaction fragmentTransaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout_fragment_container, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        return view;

    }
}