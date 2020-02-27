package com.wedoops.platinumnobleclub.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.database.TransactionsReportData;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecordsListAdapter extends RecyclerView.Adapter<RecordsListAdapter.MyViewHolder> {

    private List<TransactionsReportData> trd_list_all;
    private static String DATA_TYPE_MERCHANT_PAYMENT = "MERCHANT-PAYMENT";
    private static String DATA_TYPE_ADMIN_TOPUP = "ADMIN-TOPUP";
    private static String DATA_TYPE_ADMIN_DEDUCT = "ADMIN-DEDUCT";
    private static String DATA_TYPE_PAYMENT_PARTIAL = "PAYMENT-PARTIAL";
    private static String DATA_TYPE_PAYMENT_FULL = "PAYMENT-FULL";


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textview_remarks, textview_transaction_id, textview_points_amount, textview_date, textview_currency_amount, textview_transaction_type, textview_transaction_type_amount;
        private View view_divider;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_remarks = itemView.findViewById(R.id.textview_remarks);
            textview_transaction_id = itemView.findViewById(R.id.textview_transaction_id);
            textview_points_amount = itemView.findViewById(R.id.textview_points_amount);
            textview_date = itemView.findViewById(R.id.textview_date);
            textview_currency_amount = itemView.findViewById(R.id.textview_currency_amount);
            textview_transaction_type = itemView.findViewById(R.id.textview_transaction_type);
            textview_transaction_type_amount = itemView.findViewById(R.id.textview_transaction_type_amount);

            view_divider = itemView.findViewById(R.id.view_divider);

            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/NUNITOSANS-REGULAR.TTF");
            textview_transaction_id.setTypeface(typeface);
            textview_date.setTypeface(typeface);
            textview_currency_amount.setTypeface(typeface);
            textview_transaction_type.setTypeface(typeface);
            textview_transaction_type_amount.setTypeface(typeface);

            Typeface typeface_extra_thicc = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/NUNITOSANS-BOLD.TTF");
            textview_remarks.setTypeface(typeface_extra_thicc);
            textview_points_amount.setTypeface(typeface_extra_thicc);



        }
    }

