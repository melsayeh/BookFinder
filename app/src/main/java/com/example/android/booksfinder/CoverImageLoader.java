package com.example.android.booksfinder;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CoverImageLoader extends AsyncTaskLoader<ArrayList<Bitmap>> {
    private ArrayList<Bitmap> images;
    private ArrayList<String> mUrl;
    public CoverImageLoader(Context context, ArrayList<String> url) {
        super(context);
        mUrl = new ArrayList<>();
        mUrl = (ArrayList<String>) url.clone();
    }

    @Override
    public ArrayList<Bitmap> loadInBackground(){
        Bitmap coverImage;
        ArrayList<Bitmap> images = new ArrayList<>();
        int i;
        //Load Images from the corresponding imageLinks taken from bookInfo Objects
        for (i=0; i<mUrl.size();i++) {
            try {

                if (mUrl.get(i).equals(null) || mUrl.get(i).equals(" ")) {
                    coverImage = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.no_image);
                }else{
                    Log.v("URL",mUrl.get(i));
                    coverImage = getImageFromUrl(new URL(mUrl.get(i)));
            }
                images.add(coverImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.v("Images", images.toString());
        return images;
    }

    @Override
    public void onStartLoading(){
        forceLoad();
    }

    //@Params url: the image link that holds the cover image
    //@Returns fetched image from the url
    private Bitmap getImageFromUrl(URL url) throws IOException{
        InputStream inputStream=null;
        Bitmap bookCoverImage=null;
        HttpURLConnection connection = null;

        try {
            connection =(HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            inputStream = connection.getInputStream();
            bookCoverImage = BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            Log.v("Bitmap Connection Error",e.getLocalizedMessage());
        } finally {
            if(connection != null)
                connection.disconnect();
            if(inputStream !=null)
                inputStream.close();
        }

        return bookCoverImage;
    }

}
