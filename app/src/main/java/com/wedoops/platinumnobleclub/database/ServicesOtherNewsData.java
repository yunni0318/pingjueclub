package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class ServicesOtherNewsData extends SugarRecord {
    private String EventGUID;
    private String EventName;
    private String EventCategoryCode;
    private String EventBannerImagePath;
    private String EventPrice;
    private String EventDescription;
    private String TOTALCOUNT;


    public ServicesOtherNewsData() {

    }

    public ServicesOtherNewsData(String event_guid, String event_name, String event_category_code, String event_banner_imagepath, String event_price, String event_description, String total_count) {
        this.EventGUID = event_guid;
        this.EventName = event_name;
        this.EventCategoryCode = event_category_code;
        this.EventBannerImagePath = event_banner_imagepath;
        this.EventPrice = event_price;
        this.EventDescription = event_description;
        this.TOTALCOUNT = total_count;

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

    public String getEventBannerImagePath() {
        return EventBannerImagePath;
    }

    public String getEventPrice() {
        return EventPrice;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public String getTOTALCOUNT() {
        return TOTALCOUNT;
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

    public void setEventBannerImagePath(String eventBannerImagePath) {
        EventBannerImagePath = eventBannerImagePath;
    }

    public void setEventPrice(String eventPrice) {
        EventPrice = eventPrice;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public void setTOTALCOUNT(String TOTALCOUNT) {
        this.TOTALCOUNT = TOTALCOUNT;
    }
}

