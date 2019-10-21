package com.wedoops.pingjueclub.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.database.TransactionsReportData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RecordsListAdapter extends RecyclerView.Adapter<RecordsListAdapter.MyViewHolder> {

    private List<TransactionsReportData> trd_list_all;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageview_default;
        private TextView textview_remarks, textview_transaction_id, textview_points_amount, textview_date, textview_currency_amount, textview_transaction_type, textview_transaction_type_amount;
        private View view_divider;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageview_default = itemView.findViewById(R.id.imageview_default);
            textview_remarks = itemView.findViewById(R.id.textview_remarks);
            textview_transaction_id = itemView.findViewById(R.id.textview_transaction_id);
            textview_points_amount = itemView.findViewById(R.id.textview_points_amount);
            textview_date = itemView.findViewById(R.id.textview_date);
            textview_currency_amount = itemView.findViewById(R.id.textview_currency_amount);
            textview_transaction_type = itemView.findViewById(R.id.textview_transaction_type);
            textview_transaction_type_amount = itemView.findViewById(R.id.textview_transaction_type_amount);

            view_divider = itemView.findViewById(R.id.view_divider);

            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/NUNITOSANS-REGULAR.TTF");
            textview_remarks.setTypeface(typeface);
            textview_transaction_id.setTypeface(typeface);
            textview_points_amount.setTypeface(typeface);
            textview_date.setTypeface(typeface);
            textview_currency_amount.setTypeface(typeface);
            textview_transaction_type.setTypeface(typeface);
            textview_transaction_type_amount.setTypeface(typeface);


        }
    }

    public RecordsListAdapter(List<TransactionsReportData> trd) {
        this.trd_list_all = trd;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_records_list, parent, false);

        return new RecordsListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageview_default.setImageResource(R.drawable.records_list_adapter_default);
        holder.textview_remarks.setText(trd_list_all.get(position).getAdminRemarks());
        holder.textview_transaction_id.setText("(id: xxxxxx)");
        holder.textview_points_amount.setText(String.format("%s pts", trd_list_all.get(position).getTCashOutAmount()));

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
            Date new_date_startDate = format.parse(trd_list_all.get(position).getTDate());

            SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
            String new_startdate = outFormat.format(new_date_startDate);

            holder.textview_date.setText(new_startdate);


        } catch (Exception e) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sd", Locale.US);
                Date new_date_startDate = format.parse(trd_list_all.get(position).getTDate());

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
                String new_startdate = outFormat.format(new_date_startDate);

                holder.textview_date.setText(new_startdate);
            } catch (Exception ex) {
                holder.textview_date.setText(trd_list_all.get(position).getTDate());
            }

        }

        holder.textview_currency_amount.setText("(MYR) XXX");
        holder.textview_transaction_type.setText("Discount");
        holder.textview_transaction_type_amount.setText("-5%");

    }


    @Override
    public int getItemCount() {
        return trd_list_all.size();
    }
}
