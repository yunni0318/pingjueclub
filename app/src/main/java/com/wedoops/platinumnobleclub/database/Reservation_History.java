package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class Reservation_History extends SugarRecord {

    int srno;
    String productGUID;
    String reservationName;
    int estimateParticipant;
    String reservationNumber;
    String remark;
    String productName;
    String category;
    String productDescription;
    String productImage;
    String userLevelCode;
    String reservationtDate;
    String reservationStatus;
    String createdBy;
    String createdDate;

    public Reservation_History() {
    }

    public Reservation_History(int srno, String productGUID, String reservationName, int estimateParticipant, String reservationNumber, String remark, String productName, String category, String productDescription, String productImage, String userLevelCode, String reservationtDate, String reservationStatus, String createdBy, String createdDate) {
        this.srno = srno;
        this.productGUID = productGUID;
        this.reservationName = reservationName;
        this.estimateParticipant = estimateParticipant;
        this.reservationNumber = reservationNumber;
        this.remark = remark;
        this.productName = productName;
        this.category = category;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.userLevelCode = userLevelCode;
        this.reservationtDate = reservationtDate;
        this.reservationStatus = reservationStatus;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public String getProductGUID() {
        return productGUID;
    }

    public void setProductGUID(String productGUID) {
        this.productGUID = productGUID;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public int getEstimateParticipant() {
        return estimateParticipant;
    }

    public void setEstimateParticipant(int estimateParticipant) {
        this.estimateParticipant = estimateParticipant;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getUserLevelCode() {
        return userLevelCode;
    }

    public void setUserLevelCode(String userLevelCode) {
        this.userLevelCode = userLevelCode;
    }

    public String getReservationtDate() {
        return reservationtDate;
    }

    public void setReservationtDate(String reservationtDate) {
        this.reservationtDate = reservationtDate;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
