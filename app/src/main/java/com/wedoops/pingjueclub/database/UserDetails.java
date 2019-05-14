package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class UserDetails extends SugarRecord {
    private String accessToken;
    private String refreshToken;
    private String Srno;
    private String LoginID;
    private String Name;
    private String DOB;
    private String Email;
    private String Phone;
    private String CountryCode;
    private String StateCode;
    private String Address;
    private String Gender;
    private String ProfilePictureImagePath;
    private String UserLevelCode;
    private String JoinedDate;
    private String CashWallet;

    public UserDetails() {

    }

    public UserDetails(String accessToken, String refreshToken, String srno, String loginID, String name, String DOB, String email, String phone, String countryCode, String stateCode, String address, String gender, String profilePictureImagePath, String userLevelCode, String joinedDate, String cashWallet) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        Srno = srno;
        LoginID = loginID;
        Name = name;
        this.DOB = DOB;
        Email = email;
        Phone = phone;
        CountryCode = countryCode;
        StateCode = stateCode;
        Address = address;
        Gender = gender;
        ProfilePictureImagePath = profilePictureImagePath;
        UserLevelCode = userLevelCode;
        JoinedDate = joinedDate;
        CashWallet = cashWallet;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getSrno() {
        return Srno;
    }

    public String getLoginID() {
        return LoginID;
    }

    public String getName() {
        return Name;
    }

    public String getDOB() {
        return DOB;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public String getStateCode() {
        return StateCode;
    }

    public String getAddress() {
        return Address;
    }

    public String getGender() {
        return Gender;
    }

    public String getProfilePictureImagePath() {
        return ProfilePictureImagePath;
    }

    public String getUserLevelCode() {
        return UserLevelCode;
    }

    public String getJoinedDate() {
        return JoinedDate;
    }

    public String getCashWallet() {
        return CashWallet;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setSrno(String srno) {
        Srno = srno;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setProfilePictureImagePath(String profilePictureImagePath) {
        ProfilePictureImagePath = profilePictureImagePath;
    }

    public void setUserLevelCode(String userLevelCode) {
        UserLevelCode = userLevelCode;
    }

    public void setJoinedDate(String joinedDate) {
        JoinedDate = joinedDate;
    }

    public void setCashWallet(String cashWallet) {
        CashWallet = cashWallet;
    }
}
