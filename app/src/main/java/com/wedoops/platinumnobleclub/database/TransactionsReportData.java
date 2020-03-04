package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class TransactionsReportData extends SugarRecord {
    private String TRederenceCode;
    private String Type;
    private double ActualAmount;
    private double DiscountRate;
    private double DiscountAmount;
    private String Remarks;
    private String TDate;
    private String TStatus;
    private double PointAmount;
    private boolean IsCashIn;
    private String AdminTitle;

    public TransactionsReportData() {

    }

    public TransactionsReportData(String TRederenceCode, String Type, double ActualAmount, double DiscountRate, double DiscountAmount, String Remarks, String TDate, String TStatus, double PointAmount, boolean IsCashIn, String AdminTitle) {
        this.TRederenceCode = TRederenceCode;
        this.Type = Type;
        this.ActualAmount = ActualAmount;
        this.DiscountRate = DiscountRate;
        this.DiscountAmount = DiscountAmount;
        this.Remarks = Remarks;
        this.TDate = TDate;
        this.TStatus = TStatus;
        this.PointAmount = PointAmount;
        this.IsCashIn = IsCashIn;
        this.AdminTitle = AdminTitle;
    }

    public String getTRederenceCode() {
        return TRederenceCode;
    }

    public String getType() {
        return Type;
    }

    public double getActualAmount() {
        return ActualAmount;
    }

    public double getDiscountRate() {
        return DiscountRate;
    }

    public double getDiscountAmount() {
        return DiscountAmount;
    }

    public String getRemarks() {
        return Remarks;
    }

    public String getTDate() {
        return TDate;
    }

    public String getTStatus() {
        return TStatus;
    }

    public double getPointAmount() {
        return PointAmount;
    }

    public boolean isCashIn() {
        return IsCashIn;
    }

    public String getAdminTitle() {
        return AdminTitle;
    }

    public void setTRederenceCode(String TRederenceCode) {
        this.TRederenceCode = TRederenceCode;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setActualAmount(double actualAmount) {
        ActualAmount = actualAmount;
    }

    public void setDiscountRate(int discountRate) {
        DiscountRate = discountRate;
    }

    public void setDiscountAmount(int discountAmount) {
        DiscountAmount = discountAmount;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public void setTDate(String TDate) {
        this.TDate = TDate;
    }

    public void setTStatus(String TStatus) {
        this.TStatus = TStatus;
    }

    public void setPointAmount(int pointAmount) {
        PointAmount = pointAmount;
    }

    public void setCashIn(boolean cashIn) {
        IsCashIn = cashIn;
    }

    public void setAdminTitle(String adminTitle) {
        AdminTitle = adminTitle;
    }
}
