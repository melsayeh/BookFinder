package com.example.android.booksfinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;

import java.io.Serializable;
import java.util.ArrayList;


public class ShowResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        ArrayList<BookInfo> queryUrl = MainActivity.getResults();
        ArrayList<Bitmap> bookCovers = MainActivity.getCovers();
        updateUi(queryUrl , bookCovers);
    }
    public void updateUi(ArrayList<BookInfo> getBookInfo, ArrayList<Bitmap> coverImages){
        // Fetches {@link ArrayList} earthquake.
        ArrayList<BookInfo> bookList = getBookInfo;
        ArrayList<BookInfo> newList = new ArrayList<>(bookList.size());
        BookInfo finalBookInfo;
        Bitmap itemCoverImage;
        int i;
        for(i=0; i<bookList.size(); i++){
            itemCoverImage = coverImages.get(i);
            finalBookInfo = bookList.get(i);
            finalBookInfo.setCoverImage(itemCoverImage);
            newList.add(finalBookInfo);
        }

        // Find a reference to the {@link ListView} in the layout
        ListView booksListView = (ListView) findViewById(R.id.list);
//        earthquakeListView.setEmptyView(emptyState);

        // Create a new {@link ArrayAdapter} of bookInfo
        BookAdapter<BookInfo> adapter = new BookAdapter<BookInfo>(this, newList);


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        if (booksListView != null) {
            booksListView.setAdapter(adapter);
        }


    }

}
