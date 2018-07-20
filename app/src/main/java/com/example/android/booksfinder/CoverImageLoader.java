package com.example.android.booksfinder;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoverImageLoader extends AsyncTaskLoader<Bitmap> {
    public String mUrl;
    public CoverImageLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public Bitmap loadInBackground(){
        Bitmap coverImage = null;
        try {
            URL imageLink = new URL(mUrl);
            coverImage = getImagefromUrl(imageLink);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coverImage;
    }

    @Override
    public void onStartLoading(){
        forceLoad();
    }

    private Bitmap getImagefromUrl(URL url) throws IOException{
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap bookCoverImage = null;

        try {
            connection =(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            connection.connect();

            inputStream = connection.getInputStream();
            bookCoverImage = BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null)
                connection.disconnect();
            if(inputStream !=null)
                inputStream.close();
        }

        return bookCoverImage;
    }

}
