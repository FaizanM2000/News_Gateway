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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ArticleLoader implements Runnable {

    private static final String TAG = "";
    private String newsid;
    ArrayList<NewsArticle> articleArrayList = new ArrayList<>();
    private NewsService newsService;
    private String urlToUse;
    private MainActivity mainActivity;
    private static final String newsurl = "https://newsapi.org/v1/articles?source=";
    private static final String apikey = "&apiKey=a1c809e2e12b4d4f9e05d1f7d2fb2004";


    public ArticleLoader(NewsService newsService, String id) {
        this.newsService = newsService;
        this.newsid = id;
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
            connection.connect();
            Log.d(TAG, "run: " + connection.getResponseCode());
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
            Log.d(TAG, "handleResults: article download Failed check article loader");
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
            Log.d(TAG, "Article Length: " +response1.length());

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
                }
                articleArrayList.add(new NewsArticle(author, title, description, urlToImage, publishedAt, url));
            }

            return articleArrayList;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
