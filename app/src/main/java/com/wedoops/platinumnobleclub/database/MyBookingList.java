package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class MyBookingList extends SugarRecord {

    private String Srno;
    private String EventGUID;
    private String EventName;
    private String EventCategoryCode;
    private String EventDescription;
    private String EventBannerImagePath;
    private Double EventPrice;
    private Double EventUpfrontRate;
    private String UserLevelCode;
    private String UserLevelCodeVisibility;
    private String MaxParticipant;
    private String EventStartDate;
    private String EventEndDate;
    private String RegistrationStartDate;
    private String RegistrationEndDate;
    private boolean Active;
    private String CreatedBy;
    private String CreatedDate;
    private String ReservedSeat;
    private String BookingNumber;

    public MyBookingList() {

    }

    public MyBookingList(String srno, String eventGUID, String eventName, String eventCategoryCode, String eventDescription, String eventBannerImagePath, Double eventPrice, Double eventUpfrontRate, String userLevelCode, String userLevelCodeVisibility, String maxParticipant, String eventStartDate, String eventEndDate, String registrationStartDate, String registrationEndDate, boolean active, String createdBy, String createdDate, String reservedSeat, String bookingNumber) {
        Srno = srno;
        EventGUID = eventGUID;
        EventName = eventName;
        EventCategoryCode = eventCategoryCode;
        EventDescription = eventDescription;
        EventBannerImagePath = eventBannerImagePath;
        EventPrice = eventPrice;
        EventUpfrontRate = eventUpfrontRate;
        UserLevelCode = userLevelCode;
        UserLevelCodeVisibility = userLevelCodeVisibility;
        MaxParticipant = maxParticipant;
        EventStartDate = eventStartDate;
        EventEndDate = eventEndDate;
        RegistrationStartDate = registrationStartDate;
        RegistrationEndDate = registrationEndDate;
        Active = active;
        CreatedBy = createdBy;
        CreatedDate = createdDate;
        ReservedSeat = reservedSeat;
        BookingNumber = bookingNumber;
    }

    public String getSrno() {
        return Srno;
    }

    public String getEventGUID() {
        return EventGUID;
    }

    public String getEventName() {
        return EventName;
    }

    public String getEventCategoryCode() {
        return EventCategoryCode;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public String getEventBannerImagePath() {
        return EventBannerImagePath;
    }

    public Double getEventPrice() {
        return EventPrice;
    }

    public Double getEventUpfrontRate() {
        return EventUpfrontRate;
    }

    public String getUserLevelCode() {
        return UserLevelCode;
    }

    public String getUserLevelCodeVisibility() {
        return UserLevelCodeVisibility;
    }

    public String getMaxParticipant() {
        return MaxParticipant;
    }

    public String getEventStartDate() {
        return EventStartDate;
    }

    public String getEventEndDate() {
        return EventEndDate;
    }

    public String getRegistrationStartDate() {
        return RegistrationStartDate;
    }

    public String getRegistrationEndDate() {
        return RegistrationEndDate;
    }

    public boolean isActive() {
        return Active;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public String getReservedSeat() {
        return ReservedSeat;
    }

    public String getBookingNumber() {
        return BookingNumber;
    }

    public void setSrno(String srno) {
        Srno = srno;
    }

    public void setEventGUID(String eventGUID) {
        EventGUID = eventGUID;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public void setEventCategoryCode(String eventCategoryCode) {
        EventCategoryCode = eventCategoryCode;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public void setEventBannerImagePath(String eventBannerImagePath) {
        EventBannerImagePath = eventBannerImagePath;
    }

    public void setEventPrice(Double eventPrice) {
        EventPrice = eventPrice;
    }

    public void setEventUpfrontRate(Double eventUpfrontRate) {
        EventUpfrontRate = eventUpfrontRate;
    }

    public void setUserLevelCode(String userLevelCode) {
        UserLevelCode = userLevelCode;
    }

    public void setUserLevelCodeVisibility(String userLevelCodeVisibility) {
        UserLevelCodeVisibility = userLevelCodeVisibility;
    }

    public void setMaxParticipant(String maxParticipant) {
        MaxParticipant = maxParticipant;
    }

    public void setEventStartDate(String eventStartDate) {
        EventStartDate = eventStartDate;
    }

    public void setEventEndDate(String eventEndDate) {
        EventEndDate = eventEndDate;
    }

    public void setRegistrationStartDate(String registrationStartDate) {
        RegistrationStartDate = registrationStartDate;
    }

    public void setRegistrationEndDate(String registrationEndDate) {
        RegistrationEndDate = registrationEndDate;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public void setReservedSeat(String reservedSeat) {
        ReservedSeat = reservedSeat;
    }

    public void setBookingNumber(String bookingNumber) {
        BookingNumber = bookingNumber;
    }
}
