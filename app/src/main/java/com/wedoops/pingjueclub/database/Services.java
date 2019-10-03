package com.wedoops.pingjueclub.database;

public class Services {

    private String title;
    private String image;
    private int Thumbnail;

    public Services() {
    }

    public Services(String title, String image, int thumbnail) {
        this.title = title;
        this.image = image;
        Thumbnail = thumbnail;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
