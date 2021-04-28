package com.example.newsgateway;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class SourceLoader implements Runnable {

    String pass;
    private MainActivity mainActivity;
    private static final String TAG = "NewsSourceDownloader";
    private final String APIKEY = "https://newsapi.org/v1/sources?language=en&country=us&category=";
    private final String apiresourcekey ="&apiKey=a1c809e2e12b4d4f9e05d1f7d2fb2004";
    ArrayList<NewsSource> newsresourcelist = new ArrayList<>();
    ArrayList<String> newsresourcecategory = new ArrayList<>();
    ArrayList<String> newsresourcecategory1 = new ArrayList<>();
    HashMap<Integer, NewsSource> map = new HashMap<>();
    ArrayList uniqueList = new ArrayList();


    public SourceLoader(MainActivity ma){
        mainActivity = ma;
    }

    @Override
    public void run() {

        Uri.Builder buildURL = Uri.parse(APIKEY+apiresourcekey).buildUpon();
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "run: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();
            Log.d(TAG, "run: "+ connection.getResponseCode());
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            handleResults(null);
            return;
        }
        handleResults(sb.toString());

    }

    public void handleResults(final String jsonString){
        if(jsonString==null){
            Log.d(TAG, "handleResults: download Failed");
            mainActivity.runOnUiThread(() -> mainActivity.error404());
            return;
        }
        final ArrayList<NewsSource> sourcelist = parseJSON(jsonString);

        mainActivity.runOnUiThread(()->{
            if(sourcelist!=null)
                Toast.makeText(mainActivity,"with size"+sourcelist.size(),Toast.LENGTH_LONG).show();
            mainActivity.updateData(sourcelist);
        });
    }

    private ArrayList<NewsSource> parseJSON(String s){
        String id = null, name = null, url = null, category = null;
        try
        {
            JSONObject jr1 = new JSONObject(s);
            JSONArray response1 = jr1.getJSONArray("sources");
            Log.d(TAG, "response1 Length: " +response1.length());

            //News SOURCE data
            for(int i =0; i<response1.length(); i++)
            {
                {
                    JSONObject jb1 = response1.getJSONObject(i);
                    id = jb1.getString("id");
                    Log.d("[" + i + "]" + "Channelid:", id);
                    name = jb1.getString("name");
                    Log.d("[" + i + "]" + "Channelname:", name);
                    url = jb1.getString("url");
                    Log.d("[" + i + "]" + "ChannelURL:", url);
                    category = jb1.getString("category");
                    Log.d("[" + i + "]" + "ChannelCategory:", category);
                }
                newsresourcelist.add(new NewsSource(id, name, url, category));
                newsresourcecategory.add(category);
            }
            for(int k = 0; k<newsresourcecategory.size(); k++)
            {
                Log.d(TAG, "ResourceList: [" + k + "]" + newsresourcecategory);
            }
            Set<String> hashmap = new HashSet<>();
            hashmap.addAll(newsresourcecategory);
            newsresourcecategory.clear();
            newsresourcecategory1.addAll(hashmap);

            Log.d(TAG, "NewResourceList: ["+
                    "]" + newsresourcecategory1);

            for(int i = 0; i<newsresourcelist.size(); i++)
            {
                Log.d(TAG, "ResourceList: [" + i + "]" + newsresourcelist.get(i).getId());
                Log.d(TAG, "ResourceList: [" + i + "]" + newsresourcelist.get(i).getName());
                Log.d(TAG, "ResourceList: [" + i + "]" + newsresourcelist.get(i).getUrl());
                Log.d(TAG, "ResourceList: [" + i + "]" + newsresourcelist.get(i).getCategory());
            }

            return newsresourcelist;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }



}
