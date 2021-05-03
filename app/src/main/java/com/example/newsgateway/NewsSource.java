package com.example.newsgateway;

import android.text.SpannableString;

import java.io.Serializable;

public class NewsSource implements Serializable {
    private String id;
    private  String name;
    private String url;
    private SpannableString spannedname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setName(SpannableString s){this.spannedname = s;}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;

    public NewsSource(String id, String name, String url, String category){
        this.id = id;
        this.name = name;
        this.url = url;
        this.category = category;
    }











}
