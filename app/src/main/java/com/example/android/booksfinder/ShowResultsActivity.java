package com.example.android.booksfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;


public class ShowResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

//        Intent intent = getIntent();
        ArrayList<BookInfo> queryUrl = MainActivity.getResults();
        updateUi(queryUrl);
    }
    public void updateUi(ArrayList<BookInfo> getBookInfo){
        // Fetches {@link ArrayList} earthquake.
        ArrayList<BookInfo> bookList = getBookInfo;

        // Find a reference to the {@link ListView} in the layout
        ListView booksListView = (ListView) findViewById(R.id.list);
//        earthquakeListView.setEmptyView(emptyState);

        // Create a new {@link ArrayAdapter} of earthquakes
        BookAdapter<BookInfo> adapter = new BookAdapter<BookInfo>(this, bookList);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        if (booksListView != null) {
            booksListView.setAdapter(adapter);
        }
    }

}
