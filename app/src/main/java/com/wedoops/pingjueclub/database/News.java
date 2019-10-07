package com.wedoops.pingjueclub.database;

public class News {

    private String title;
    private String desc;
    private int like;
    private String image;
    private int thumbnail;

    public News() {
    }

    public News(String title, String desc, int like, String image, int thumbnail) {
        this.title = title;
        this.desc = desc;
        this.like = like;
        this.image = image;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
