package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class InboxList extends SugarRecord {

    private int inboxid;
    private String LoginID;
    private String Title;
    private String Status;
    private Boolean IsView;
    private String Date;

    public InboxList(){
    }

    public InboxList(int inboxid, String loginID, String title, String status, Boolean isView, String date) {
        this.inboxid = inboxid;
        LoginID = loginID;
        Title = title;
        Status = status;
        IsView = isView;
        Date = date;
    }

    public Integer getinboxid() {
        return inboxid;
    }

    public void setinboxid(Integer inboxid) {
        this.inboxid = inboxid;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Boolean getView() {
        return IsView;
    }

    public void setView(Boolean view) {
        IsView = view;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
