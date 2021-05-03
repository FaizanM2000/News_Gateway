package com.example.newsgateway;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    static final String SERVICE_MSG = "SERVICE_MSG";
    static final String NEWS_MSG = "NEWS_MSG";
    private static final String TAG = "";
    private ArrayAdapter mDrawerListadapter;
    private Reciever newsreceivercategory;
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

        newsreceivercategory = new Reciever();

        Intent serviceintent = new Intent(MainActivity.this, NewsService.class);
        startService(serviceintent);
        IntentFilter filter1 = new IntentFilter(NEWS_MSG);
        registerReceiver(newsreceivercategory, filter1);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        mDrawerListadapter = new ArrayAdapter<>(this,
                R.layout.drawer_list_object, items);
        mDrawerList.setAdapter(mDrawerListadapter);
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        selectItem(position);
                        Log.d(TAG, items.get(position));
                        pager.setBackground(null);
                        //define intent
                        for(int i = 0; i< newsresourcelist.size(); i++)
                        {
                            if(items.get(position).equals(newsresourcelist.get(i).getName()))
                            {
                                Intent newintent = new Intent();
                                newintent.putExtra("myinfo", newsresourcelist.get(i));
                                //Broadcast the intent
                                newintent.setAction(SERVICE_MSG);
                                sendBroadcast(newintent);
                                mDrawerLayout.closeDrawer(mDrawerList);
//                                startService(intent);
                            }
                        }
                    }
                }
        );

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }
        //sampleReceiver = new SampleReceiver();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Fragment Declaration
        fragments = getFragments();

        newsadapter = new MyPageAdapter(getSupportFragmentManager());
        pager =  findViewById(R.id.viewpager);
        pager.setBackgroundResource(R.drawable.newspic);
        pager.setAdapter(newsadapter);

    }



    private void selectItem(int position)
    {
        Toast.makeText(this, items.get(position), Toast.LENGTH_SHORT).show();
        setTitle(items.get(position));
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    public void setSources(ArrayList<NewsSource> newsresourcelist, ArrayList<String> newsresourcecategory1)
    {
        hashmap.clear();
        items.removeAll(items);
        this.newsresourcelist.removeAll(this.newsresourcelist);
        this.newsresourcelist.addAll(newsresourcelist);

        newsresourcecategory1.add(0, "all");


        if(flag3 == 1)
        {
            newsresource.removeAll(newsresource);
            newsresource.addAll(newsresourcecategory1);
            categoryStringarray = newsresource.toArray(new String[newsresource.size()]);
            flag3++;

        }

        //Fill the list of sources
        for(int k = 0; k< this.newsresourcelist.size(); k++)
        {
            items.add(this.newsresourcelist.get(k).getName());
            hashmap.put(this.newsresourcelist.get(k).getName(), this.newsresourcelist.get(k));
        }
        invalidateOptionsMenu();
        mDrawerListadapter.notifyDataSetChanged();

        /*
        View view;
        for(int k = 0; k< this.newsresourcelist.size(); k++){

            for(int i =0;i<items.size();i++){
                MenuItem item  = view;///NEED A WAY TO GET DRAWER LAYOUT ITEM
                Log.d(TAG, "drawerlist item "+item.getTitle());
                SpannableString s = new SpannableString(item.getTitle().toString());
                if(this.newsresourcelist.get(k).getCategory()=="all"){
                    Log.d(TAG, "setSources: black");
                    s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                    item.setTitle(s);
                }
                if(this.newsresourcelist.get(k).getCategory()=="general"){
                    Log.d(TAG, "setSources: yellow");
                    s.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, s.length(), 0);
                    item.setTitle(s);
                }
                if(this.newsresourcelist.get(k).getCategory()=="sports"){
                    Log.d(TAG, "setSources: blue");
                    s.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
                    item.setTitle(s);
                }
                if(this.newsresourcelist.get(k).getCategory()=="business"){
                    Log.d(TAG, "setSources: green");
                    s.setSpan(new ForegroundColorSpan(Color.GREEN), 0, s.length(), 0);
                    item.setTitle(s);
                }
                if(this.newsresourcelist.get(k).getCategory()=="entertainment"){
                    Log.d(TAG, "setSources: red");
                    s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
                    item.setTitle(s);
                }
                if(this.newsresourcelist.get(k).getCategory()=="science"){
                    Log.d(TAG, "setSources: cyan");
                    s.setSpan(new ForegroundColorSpan(Color.CYAN), 0, s.length(), 0);
                    item.setTitle(s);
                }
                if(this.newsresourcelist.get(k).getCategory()=="health"){
                    Log.d(TAG, "setSources: magenta");
                    s.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, s.length(), 0);
                    item.setTitle(s);
                }
                if(this.newsresourcelist.get(k).getCategory()=="technology"){
                    Log.d(TAG, "setSources: dkgray");
                    s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, s.length(), 0);
                    item.setTitle(s);
                }

            }
        }
        */


    }

    private void reDoFragments(ArrayList<NewsArticle> article)
    {
        for (int i = 0; i < newsadapter.getCount(); i++)
        {
            newsadapter.notifyChangeInPosition(i);
        }
        fragments.clear();

        for (int f = 0; f < article.size(); f++)
        {
            Log.d("redo fragments ran", article.get(f).getTitle());
            fragments.add(ArticleFragment.newInstance(article.get(f).getTitle(), article.get(f).getImageurl(), article.get(f).getAuthor(), article.get(f).getDescription(), article.get(f).getDatepub(), article.get(f).getUrl(), " Page " + (f+1) + " of" + article.size()));

        }

        newsadapter.notifyDataSetChanged();
        pager.setCurrentItem(0);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG,"onPrepareOptionsMenu "+ categoryStringarray.length);
        menu.clear();

        if(categoryStringarray.length != 0)
        {
            for (int i = 0; i < categoryStringarray.length; i++)
            {
                menu.add(R.menu.action_menu, Menu.NONE, 0, categoryStringarray[i]);
            }
            Integer[] colorarray = {Color.BLACK,Color.YELLOW,Color.BLUE,Color.GREEN,Color.RED,Color.CYAN,Color.MAGENTA,Color.DKGRAY};

            for(int i =0; i<menu.size();i++){
                MenuItem item = menu.getItem(i);
                SpannableString s = new SpannableString(menu.getItem(i).getTitle().toString());
                s.setSpan(new ForegroundColorSpan(colorarray[i]), 0, s.length(), 0);
                item.setTitle(s);
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        Log.d("item", valueOf(item));
        SourceLoader sourceLoader = new SourceLoader(MainActivity.this, valueOf(item));
        new Thread(sourceLoader).start();
        return true;
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(newsreceivercategory);
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        items.addAll(savedInstanceState.getStringArrayList("HISTORY"));
        newsresource.addAll(savedInstanceState.getStringArrayList("HISTORY1"));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("HISTORY",items);
        outState.putStringArrayList("HISTORY1",newsresource);

    }

    public void error404() {
        Log.d(TAG, "error404: unable to get news");;
    }
///////////////////////////////////////////////////////////////////////////
    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        //fragment
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    private class Reciever extends BroadcastReceiver
    {
        @Override
        //ONRECEIVE method
        public void onReceive(Context context, Intent intent)
        {
            switch (intent.getAction())
            {
                case NEWS_MSG:
                    if (intent.hasExtra("faizan"))
                    {
                        reDoFragments((ArrayList<NewsArticle>) intent.getSerializableExtra("faizan"));
                    }
            }

        }
    }


}