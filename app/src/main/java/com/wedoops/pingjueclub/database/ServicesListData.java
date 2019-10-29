package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class ServicesListData extends SugarRecord {
    private String MainServicesID;
    private String MainServiceName;
    private String MainServiceImagePath;


    public ServicesListData() {

    }

    public ServicesListData(String mainServicesID, String mainServiceName, String mainServiceImagePath) {
        this.MainServicesID = mainServicesID;
        this.MainServiceName = mainServiceName;
        this.MainServiceImagePath = mainServiceImagePath;
    }

    public String getMainServicesID() {
        return MainServicesID;
    }

    public String getMainServiceName() {
        return MainServiceName;
    }

    public String getMainServiceImagePath() {
        return MainServiceImagePath;
    }

    public void setMainServicesID(String mainServicesID) {
        MainServicesID = mainServicesID;
    }

    public void setMainServiceName(String mainServiceName) {
        MainServiceName = mainServiceName;
    }

    public void setMainServiceImagePath(String mainServiceImagePath) {
        MainServiceImagePath = mainServiceImagePath;
    }
}

