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

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSrno() {
        return Srno;
    }

    public void setSrno(String srno) {
        Srno = srno;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getProfilePictureImagePath() {
        return ProfilePictureImagePath;
    }

    public void setProfilePictureImagePath(String profilePictureImagePath) {
        ProfilePictureImagePath = profilePictureImagePath;
    }

    public String getUserLevelCode() {
        return UserLevelCode;
    }

    public void setUserLevelCode(String userLevelCode) {
        UserLevelCode = userLevelCode;
    }

    public String getJoinedDate() {
        return JoinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        JoinedDate = joinedDate;
    }

    public String getCashWallet() {
        return CashWallet;
    }

    public void setCashWallet(String cashWallet) {
        CashWallet = cashWallet;
    }
}
