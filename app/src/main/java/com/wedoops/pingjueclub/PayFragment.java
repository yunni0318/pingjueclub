package com.wedoops.pingjueclub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wedoops.pingjueclub.helper.DisplayAlertDialog;

public class PayFragment extends Fragment {

    private LinearLayout payConfirmation, paySuccess;
    private CardView cancel, confirm;
    private String transaction_id, remarks, currency, pay_amount;
    private TextView payTitle, payPointFront, payPointEnd, payCurrency, payAmount, paySuccessMessage, paySuccessID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            transaction_id = bundle.getString("TRANSACTION_ID", "");
            remarks = "GYMS Member";
            currency = "EUR";
            pay_amount = "1000";

        } else {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).loadHomeFragment();
            }

            new DisplayAlertDialog().displayAlertDialogString(0, "Cannot get data from QR Code, please try again!", false, getContext());

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.pay_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payConfirmation = view.findViewById(R.id.payConfirmation);
        cancel = view.findViewById(R.id.payCancelBtn);
        confirm = view.findViewById(R.id.payConfirmBtn);
        paySuccess = view.findViewById(R.id.paySuccess);
        payTitle = view.findViewById(R.id.payTitle);
        payPointFront = view.findViewById(R.id.payPointFront);
        payPointEnd = view.findViewById(R.id.payPointEnd);
        payCurrency = view.findViewById(R.id.payCurrency);
        payAmount = view.findViewById(R.id.payAmount);
        paySuccessMessage = view.findViewById(R.id.paySuccessMessage);
        paySuccessID = view.findViewById(R.id.paySuccessID);

        payTitle.setText(String.format(getResources().getString(R.string.pay_confirmation_msg), remarks));
        payPointFront.setText(pay_amount);
        payAmount.setText(String.valueOf(pay_amount));
        paySuccessMessage.setText(String.format(getResources().getString(R.string.pay_success_msg), pay_amount, remarks));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payConfirmation.setVisibility(View.GONE);
                paySuccess.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).loadHomeFragment();
                }
            }
        });
    }
}
