package com.wedoops.pingjueclub.database;

import com.orm.SugarRecord;

public class ServicesTopBannerData extends SugarRecord {
    private String Srno;
    private String ImagePath;
    private String RedirectURL;
    private String AnnouncementType;
    private String AdminRemarks;
    private String PublishDate;
    private boolean Active;
    private String CreatedBy;
    private String CreatedDate;

    public ServicesTopBannerData() {

    }

    public ServicesTopBannerData(String Srno, String ImagePath, String RedirectURL, String AnnouncementType, String AdminRemarks, String PublishDate, boolean Active, String CreatedBy, String CreatedDate) {
        this.Srno = Srno;
        this.ImagePath = ImagePath;
        this.RedirectURL = RedirectURL;
        this.AnnouncementType = AnnouncementType;
        this.AdminRemarks = AdminRemarks;
        this.PublishDate = PublishDate;
        this.Active = Active;
        this.CreatedBy = CreatedBy;
        this.CreatedDate = CreatedDate;
    }

    public String getSrno() {
        return Srno;
    }

    public void setSrno(String srno) {
        Srno = srno;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getRedirectURL() {
        return RedirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        RedirectURL = redirectURL;
    }

    public String getAnnouncementType() {
        return AnnouncementType;
    }

    public void setAnnouncementType(String announcementType) {
        AnnouncementType = announcementType;
    }

    public String getAdminRemarks() {
        return AdminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        AdminRemarks = adminRemarks;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        PublishDate = publishDate;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
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
}

