package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class CountryList extends SugarRecord {
    private String CountryCode;
    private String PhoneCode;
    private String CountryName;
    private String CountryDefaultName;
    private String Active;
    private String CreatedDate;

    public CountryList() {

    }

    public CountryList(String countryCode, String phoneCode, String countryName, String countryDefaultName, String active, String createdDate) {
        CountryCode = countryCode;
        PhoneCode = phoneCode;
        CountryName = countryName;
        CountryDefaultName = countryDefaultName;
        Active = active;
        CreatedDate = createdDate;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public String getPhoneCode() {
        return PhoneCode;
    }

    public String getCountryName() {
        return CountryName;
    }

    public String getCountryDefaultName() {
        return CountryDefaultName;
    }

    public String getActive() {
        return Active;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public void setPhoneCode(String phoneCode) {
        PhoneCode = phoneCode;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public void setCountryDefaultName(String countryDefaultName) {
        CountryDefaultName = countryDefaultName;
    }

    public void setActive(String active) {
        Active = active;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
