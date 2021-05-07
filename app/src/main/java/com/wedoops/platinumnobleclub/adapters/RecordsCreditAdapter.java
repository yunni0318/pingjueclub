package com.wedoops.platinumnobleclub.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.TransactionsCreditData;
import com.wedoops.platinumnobleclub.database.TransactionsReportData;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecordsCreditAdapter extends RecyclerView.Adapter<RecordsCreditAdapter.MyViewHolder> {

    private List<TransactionsCreditData> tc_Data;
    private static final String KEY_LANG = "key_lang";
    private Context mContext;


    public RecordsCreditAdapter(Context context, List<TransactionsCreditData> tcd) {
        this.mContext = context;
        this.tc_Data = tcd;
    }

    public void UpdateRecordListAdapter(List<TransactionsCreditData> tc_d) {

        this.tc_Data = tc_d;
        this.notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textview_remarks, textview_transaction_id, textview_points_amount, textview_date, textview_transaction_type;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_remarks = itemView.findViewById(R.id.textview_remarks);
            textview_transaction_id = itemView.findViewById(R.id.textview_transaction_id);
            textview_points_amount = itemView.findViewById(R.id.textview_points_amount);
            textview_date = itemView.findViewById(R.id.textview_date);
            textview_transaction_type = itemView.findViewById(R.id.textview_transaction_type);

            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/NUNITOSANS-REGULAR.TTF");
            textview_transaction_id.setTypeface(typeface);
            textview_date.setTypeface(typeface);
            textview_transaction_type.setTypeface(typeface);

            Typeface typeface_extra_thicc = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/NUNITOSANS-BOLD.TTF");
            textview_remarks.setTypeface(typeface_extra_thicc);
            textview_points_amount.setTypeface(typeface_extra_thicc);


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_credit_list, parent, false);

        return new RecordsCreditAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String lang = new ApplicationClass().readFromSharedPreferences(mContext, KEY_LANG);


        if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
            holder.textview_transaction_id.setText(String.format("ID: %s", tc_Data.get(position).getTRederenceCode()));
            holder.textview_remarks.setText(String.format("%s", tc_Data.get(position).getRemarks()));
            holder.textview_points_amount.setText(String.format("- %.2f pts", tc_Data.get(position).getPointAmount()));
            holder.textview_transaction_type.setText(String.format("%s", tc_Data.get(position).getType()));
        } else {
            holder.textview_transaction_id.setText(String.format("ID: %s", tc_Data.get(position).getTRederenceCode()));
            holder.textview_remarks.setText(String.format("%s", tc_Data.get(position).getRemarks()));
            holder.textview_points_amount.setText(String.format("- %.2f pts", tc_Data.get(position).getPointAmount()));
            holder.textview_transaction_type.setText(String.format("%s", tc_Data.get(position).getType()));
        }


        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
            Date new_date_startDate = format.parse(tc_Data.get(position).getTDate());

            SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.US);
            String new_startdate = outFormat.format(new_date_startDate);

            String d = mContext.getResources().getString(R.string.transaction_date);

            holder.textview_date.setText(String.format(d + ": %s", new_startdate));


        } catch (Exception e) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sd", Locale.US);
                Date new_date_startDate = format.parse(tc_Data.get(position).getTDate());

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.US);
                String new_startdate = outFormat.format(new_date_startDate);

                String d = mContext.getResources().getString(R.string.transaction_date);

                holder.textview_date.setText(String.format(d + ": %s", new_startdate));

            } catch (Exception ex) {
                holder.textview_date.setText(tc_Data.get(position).getTDate());
            }

        }


    }

    @Override
    public int getItemCount() {

        if (tc_Data == null) {
            return 0;
        } else {
            if (tc_Data.size() > 0) {
                return tc_Data.size();

            } else {
                return 0;
            }
        }

    }
}
