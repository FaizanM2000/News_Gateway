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
    //ArrayList uniqueList = new ArrayList();
    private String category;

    public SourceLoader(MainActivity ma){
        mainActivity = ma;
    }
    public SourceLoader(MainActivity ma, String item){
        mainActivity = ma;
        category = item;
    }
    @Override
    public void run() {
        String urlToUse;
        if (category != null) {
            if(category.equals("all")){
                Log.d(TAG, "run: category was all ");
                Uri.Builder buildURL = Uri.parse(APIKEY+apiresourcekey).buildUpon();
                urlToUse = buildURL.build().toString();
                Log.d(TAG, "run: " + urlToUse);
            }
            else{
                Log.d(TAG, "run: category was "+category);
                Uri.Builder buildURL = Uri.parse(APIKEY+category+apiresourcekey).buildUpon();
                urlToUse = buildURL.build().toString();
                Log.d(TAG, "run: " + urlToUse);
            }
        }
        else{
            Log.d(TAG, "run: category was null");
            Uri.Builder buildURL = Uri.parse(APIKEY+apiresourcekey).buildUpon();
            urlToUse = buildURL.build().toString();
            Log.d(TAG, "run: " + urlToUse);
        }
       

        StringBuilder sb = new StringBuilder();


        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/88.0");
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                Log.d(TAG, "run: not OK");
                handleResults(null);
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }



        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            handleResults(null);
            return;
        }
        handleResults(sb.toString());

    }

    public void handleResults(final String jsonString){
        if(jsonString==null){
            Log.d(TAG, "handleResults: source download Failed, check source loader");
            mainActivity.runOnUiThread(() -> mainActivity.error404());
            return;
        }
        final ArrayList<NewsSource> sourcelist = parseJSON(jsonString);

        mainActivity.runOnUiThread(()->{
            if(sourcelist!=null)
                Toast.makeText(mainActivity,"with size"+sourcelist.size(),Toast.LENGTH_LONG).show();
            mainActivity.setSources(sourcelist,newsresourcecategory1);
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

                    name = jb1.getString("name");

                    url = jb1.getString("url");

                    category = jb1.getString("category");

                }
                newsresourcelist.add(new NewsSource(id, name, url, category));
                newsresourcecategory.add(category);
            }

            Set<String> hashmap = new HashSet<>();
            hashmap.addAll(newsresourcecategory);
            newsresourcecategory.clear();
            newsresourcecategory1.addAll(hashmap);



            return newsresourcelist;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }



}
