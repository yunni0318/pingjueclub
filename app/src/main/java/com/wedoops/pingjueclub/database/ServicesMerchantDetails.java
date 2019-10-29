package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class ServicesMerchantDetails extends SugarRecord {
    private String Srno;
    private String MerchantID;
    private String CompanyRegisterNo;
    private String CompanyCategory;
    private String CompanyName;
    private String CompanyAddress;
    private String CompanyLatX;
    private String CompanyLatY;
    private String CompanyDescription;
    private String CompanyLogo;
    private String CompanyImages;
    private String Status;


    public ServicesMerchantDetails() {

    }

    public ServicesMerchantDetails(String srno, String merchantID, String companyRegisterNo, String companyCategory, String companyName, String companyAddress, String companyLatX, String companyLatY, String companyDescription, String companyLogo, String companyImages, String status) {
        Srno = srno;
        MerchantID = merchantID;
        CompanyRegisterNo = companyRegisterNo;
        CompanyCategory = companyCategory;
        CompanyName = companyName;
        CompanyAddress = companyAddress;
        CompanyLatX = companyLatX;
        CompanyLatY = companyLatY;
        CompanyDescription = companyDescription;
        CompanyLogo = companyLogo;
        CompanyImages = companyImages;
        Status = status;
    }

    public String getSrno() {
        return Srno;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public String getCompanyRegisterNo() {
        return CompanyRegisterNo;
    }

    public String getCompanyCategory() {
        return CompanyCategory;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public String getCompanyLatX() {
        return CompanyLatX;
    }

    public String getCompanyLatY() {
        return CompanyLatY;
    }

    public String getCompanyDescription() {
        return CompanyDescription;
    }

    public String getCompanyLogo() {
        return CompanyLogo;
    }

    public String getCompanyImages() {
        return CompanyImages;
    }

    public String getStatus() {
        return Status;
    }

    public void setSrno(String srno) {
        Srno = srno;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }

    public void setCompanyRegisterNo(String companyRegisterNo) {
        CompanyRegisterNo = companyRegisterNo;
    }

    public void setCompanyCategory(String companyCategory) {
        CompanyCategory = companyCategory;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public void setCompanyAddress(String companyAddress) {
        CompanyAddress = companyAddress;
    }

    public void setCompanyLatX(String companyLatX) {
        CompanyLatX = companyLatX;
    }

    public void setCompanyLatY(String companyLatY) {
        CompanyLatY = companyLatY;
    }

    public void setCompanyDescription(String companyDescription) {
        CompanyDescription = companyDescription;
    }

    public void setCompanyLogo(String companyLogo) {
        CompanyLogo = companyLogo;
    }

    public void setCompanyImages(String companyImages) {
        CompanyImages = companyImages;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

