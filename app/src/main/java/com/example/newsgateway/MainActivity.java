package com.example.newsgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String SERVICE_MSG = "SERVICE_MSG";
    static final String NEWS_MSG = "NEWS_MSG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void error404() {
    }

    public void updateData(ArrayList<NewsSource> newsArticleArrayList) {
    }
}