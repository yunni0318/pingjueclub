package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class TransactionsCreditData extends SugarRecord {

    private String TRederenceCode;
    private String Type;
    private String Remarks;
    private String TDate;
    private String TStatus;
    private double PointAmount;
    private boolean IsCashIn;

    public TransactionsCreditData() {
    }

    public TransactionsCreditData(String TRederenceCode, String type, String remarks, String TDate, String TStatus, double pointAmount, boolean isCashIn) {
        this.TRederenceCode = TRederenceCode;
        Type = type;
        Remarks = remarks;
        this.TDate = TDate;
        this.TStatus = TStatus;
        PointAmount = pointAmount;
        IsCashIn = isCashIn;
    }

    public String getTRederenceCode() {
        return TRederenceCode;
    }

    public void setTRederenceCode(String TRederenceCode) {
        this.TRederenceCode = TRederenceCode;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getTDate() {
        return TDate;
    }

    public void setTDate(String TDate) {
        this.TDate = TDate;
    }

    public String getTStatus() {
        return TStatus;
    }

    public void setTStatus(String TStatus) {
        this.TStatus = TStatus;
    }

    public double getPointAmount() {
        return PointAmount;
    }

    public void setPointAmount(double pointAmount) {
        PointAmount = pointAmount;
    }

    public boolean isCashIn() {
        return IsCashIn;
    }

    public void setCashIn(boolean cashIn) {
        IsCashIn = cashIn;
    }
}
