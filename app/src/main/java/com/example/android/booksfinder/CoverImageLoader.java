package com.example.android.booksfinder;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CoverImageLoader extends AsyncTaskLoader<ArrayList<Bitmap>> {
    ArrayList<Bitmap> images;
    private String mUrl[];
    public CoverImageLoader(Context context, String url[]) {
        super(context);
        mUrl= new String[url.length];
        mUrl = url.clone();
    }

    @Override
    public ArrayList<Bitmap> loadInBackground(){
        Bitmap coverImage = null;
        ArrayList<Bitmap> images = new ArrayList<>(mUrl.length);
        int i;
        for (i=0; i<mUrl.length;i++) {
            try {
                URL imageLink = new URL(mUrl[i]);
                coverImage = getImagefromUrl(imageLink);
                images.add(coverImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
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