//    public RecordsListAdapter(List<TransactionsReportData> trd) {
//        this.trd_list_all = trd;
//    }

    public RecordsListAdapter() {
    }

    public void UpdateRecordListAdapter(List<TransactionsReportData> trd) {

        this.trd_list_all = trd;
        this.notifyDataSetChanged();
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

        if (trd_list_all.get(position).getType().equals(DATA_TYPE_MERCHANT_PAYMENT)) {

            holder.textview_transaction_id.setText(String.format("ID: %s", trd_list_all.get(position).getTRederenceCode()));
            holder.textview_remarks.setText(String.format("%s", trd_list_all.get(position).getRemarks()));

            BigDecimal discount_amount_bd = new BigDecimal(trd_list_all.get(position).getDiscountAmount());
            discount_amount_bd = discount_amount_bd.setScale(4, BigDecimal.ROUND_HALF_UP);
            discount_amount_bd = discount_amount_bd.setScale(3, BigDecimal.ROUND_DOWN);
            discount_amount_bd = discount_amount_bd.setScale(2, BigDecimal.ROUND_HALF_UP);

            holder.textview_points_amount.setText(String.format("- %s pts", discount_amount_bd.toString()));

            holder.textview_currency_amount.setText("");
            holder.textview_transaction_type.setText(DATA_TYPE_MERCHANT_PAYMENT);
            holder.textview_transaction_type_amount.setText(String.format("Discount %d%% out of %.2f pts", trd_list_all.get(position).getDiscountRate(),trd_list_all.get(position).getActualAmount()));

            holder.textview_currency_amount.setVisibility(View.GONE);
            holder.textview_transaction_type.setVisibility(View.VISIBLE);
            holder.textview_transaction_type_amount.setVisibility(View.VISIBLE);

        } else if (trd_list_all.get(position).getType().equals(DATA_TYPE_ADMIN_TOPUP)) {

            holder.textview_transaction_id.setText(String.format("ID: %s", trd_list_all.get(position).getTRederenceCode()));
            holder.textview_remarks.setText(String.format("%s", trd_list_all.get(position).getRemarks()));

            if (trd_list_all.get(position).isCashIn()) {
                holder.textview_points_amount.setText(String.format("+ %d pts", trd_list_all.get(position).getPointAmount()));

            } else {
                holder.textview_points_amount.setText(String.format("- %d pts", trd_list_all.get(position).getPointAmount()));
            }

            holder.textview_currency_amount.setText("");
            holder.textview_transaction_type.setText(DATA_TYPE_ADMIN_TOPUP);


            holder.textview_currency_amount.setVisibility(View.GONE);
            holder.textview_transaction_type.setVisibility(View.VISIBLE);
            holder.textview_transaction_type_amount.setVisibility(View.GONE);


        } else if (trd_list_all.get(position).getType().equals(DATA_TYPE_ADMIN_DEDUCT)) {

            holder.textview_transaction_id.setText(String.format("ID: %s", trd_list_all.get(position).getTRederenceCode()));
            holder.textview_remarks.setText(String.format("%s", trd_list_all.get(position).getRemarks()));


            if (trd_list_all.get(position).isCashIn()) {
                holder.textview_points_amount.setText(String.format("+ %d pts", trd_list_all.get(position).getPointAmount()));

            } else {
                holder.textview_points_amount.setText(String.format("- %d pts", trd_list_all.get(position).getPointAmount()));
            }

            holder.textview_currency_amount.setText("");
            holder.textview_transaction_type.setText(DATA_TYPE_ADMIN_DEDUCT);

            holder.textview_currency_amount.setVisibility(View.GONE);
            holder.textview_transaction_type.setVisibility(View.VISIBLE);
            holder.textview_transaction_type_amount.setVisibility(View.GONE);


        } else if (trd_list_all.get(position).getType().contains(DATA_TYPE_PAYMENT_PARTIAL)) {

            holder.textview_transaction_id.setText(String.format("ID: %s", trd_list_all.get(position).getTRederenceCode()));
            holder.textview_remarks.setText(String.format("%s", trd_list_all.get(position).getAdminTitle()));

            if (trd_list_all.get(position).isCashIn()) {
                holder.textview_points_amount.setText(String.format("+ %d pts", trd_list_all.get(position).getPointAmount()));

            } else {
                holder.textview_points_amount.setText(String.format("- %d pts", trd_list_all.get(position).getPointAmount()));
            }

            holder.textview_currency_amount.setText("");
            holder.textview_transaction_type.setText(DATA_TYPE_PAYMENT_PARTIAL);
            holder.textview_transaction_type_amount.setText(trd_list_all.get(position).getRemarks());

            holder.textview_currency_amount.setVisibility(View.GONE);
            holder.textview_transaction_type.setVisibility(View.VISIBLE);
            holder.textview_transaction_type_amount.setVisibility(View.VISIBLE);

        } else if (trd_list_all.get(position).getType().contains(DATA_TYPE_PAYMENT_FULL)) {
            holder.textview_transaction_id.setText(String.format("ID: %s", trd_list_all.get(position).getTRederenceCode()));
            holder.textview_remarks.setText(String.format("%s", trd_list_all.get(position).getAdminTitle()));

            holder.textview_points_amount.setText(String.format("- %d pts", trd_list_all.get(position).getPointAmount()));


            holder.textview_currency_amount.setText("");
            holder.textview_transaction_type.setText(DATA_TYPE_PAYMENT_FULL);
            holder.textview_transaction_type_amount.setText(trd_list_all.get(position).getRemarks());

            holder.textview_currency_amount.setVisibility(View.GONE);
            holder.textview_transaction_type.setVisibility(View.VISIBLE);
            holder.textview_transaction_type_amount.setVisibility(View.VISIBLE);

        }

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
            Date new_date_startDate = format.parse(trd_list_all.get(position).getTDate());

            SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.US);
            String new_startdate = outFormat.format(new_date_startDate);

            holder.textview_date.setText(String.format("Date: %s", new_startdate));


        } catch (Exception e) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sd", Locale.US);
                Date new_date_startDate = format.parse(trd_list_all.get(position).getTDate());

                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.US);
                String new_startdate = outFormat.format(new_date_startDate);

                holder.textview_date.setText(String.format("Date: %s", new_startdate));

            } catch (Exception ex) {
                holder.textview_date.setText(trd_list_all.get(position).getTDate());
            }

        }


    }

    @Override
    public int getItemCount() {

        if (trd_list_all == null) {
            return 0;
        } else {
            if (trd_list_all.size() > 0) {
                return trd_list_all.size();

            } else {
                return 0;
            }
        }

    }
}
