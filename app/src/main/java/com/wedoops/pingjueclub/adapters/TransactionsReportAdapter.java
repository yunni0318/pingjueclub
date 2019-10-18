package com.wedoops.pingjueclub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.wedoops.pingjueclub.R;
import com.wedoops.pingjueclub.database.TransactionReportExpandableParcable;

import java.util.List;

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
