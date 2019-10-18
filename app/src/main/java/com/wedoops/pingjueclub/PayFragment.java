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

public class PayFragment extends Fragment {

    private LinearLayout payConfirmation,paySuccess;
    private CardView cancel,confirm;
    private String title="";
    private int point=0;
    private int currency=0;
    private TextView payTitle,payPointFront,payPointEnd,payCurrency,payAmount,paySuccessMessage,paySuccessID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            title=bundle.getString("title","");
            point=bundle.getInt("point",0);
            currency=bundle.getInt("currency",0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.pay_fragment,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payConfirmation=(LinearLayout)view.findViewById(R.id.payConfirmation);
        cancel=(CardView)view.findViewById(R.id.payCancelBtn);
        confirm=(CardView)view.findViewById(R.id.payConfirmBtn);
        paySuccess=(LinearLayout)view.findViewById(R.id.paySuccess);
        payTitle=(TextView)view.findViewById(R.id.payTitle);
        payPointFront=(TextView)view.findViewById(R.id.payPointFront);
        payPointEnd=(TextView)view.findViewById(R.id.payPointEnd);
        payCurrency=(TextView)view.findViewById(R.id.payCurrency);
        payAmount=(TextView)view.findViewById(R.id.payAmount);
        paySuccessMessage=(TextView)view.findViewById(R.id.paySuccessMessage);
        paySuccessID=(TextView)view.findViewById(R.id.paySuccessID);

        payTitle.setText(String.format(getResources().getString(R.string.pay_confirmation_msg), title));
        payPointFront.setText(String.valueOf(point));
        payAmount.setText(String.valueOf(point));
        paySuccessMessage.setText(String.format(getResources().getString(R.string.pay_success_msg), String.valueOf(point),title));

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
