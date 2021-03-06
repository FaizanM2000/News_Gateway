package com.example.newsgateway;

import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private TextView authornametext, title, description, pageno;
    private ImageView image;
    Fragment mMyFragment;

    public static final ArticleFragment newInstance(String message, String message1, String message2, String message3, String message4, String message5, String message6) {
        ArticleFragment f = new ArticleFragment();

        Bundle bdl = new Bundle(6);
        if (message != null) {
            bdl.putString(a, message);
        }
        if (message1 != null) {
            bdl.putString(b, message1);
        }
        if (message2 != null) {
            bdl.putString(c, message2);
        }
        if (message3 != null) {
            bdl.putString(d, message3);
        }
        if (message4 != null) {
            bdl.putString(e, message4);
        }
        if (message5 != null) {
            bdl.putString(ArticleFragment.f, message5);
        }
        if (message6 != null) {
            bdl.putString(g, message6);
        }
        f.setArguments(bdl);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onRestoreInstanceState(savedInstanceState);
        final String message = getArguments().getString(a);;
        String message1 = getArguments().getString(b);
        String message2 = getArguments().getString(c);
        String message3 = getArguments().getString(d);
        String message4 = getArguments().getString(e);
        final String message5 = getArguments().getString(f);
        String message6 = getArguments().getString(g);
        message.trim();
        message1.trim();
        message2.trim();
        message3.trim();
        message6.trim();

        setRetainInstance(true);
        View v = inflater.inflate(R.layout.fragmentlayout, container, false);
        authornametext = v.findViewById(R.id.Authorname);
        image = v.findViewById(R.id.positionphoto);
        title = v.findViewById(R.id.Title);
        description = v.findViewById(R.id.Description);
        pageno = v.findViewById(R.id.pageno);
        finalDate = message4;
        /*
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
            Date createdDate = simpleDateFormat.parse(message4.replaceAll("Z$", "+0000"));

            simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm", Locale.getDefault());
            String newDate = simpleDateFormat.format(createdDate);

            String [] d1 = newDate.split(" ");
            finalDate = d1[1]+" "+ d1[2]+", 2021 "+d1[3];
            d2 = finalDate;

//            dateSet(finalDate);
        }catch(Exception pe) {
            Log.d(TAG, "onCreateView: Inside exception");
        }
        */


        Log.d("message2", message2.trim());
        if(message2.trim()!= null && finalDate!= null)
        {
            authornametext.setText(finalDate + "\n\n\n" +message2);
        }
        else if(message2.trim() == null && finalDate!= null)
        {
            authornametext.setText(finalDate);
        }
        else if(message2.trim()!= null && finalDate== null)
        {
            authornametext.setText("");
        }
        else if (message2.trim()== null && finalDate== null)
        {
            authornametext.setText("No Information Available!");
        }
        pageno.setText(message6);



        if (message1 != null) {

            final String imageurl = message1;

            Picasso picasso = new Picasso.Builder(this.getContext()).listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                { // Here we try https if the http image attempt failed
                    Log.d(TAG, "onImageLoadFailed: image failed");
                    final String changedUrl = imageurl.replace("http:", "https:");
                    picasso.load(changedUrl).error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder).into(image);
                }
            }).build();
            picasso.load(imageurl).error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder).into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Uri uri = Uri.parse(message5);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
        if(!message.equals(null))
        {
            title.setText(message);
            title.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Uri uri = Uri.parse(message5);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
//
        }

        else {
            title.setText("No Information Available!");
        }
        if(!message3.equals(null)) {
            description.setText(message3);
            description.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Uri uri = Uri.parse(message5);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
        else {
            description.setText("No Information Available!");
        }
        return v;

    }



    private void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {

            title.setText(savedInstanceState.getString("title"));
            description.setText(savedInstanceState.getString("description"));
            pageno.setText(savedInstanceState.getString("pageno"));
            authornametext.setText(savedInstanceState.getString("author"));
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            mMyFragment = (ArticleFragment) manager.getFragment(savedInstanceState, "myFragment");

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getFragmentManager();
        if(mMyFragment != null) {
            manager.putFragment(outState, "myFragment", mMyFragment);
        }
        outState.putString("author", authornametext.getText().toString());
        outState.putString("title", title.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("pageno", pageno.getText().toString());
    }
}







