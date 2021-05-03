package com.example.newsgateway;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class ArticleLoader implements Runnable {

    private static final String TAG = "";
    private String newsid;
    ArrayList<NewsArticle> articleArrayList = new ArrayList<>();
    private NewsService newsService;
    private String urlToUse;
    private MainActivity mainActivity;
    private static final String newsurl = "https://newsapi.org/v1/articles?source=";
    private static final String apikey = "&apiKey=0c8aeb3669514ee28944fa033c7b34bd";


    public ArticleLoader(NewsService newsService, String id,MainActivity ma) {
        this.newsService = newsService;
        this.newsid = id;
        this.mainActivity = ma;
    }


    @Override
    public void run() {

        Uri urlmaker = Uri.parse(newsurl + newsid + apikey);
        urlToUse = urlmaker.toString();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "");
            connection.connect();

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



        } catch (Exception e) {

            handleResults(null);
            return;
        }

        handleResults(sb.toString());
    }


    public void handleResults(final String jsonString){
        if(jsonString==null){

            mainActivity.runOnUiThread(() -> mainActivity.error404());
            return;
        }
        final ArrayList<NewsArticle> newsArticleArrayList = parseJSON(jsonString);

        if(newsArticleArrayList!=null){
            newsService.makeArticles(newsArticleArrayList);
        }

    }

    private ArrayList<NewsArticle> parseJSON(String s){
        String author = null, title = null, description = null, urlToImage = null, publishedAt = null, url = null;
        try
        {
            JSONObject jr1 = new JSONObject(s);
            JSONArray response1 = jr1.getJSONArray("articles");


            for(int i =0; i<response1.length(); i++)
            {
                {
                    JSONObject job = response1.getJSONObject(i);
                    author = job.getString("author");
                    Log.d("[" + i + "]" + "Author:", author);
                    title = job.getString("title");
                    Log.d("[" + i + "]" + "Title:", title);
                    description = job.getString("description");
                    Log.d("[" + i + "]" + "Description:", description);
                    urlToImage = job.getString("urlToImage");
                    Log.d("[" + i + "]" + "urlToImage:", urlToImage);
                    publishedAt = job.getString("publishedAt");
                    Log.d("[" + i + "]" + "PublishedAt:", publishedAt);
                    url = job.getString("url");
                    Log.d("[" + i + "]" + "url:", url);
                    DateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy HH:mm");
                    Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(publishedAt);
                    publishedAt = dateFormat.format(date);

                }
                articleArrayList.add(new NewsArticle(author, title, description, urlToImage, publishedAt, url));
            }

            return articleArrayList;
        }
        catch (JSONException | ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
