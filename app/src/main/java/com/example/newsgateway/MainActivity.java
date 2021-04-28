package com.example.newsgateway;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String SERVICE_MSG = "SERVICE_MSG";
    static final String NEWS_MSG = "NEWS_MSG";
    private ArrayAdapter mDrawerListadapter;
    private NewsService.ServiceReceiver serviceReciever;
    private MyPageAdapter newsadapter;
    private List<Fragment> fragments;
    private ViewPager pager;
    private NewsArticle newsarticle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    HashMap hashmap = new HashMap();
    private int flag3 = 1;
    private static int flag2 = 1;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<String> newsresource = new ArrayList<>();
    private ArrayList<String> newsresource1 = new ArrayList<>();
    private ArrayList<NewsSource> newsresourcelist = new ArrayList<>();
    String et2;
    String[] categoryStringarray = new String[newsresource.size()];
    Fragment mContent;
    private static FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //describe drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        String allcategory = "all";
        SourceLoader sourceLoader = new SourceLoader(MainActivity.this);
        new Thread(sourceLoader).start();

        newsreceivercategory = new NewsService.ServiceReceiver();


    }

    public void error404() {
    }

    public void updateData(ArrayList<NewsSource> newsArticleArrayList) {
    }

    public void updateArticleData(ArrayList<NewsArticle> newsArticleArrayList) {
    }
}