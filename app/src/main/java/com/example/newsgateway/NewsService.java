package com.example.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.newsgateway.MainActivity.NEWS_MSG;
import static com.example.newsgateway.MainActivity.SERVICE_MSG;

public class NewsService extends Service {

    private boolean ran = true;
    private NewsSource newssource;
    private ArrayList<NewsArticle> articlelist = new ArrayList<>();
    private Reciever serviceReciever;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: new service started");
        serviceReciever = new Reciever();
        IntentFilter filter1 = new IntentFilter(SERVICE_MSG);
        registerReceiver(serviceReciever,filter1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        while(articlelist.size()==0){
                            Thread.sleep(250);
                        }
                        Intent intent1 = new Intent();
                        intent1.setAction(NEWS_MSG);
                        intent1.putExtra("faizan", articlelist);
                        sendBroadcast(intent1);
                        articlelist.removeAll(articlelist);
                    }catch(InterruptedException excp){
                        excp.printStackTrace();
                    }

                }
            }
        }).start();
        return Service.START_STICKY;

    }
    public void makeArticles(ArrayList<NewsArticle> newsArticles){
        articlelist.clear();
        articlelist.addAll(newsArticles);
    }

    public void onDestroy(){

        unregisterReceiver(serviceReciever);
        Intent intent = new Intent(NewsService.this, MainActivity.class);
        stopService(intent);

    }
    private class Reciever extends BroadcastReceiver
    {
        MainActivity ma = new MainActivity();
        @Override
        public void onReceive(Context context, Intent intent)
        {
            switch (intent.getAction()) {
                case SERVICE_MSG:
                    if (intent.hasExtra("myinfo"))
                    {
                        newssource = (NewsSource) intent.getSerializableExtra("myinfo");
                        ArticleLoader articleLoader = new  ArticleLoader(NewsService.this, newssource.getId(),ma);
                        new Thread(articleLoader).start();

                    }
            }

        }
    }


}



