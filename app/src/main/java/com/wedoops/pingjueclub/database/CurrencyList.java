package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class CurrencyList extends SugarRecord {
    private String Srno;
    private String CurrencyName;
    private String CurrencyCode;
    private double CurrencyRate;
    private String Status;
    private String ImagePath;
    private String CreatedDate;

    public CurrencyList() {

    }

    public CurrencyList(String srno, String currency_name, String currency_code, double currency_rate, String status, String image_path, String created_date) {
        this.Srno = srno;
        this.CurrencyName = currency_name;
        this.CurrencyCode = currency_code;
        this.CurrencyRate = currency_rate;
        this.Status = status;
        this.ImagePath = image_path;
        this.CreatedDate = created_date;

    }

    public String getSrno() {
        return Srno;
    }

    public String getCurrencyName() {
        return CurrencyName;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public double getCurrencyRate() {
        return CurrencyRate;
    }

    public String getStatus() {
        return Status;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setSrno(String srno) {
        Srno = srno;
    }

    public void setCurrencyName(String currencyName) {
        CurrencyName = currencyName;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public void setCurrencyRate(double currencyRate) {
        CurrencyRate = currencyRate;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
