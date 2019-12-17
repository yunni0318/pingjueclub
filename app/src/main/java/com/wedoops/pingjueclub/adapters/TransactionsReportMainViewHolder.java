package com.wedoops.pingjueclub.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orm.StringUtil;
import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.database.TransactionsReportData;
import com.wedoops.pingjueclub.database.UserDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class TransactionsReportMainViewHolder {

//public class TransactionsReportMainViewHolder extends GroupViewHolder {
//
//    private TextView textview_booking_number, textview_event_date, textview_transaction_amount;
//    private ImageView imageview_expand;
//    public LinearLayout linearlayout_adapter_transactions_report_parent, linearlayout_adapter_transactions_report_inner_parent;
//    private View view;
//
//    public TransactionsReportMainViewHolder(View itemView) {
//        super(itemView);
//        view = itemView;
//        textview_booking_number = itemView.findViewById(R.id.textview_booking_number);
//        textview_event_date = itemView.findViewById(R.id.textview_event_date);
//        textview_transaction_amount = itemView.findViewById(R.id.textview_transaction_amount);
//        imageview_expand = itemView.findViewById(R.id.imageview_expand);
//        linearlayout_adapter_transactions_report_parent = itemView.findViewById(R.id.linearlayout_adapter_transactions_report_parent);
//        linearlayout_adapter_transactions_report_inner_parent = itemView.findViewById(R.id.linearlayout_adapter_transactions_report_inner_parent);
//
//        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/poppins-v6-latin-regular.ttf");
//        textview_event_date.setTypeface(typeface);
//
//
////        Typeface typeface_bold_500 = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/poppins-v6-latin-500.ttf");
////        textview_booking_number.setTypeface(typeface_bold_500);
////        textview_transaction_amount.setTypeface(typeface_bold_500);
//
//        Typeface typeface_bold_700 = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/poppins-v6-latin-700.ttf");
//        textview_booking_number.setTypeface(typeface_bold_700);
//        textview_transaction_amount.setTypeface(typeface_bold_700);
//
//
//
//    }
//
//    public void bind(ExpandableGroup group) {
//
//        textview_booking_number.setText(String.format("#%s", group.getTitle()));
//
//        String table_name = UserDetails.getTableName(TransactionsReportData.class);
//        String rederence_field = StringUtil.toSQLName("TRederenceCode");
//
//        List<TransactionsReportData> trd = TransactionsReportData.findWithQuery(TransactionsReportData.class, "select * from " + table_name + " where " + rederence_field + " = ?", group.getTitle());
//
//        String startdate = trd.get(0).getTDate();
//        String new_startdate = "";
//
//        try {
//            TimeZone tz = TimeZone.getTimeZone("SGT");
//
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
//            format.setTimeZone(tz);
//            Date new_date_startDate = format.parse(startdate);
//
//            SimpleDateFormat outFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm a", Locale.US);
//            outFormat.setTimeZone(tz);
//            new_startdate = outFormat.format(new_date_startDate);
//
//        } catch (Exception e) {
//            Log.e("Date", e.toString());
//        }
//
//        textview_event_date.setText(new_startdate);
//
////        if (trd.get(0).equals("true")) {
////            textview_transaction_amount.setTextColor(Color.parseColor("#8bc34a"));
////            textview_transaction_amount.setText(String.format("RM%s", trd.get(0).getTCashInAmount()));
////
////        } else {
////            textview_transaction_amount.setTextColor(Color.parseColor("#ff0000"));
////            textview_transaction_amount.setText(String.format("-RM%s", trd.get(0).getTCashOutAmount()));
////
////        }
//
////        textview_booking_number.setText(String.format("#%s", trd.getTRederenceCode()));
////
////        String startdate = trd.getTDate();
////        String new_startdate = "";
////
////        try {
////            TimeZone tz = TimeZone.getTimeZone("SGT");
////
////            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
////            format.setTimeZone(tz);
////            Date new_date_startDate = format.parse(startdate);
////
////            SimpleDateFormat outFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm a", Locale.US);
////            outFormat.setTimeZone(tz);
////            new_startdate = outFormat.format(new_date_startDate);
////
////        } catch (Exception e) {
////            Log.e("Date", e.toString());
////        }
////
////        textview_event_date.setText(new_startdate);
////
////        if (trd.getTCashIn().equals("true")) {
////            textview_transaction_amount.setTextColor(Color.parseColor("#8bc34a"));
////            textview_transaction_amount.setText(String.format("RM%s", trd.getTCashInAmount()));
////
////        } else {
////            textview_transaction_amount.setTextColor(Color.parseColor("#ff0000"));
////            textview_transaction_amount.setText(String.format("-RM%s", trd.getTCashOutAmount()));
////
////        }
//
//    }
//
//    @Override
//    public void expand() {
//        super.expand();
//        animateExpand();
//    }
//
//    @Override
//    public void collapse() {
//        super.collapse();
//        animateCollapse();
//    }
//
//    private void animateExpand() {
////        RotateAnimation rotate =
////                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
////        rotate.setDuration(300);
////        rotate.setFillAfter(true);
////        imageview_expand.setAnimation(rotate);
//
//        imageview_expand.setRotation(imageview_expand.getRotation() + 90);
//
//        linearlayout_adapter_transactions_report_parent.setBackground(view.getContext().getResources().getDrawable(R.drawable.recycler_transaction_report_parent_expanded_outerline));
//        linearlayout_adapter_transactions_report_inner_parent.setBackground(view.getContext().getResources().getDrawable(R.drawable.radius_top_left_right_black));
//    }
//
//    private void animateCollapse() {
////        RotateAnimation rotate =
//
//        imageview_expand.setRotation(imageview_expand.getRotation() - 90);
//
//        linearlayout_adapter_transactions_report_parent.setBackground(view.getContext().getResources().getDrawable(R.drawable.recycler_transaction_report_outerline));
//        linearlayout_adapter_transactions_report_inner_parent.setBackground(view.getContext().getResources().getDrawable(R.drawable.radius_all_black));
//
//
//    }

}
