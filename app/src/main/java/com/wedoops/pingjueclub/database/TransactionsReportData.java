package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class TransactionsReportData extends SugarRecord {

    private String TRederenceCode;
    private String LoginID;
    private String TCashInAmount;
    private String TCashOutAmount;
    private String TCashIn;
    private String TType;
    private String TDescription;
    private String ActionBy;
    private String AdminRemarks;
    private String Status;
    private String TDate;

    public TransactionsReportData() {

    }

    public TransactionsReportData(String TRederenceCode, String loginID, String TCashInAmount, String TCashOutAmount, String TCashIn, String TType, String TDescription, String actionBy, String adminRemarks, String status, String TDate) {
        this.TRederenceCode = TRederenceCode;
        LoginID = loginID;
        this.TCashInAmount = TCashInAmount;
        this.TCashOutAmount = TCashOutAmount;
        this.TCashIn = TCashIn;
        this.TType = TType;
        this.TDescription = TDescription;
        ActionBy = actionBy;
        AdminRemarks = adminRemarks;
        Status = status;
        this.TDate = TDate;
    }

    public String getTRederenceCode() {
        return TRederenceCode;
    }

    public String getLoginID() {
        return LoginID;
    }

    public String getTCashInAmount() {
        return TCashInAmount;
    }

    public String getTCashOutAmount() {
        return TCashOutAmount;
    }

    public String getTCashIn() {
        return TCashIn;
    }

    public String getTType() {
        return TType;
    }

    public String getTDescription() {
        return TDescription;
    }

    public String getActionBy() {
        return ActionBy;
    }

    public String getAdminRemarks() {
        return AdminRemarks;
    }

    public String getStatus() {
        return Status;
    }

    public String getTDate() {
        return TDate;
    }

    public void setTRederenceCode(String TRederenceCode) {
        this.TRederenceCode = TRederenceCode;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public void setTCashInAmount(String TCashInAmount) {
        this.TCashInAmount = TCashInAmount;
    }

    public void setTCashOutAmount(String TCashOutAmount) {
        this.TCashOutAmount = TCashOutAmount;
    }

    public void setTCashIn(String TCashIn) {
        this.TCashIn = TCashIn;
    }

    public void setTType(String TType) {
        this.TType = TType;
    }

    public void setTDescription(String TDescription) {
        this.TDescription = TDescription;
    }

    public void setActionBy(String actionBy) {
        ActionBy = actionBy;
    }

    public void setAdminRemarks(String adminRemarks) {
        AdminRemarks = adminRemarks;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setTDate(String TDate) {
        this.TDate = TDate;
    }
}
