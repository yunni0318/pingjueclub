package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class EventDetailEventData extends SugarRecord {

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
    private String Active;
    private String CreatedBy;
    private String CreatedDate;
    private String ReservedSeat;
    private String BookingNumber;

    public EventDetailEventData() {

    }

    public EventDetailEventData(String srno, String eventGUID, String eventName, String eventCategoryCode, String eventDescription, String eventBannerImagePath, Double eventPrice, Double eventUpfrontRate, String userLevelCode, String userLevelCodeVisibility, String maxParticipant, String eventStartDate, String eventEndDate, String registrationStartDate, String registrationEndDate, String active, String createdBy, String createdDate, String reservedSeat, String bookingNumber) {
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

    public void setSrno(String srno) {
        Srno = srno;
    }

    public String getEventGUID() {
        return EventGUID;
    }

    public void setEventGUID(String eventGUID) {
        EventGUID = eventGUID;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventCategoryCode() {
        return EventCategoryCode;
    }

    public void setEventCategoryCode(String eventCategoryCode) {
        EventCategoryCode = eventCategoryCode;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public String getEventBannerImagePath() {
        return EventBannerImagePath;
    }

    public void setEventBannerImagePath(String eventBannerImagePath) {
        EventBannerImagePath = eventBannerImagePath;
    }

    public Double getEventPrice() {
        return EventPrice;
    }

    public void setEventPrice(Double eventPrice) {
        EventPrice = eventPrice;
    }

    public Double getEventUpfrontRate() {
        return EventUpfrontRate;
    }

    public void setEventUpfrontRate(Double eventUpfrontRate) {
        EventUpfrontRate = eventUpfrontRate;
    }

    public String getUserLevelCode() {
        return UserLevelCode;
    }

    public void setUserLevelCode(String userLevelCode) {
        UserLevelCode = userLevelCode;
    }

    public String getUserLevelCodeVisibility() {
        return UserLevelCodeVisibility;
    }

    public void setUserLevelCodeVisibility(String userLevelCodeVisibility) {
        UserLevelCodeVisibility = userLevelCodeVisibility;
    }

    public String getMaxParticipant() {
        return MaxParticipant;
    }

    public void setMaxParticipant(String maxParticipant) {
        MaxParticipant = maxParticipant;
    }

    public String getEventStartDate() {
        return EventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        EventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return EventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        EventEndDate = eventEndDate;
    }

    public String getRegistrationStartDate() {
        return RegistrationStartDate;
    }

    public void setRegistrationStartDate(String registrationStartDate) {
        RegistrationStartDate = registrationStartDate;
    }

    public String getRegistrationEndDate() {
        return RegistrationEndDate;
    }

    public void setRegistrationEndDate(String registrationEndDate) {
        RegistrationEndDate = registrationEndDate;
    }

    public String isActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getReservedSeat() {
        return ReservedSeat;
    }

    public void setReservedSeat(String reservedSeat) {
        ReservedSeat = reservedSeat;
    }

    public String getBookingNumber() {
        return BookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        BookingNumber = bookingNumber;
    }
}
