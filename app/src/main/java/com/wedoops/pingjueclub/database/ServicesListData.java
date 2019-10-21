package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class ServicesListData extends SugarRecord {
    private String Srno;
    private String ServiceName;
    private String ServiceDescription;
    private String ServiceImagePath;
    private String Status;
    private String CreatedDate;

    public ServicesListData() {

    }

    public ServicesListData(String Srno, String service_name, String description, String image_path, String status, String CreatedDate) {
        this.Srno = Srno;
        this.ServiceName = service_name;
        this.ServiceDescription = description;
        this.ServiceImagePath = image_path;
        this.Status = status;
        this.CreatedDate = CreatedDate;
    }

    public String getSrno() {
        return Srno;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public String getServiceDescription() {
        return ServiceDescription;
    }

    public String getServiceImagePath() {
        return ServiceImagePath;
    }

    public String getStatus() {
        return Status;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setSrno(String srno) {
        Srno = srno;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public void setServiceDescription(String serviceDescription) {
        ServiceDescription = serviceDescription;
    }

    public void setServiceImagePath(String serviceImagePath) {
        ServiceImagePath = serviceImagePath;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}

