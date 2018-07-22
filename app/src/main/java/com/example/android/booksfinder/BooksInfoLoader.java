package com.example.android.booksfinder;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class BooksInfoLoader extends AsyncTaskLoader<ArrayList<BookInfo>> {
    private static final String LOG_TAG = "BooksInfoLoader";

    private String mUrl;
    private String jsonResponse="";
    private ArrayList<BookInfo> bookList = new ArrayList<>();
    public BooksInfoLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public ArrayList<BookInfo> loadInBackground() {
        try {
            jsonResponse = makeHttpRequest(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bookList = extractBookInfo(jsonResponse);
        return bookList;
    }

    @Override
    public void onStartLoading(){
        forceLoad();
    }

    private String makeHttpRequest(String jsonQuery) throws IOException{
        URL queryUrl = null;
        try {
            queryUrl = new URL(jsonQuery);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(jsonQuery==null){
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) queryUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000 /*milliseconds*/);
            httpURLConnection.setReadTimeout(15000 /*milliseconds*/);
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            jsonResponse = readInputStream(inputStream);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if(httpURLConnection !=null){
                httpURLConnection.disconnect();
            }
            if(inputStream !=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readInputStream(InputStream inputStream) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream !=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private ArrayList<BookInfo> extractBookInfo(String jResponse) {

        // Create an empty ArrayList that we can start adding book info to
        ArrayList<BookInfo> bookInfos = new ArrayList<>();

        // Try to parse the jsonResponse. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of BookInfo objects with the corresponding data.

            JSONObject root = new JSONObject(jResponse);

            JSONArray itemsArray = root.getJSONArray("items");

            for (int i=0; i<itemsArray.length(); i++) {
                JSONObject arrayElement = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = arrayElement.getJSONObject("volumeInfo");
                String bookTitle = volumeInfo.getString("title");
                ArrayList<String> authors = new ArrayList<>();
                if(volumeInfo.has("authors")){
                    JSONArray authorNames =volumeInfo.getJSONArray("authors");
                    if(authorNames.length()>0){
                        int x;
                        for(x=0; x<authorNames.length(); x++){
                            authors.add(authorNames.getString(x));
                        }
                    }
                }


                String publisher = " ";
                if(volumeInfo.has("publisher")){
                    publisher = volumeInfo.getString("publisher");
                }
                String publishedDate = " ";
                if(volumeInfo.has("publishedDate"))
                publishedDate = volumeInfo.getString("publishedDate");
                String bookDescription = " ";
                if(volumeInfo.has("description"))
                    bookDescription = volumeInfo.getString("description");
                String bookCoverImageUrl=" ";
                if(volumeInfo.has("imageLinks")){
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    bookCoverImageUrl = imageLinks.getString("thumbnail");
                }

                String previewLinkUrl = volumeInfo.getString("previewLink");
                JSONObject saleInfo = arrayElement.getJSONObject("saleInfo");
                String retailPrice="Free";
                String currencyCode = " ";
                if(saleInfo.has("retailPrice")) {
                    JSONObject price = saleInfo.getJSONObject("retailPrice");
                    retailPrice = price.getString("amount");
                    currencyCode = price.getString("currencyCode");
                }
                Bitmap bookCover = null;
                BookInfo bookInfo = new BookInfo(bookTitle,authors,publisher,bookCoverImageUrl,publishedDate,bookDescription,previewLinkUrl,retailPrice,currencyCode,bookCover);
                bookInfos.add(bookInfo);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("Books Main Activity", "Problem parsing the BookInfo JSON results", e);
        }

        // Return the list of earthquakes
        return bookInfos;
    }
}
