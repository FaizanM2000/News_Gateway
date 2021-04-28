package com.example.newsgateway;

import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ArticleFragment extends Fragment {

    final String TAG = "ArticleFragment";
    public static final String a = "a";
    public static final String b = "b";
    public static final String c = "c";
    public static final String d = "d";
    String finalDate;
    public static final String e = "e";
    public static final String f = "f";
    public static final String g = "g";
    String d2 = "";
    NetworkInfo activeNetworkInfo;
    private TextView authornametext, title, description, pageno;
    private ImageView image;
    Fragment mMyFragment;

    public static final ArticleFragment newInstance(String message, String message1, String message2, String message3, String message4, String message5, String message6) {
        ArticleFragment f = new ArticleFragment();


        return f;
    }
}
