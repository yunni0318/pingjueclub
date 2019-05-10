package com.wedoops.pingjueclub.database;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionReportExpandableParcable implements Parcelable {
    public final String type;
    public final String amount;
    public final String description;

    public TransactionReportExpandableParcable(String type, String amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    protected TransactionReportExpandableParcable(Parcel in) {
        type = in.readString();
        amount = in.readString();
        description = in.readString();
    }

    public static final Creator<TransactionReportExpandableParcable>CREATOR = new Creator<TransactionReportExpandableParcable>() {
        @Override
        public TransactionReportExpandableParcable createFromParcel(Parcel in) {
            return new TransactionReportExpandableParcable(in);
        }

        @Override
        public TransactionReportExpandableParcable[] newArray(int size) {
            return new TransactionReportExpandableParcable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(amount);
        dest.writeString(description);
    }
}
