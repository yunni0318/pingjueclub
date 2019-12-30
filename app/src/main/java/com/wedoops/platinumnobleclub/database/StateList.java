package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class StateList extends SugarRecord {

    private String StateCode;
    private String StateName;
    private String StateDefaultName;
    private String CountryCode;
    private String Active;
    private String CreatedDate;

    public StateList() {

    }

    public StateList(String stateCode, String stateName, String stateDefaultName, String countryCode, String active, String createdDate) {
        StateCode = stateCode;
        StateName = stateName;
        StateDefaultName = stateDefaultName;
        CountryCode = countryCode;
        Active = active;
        CreatedDate = createdDate;
    }

    public String getStateCode() {
        return StateCode;
    }

    public String getStateName() {
        return StateName;
    }

    public String getStateDefaultName() {
        return StateDefaultName;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public String getActive() {
        return Active;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public void setStateDefaultName(String stateDefaultName) {
        StateDefaultName = stateDefaultName;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public void setActive(String active) {
        Active = active;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
