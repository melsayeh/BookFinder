package com.example.android.booksfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;

import java.io.Serializable;
import java.util.ArrayList;


public class ShowResultsActivity extends AppCompatActivity {

    LinearLayout emptyView;
    TextView emptyStateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        //Set empty view to the ListView to show when empty
        emptyView = findViewById(R.id.empty_state);
        emptyStateText = findViewById(R.id.empty_state_text);

        //If no network show "No Connection"
        if (!isNetworkConnected()){
            emptyStateText.setText(R.string.no_connection);
            emptyView.setVisibility(View.VISIBLE);
        }
        //Get RESULT and COVER variables from the MainActivity
        ArrayList<BookInfo> queryUrl = MainActivity.getResults();
        ArrayList<Bitmap> bookCovers = MainActivity.getCovers();

        //Show the empty view if no results found
        if(queryUrl==null){
            emptyView.setVisibility(View.VISIBLE);
        }else //otherwise, show the results in the screen
        updateUi(queryUrl , bookCovers);
    }

    /* @Params: getBookInfo: results of the parsed JSON Query
       @Params: coverImages: results of the parsed cover Images
     */
    public void updateUi(ArrayList<BookInfo> getBookInfo, ArrayList<Bitmap> coverImages){
        // Fetches {@link ArrayList} earthquake.
        ArrayList<BookInfo> bookList = getBookInfo;
        ArrayList<BookInfo> newList = new ArrayList<>(bookList.size());
        BookInfo finalBookInfo;
        Bitmap itemCoverImage;
        int i;
        //Merge book information with corresponding cover images in one List
        for(i=0; i<bookList.size(); i++){
            itemCoverImage = coverImages.get(i);
            finalBookInfo = bookList.get(i);
            finalBookInfo.setCoverImage(itemCoverImage);
            newList.add(finalBookInfo);
        }

        // Find a reference to the {@link ListView} in the layout
        ListView booksListView = (ListView) findViewById(R.id.list);
        booksListView.setEmptyView(emptyView);

        // Create a new {@link ArrayAdapter} of bookInfo
        BookAdapter<BookInfo> adapter = new BookAdapter<BookInfo>(this, newList);



        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        if (booksListView != null) {
            booksListView.setAdapter(adapter);
        }


    }

    //Test internet connectivity
    //@return connected or not
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
