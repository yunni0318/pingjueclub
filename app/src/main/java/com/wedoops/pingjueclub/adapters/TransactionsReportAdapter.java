package com.wedoops.pingjueclub.adapters;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.database.MyBookingList;
import com.wedoops.pingjueclub.database.TransactionReportExpandableParcable;
import com.wedoops.pingjueclub.database.TransactionsReportData;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TransactionsReportAdapter extends ExpandableRecyclerViewAdapter<TransactionsReportMainViewHolder, TransactionsReportChildViewHolder> {

    public TransactionsReportAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }


    @Override
    public TransactionsReportMainViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactions_report, parent, false);
        return new TransactionsReportMainViewHolder(v);
    }

    @Override
    public TransactionsReportChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transactions_report_child, parent, false);
        return new TransactionsReportChildViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(TransactionsReportChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

        final TransactionReportExpandableParcable trep = ((TransactionsReportTopExpandableGroup)group).getItems().get(childIndex);
        holder.bind(trep,group);

    }

    @Override
    public void onBindGroupViewHolder(TransactionsReportMainViewHolder holder, int flatPosition, ExpandableGroup group) {

//        List<TransactionsReportData> trd = TransactionsReportData.listAll(TransactionsReportData.class);
//        holder.bind(trd.get(flatPosition));
        holder.bind(group);
    }

}
