package com.wedoops.pingjueclub.adapters;

import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orm.StringUtil;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.database.TransactionReportExpandableParcable;
import com.wedoops.pingjueclub.database.TransactionsReportData;
import com.wedoops.pingjueclub.database.UserDetails;

import java.util.List;

public class TransactionsReportChildViewHolder extends ChildViewHolder {

    private TextView textview_transaction_type_title;
    private TextView textview_transaction_type;
    private TextView textview_transaction_amount_title;
    private TextView textview_transaction_amount;
    private TextView textview_transaction_remarks_title;
    private TextView textview_transaction_description;
    private LinearLayout linearlayout_adapter_transactions_report_child;
    private View v;

    public TransactionsReportChildViewHolder(View itemView) {
        super(itemView);
        v = itemView;
        textview_transaction_type_title = itemView.findViewById(R.id.textview_transaction_type_title);
        textview_transaction_type = itemView.findViewById(R.id.textview_transaction_type);
        textview_transaction_amount_title = itemView.findViewById(R.id.textview_transaction_amount_title);
        textview_transaction_amount = itemView.findViewById(R.id.textview_transaction_amount);
        textview_transaction_remarks_title = itemView.findViewById(R.id.textview_transaction_remarks_title);
        textview_transaction_description = itemView.findViewById(R.id.textview_transaction_description);

        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/poppins-v6-latin-regular.ttf");
        textview_transaction_type.setTypeface(typeface);
        textview_transaction_amount.setTypeface(typeface);
        textview_transaction_description.setTypeface(typeface);

        Typeface typeface_bold_700 = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/poppins-v6-latin-700.ttf");
        textview_transaction_type_title.setTypeface(typeface_bold_700);
        textview_transaction_amount_title.setTypeface(typeface_bold_700);
        textview_transaction_remarks_title.setTypeface(typeface_bold_700);

//        Typeface typeface_bold_500 = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/poppins-v6-latin-500.ttf");
//        textview_transaction_type_title.setTypeface(typeface_bold_500);
//        textview_transaction_amount_title.setTypeface(typeface_bold_500);
//        textview_transaction_remarks_title.setTypeface(typeface_bold_500);

    }

    public void bind(TransactionReportExpandableParcable trep, ExpandableGroup group) {

        String table_name = UserDetails.getTableName(TransactionsReportData.class);
        String rederence_field = StringUtil.toSQLName("TRederenceCode");

        List<TransactionsReportData> trd = TransactionsReportData.findWithQuery(TransactionsReportData.class, "select * from " + table_name + " where " + rederence_field + " = ?", group.getTitle());


//        textview_transaction_type.setText(trd.get(0).getTType());
//
//        if (trd.get(0).getTCashIn().equals("true")) {
//            textview_transaction_amount.setText(String.format("RM%s", trd.get(0).getTCashInAmount()));
//
//        } else {
//            textview_transaction_amount.setText(String.format("-RM%s", trd.get(0).getTCashOutAmount()));
//
//        }
//
//        textview_transaction_description.setText(trd.get(0).getTDescription());

////        linearlayout_adapter_transactions_report_child.setBackground(v.getResources().getDrawable(R.drawable.recycler_transaction_report_child_expanded_outerline));
//
//        List<TransactionsReportData> trd_list_all = TransactionsReportData.listAll(TransactionsReportData.class);
//
//        if (trd_list_all.get(position).getTCashIn().equals("true")) {
//            textview_transaction_amount.setText(String.format("RM%s", trd_list_all.get(position).getTCashInAmount()));
//
//        } else {
//            textview_transaction_amount.setText(String.format("-RM%s", trd_list_all.get(position).getTCashOutAmount()));
//
//        }
//        textview_transaction_type.setText(trd_list_all.get(position).getTType());
//        textview_transaction_description.setText(trd_list_all.get(position).getTDescription());

    }

//    public void setTextview_transaction_type(String type) {
//        textview_transaction_type.setText(type);
//
//    }
//
//    public void setTextview_transaction_amount(String amount) {
//        textview_transaction_amount.setText(amount);
//
//    }
//
//    public void setTextview_transaction_description(String description) {
//        textview_transaction_description.setText(description);
//
//    }
}
