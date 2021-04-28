package com.example.newsgateway;

import java.io.Serializable;

public class NewsArticle implements Serializable {

    private String author;
    private String title;
    private String description;
    private String imageurl;
    private String datepub;
    private String url;

    public NewsArticle(String author, String title, String description, String imageurl, String datepub, String url){
        this.author = author;
        this.title = title;
        this.description = description;
        this.imageurl = imageurl;
        this.datepub = datepub;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDatepub() {
        return datepub;
    }

    public void setDatepub(String datepub) {
        this.datepub = datepub;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
