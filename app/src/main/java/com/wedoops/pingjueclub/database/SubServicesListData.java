package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class SubServicesListData extends SugarRecord {
    private String ForeignMainServicesID;
    private String SubServicesID;
    private String SubServiceName;
    private String SubServiceImagePath;


    public SubServicesListData() {

    }

    public SubServicesListData(String foreignMainServicesID, String subServicesID, String subServiceName, String subServiceImagePath) {
        ForeignMainServicesID = foreignMainServicesID;
        SubServicesID = subServicesID;
        SubServiceName = subServiceName;
        SubServiceImagePath = subServiceImagePath;
    }

    public String getForeignMainServicesID() {
        return ForeignMainServicesID;
    }

    public String getSubServicesID() {
        return SubServicesID;
    }

    public String getSubServiceName() {
        return SubServiceName;
    }

    public String getSubServiceImagePath() {
        return SubServiceImagePath;
    }

    public void setForeignMainServicesID(String foreignMainServicesID) {
        ForeignMainServicesID = foreignMainServicesID;
    }

    public void setSubServicesID(String subServicesID) {
        SubServicesID = subServicesID;
    }

    public void setSubServiceName(String subServiceName) {
        SubServiceName = subServiceName;
    }

    public void setSubServiceImagePath(String subServiceImagePath) {
        SubServiceImagePath = subServiceImagePath;
    }
}

