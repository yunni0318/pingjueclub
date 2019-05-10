package com.wedoops.pingjueclub.adapters;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.wedoops.pingjueclub.database.TransactionReportExpandableParcable;
import com.wedoops.pingjueclub.database.TransactionsReportData;

import java.util.List;

public class TransactionsReportTopExpandableGroup extends ExpandableGroup<TransactionReportExpandableParcable> {

    public TransactionsReportTopExpandableGroup(String booking_number, List<TransactionReportExpandableParcable> items) {
        super(booking_number, items);
    }
}
