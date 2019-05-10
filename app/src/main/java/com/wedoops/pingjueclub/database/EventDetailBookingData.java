package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class EventDetailBookingData extends SugarRecord {
    private String IsJoined;
    private String LoginID;
    private String EventGUID;
    private String BookingNumber;
    private String BookingDate;
    private String BookingStatus;
    private String PaidAmount;
    private String PaymentStatus;

    public EventDetailBookingData() {

    }

    public EventDetailBookingData(String isJoined, String loginID, String eventGUID, String bookingNumber, String bookingDate, String bookingStatus, String paidAmount, String paymentStatus) {
        IsJoined = isJoined;
        LoginID = loginID;
        EventGUID = eventGUID;
        BookingNumber = bookingNumber;
        BookingDate = bookingDate;
        BookingStatus = bookingStatus;
        PaidAmount = paidAmount;
        PaymentStatus = paymentStatus;
    }

    public String getIsJoined() {
        return IsJoined;
    }

    public void setIsJoined(String joined) {
        IsJoined = joined;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getEventGUID() {
        return EventGUID;
    }

    public void setEventGUID(String eventGUID) {
        EventGUID = eventGUID;
    }

    public String getBookingNumber() {
        return BookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        BookingNumber = bookingNumber;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        BookingStatus = bookingStatus;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        PaidAmount = paidAmount;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }
}
