package com.example.android.booksfinder;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String LOG_TAG = "MainActivity";
    static String INITIAL_QUERY = "https://www.googleapis.com/books/v1/volumes?q=";
    String searchCriteria = "intitle";
    RadioGroup radioGroup;
    ImageButton searchButton;
    EditText userInput;
    static ArrayList<BookInfo> RESULTS;
    static ArrayList<Bitmap> COVERS;
    ProgressBar progressBar;

    public static ArrayList<BookInfo> getResults(){
        return RESULTS;
    }

    public static ArrayList<Bitmap> getCovers(){
        return COVERS;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput = findViewById(R.id.edit_text);

        //Trigger the search button when user clicks "Enter" instead of Search button
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchButton.performClick();

                    //Hide the keyboard after the user hits "Enter"
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(userInput.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        radioGroup = findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.is_title:
                        searchCriteria = "intitle";
                        break;

                    case R.id.is_author:
                        searchCriteria = "inauthor";
                        break;

                    case R.id.is_publisher:
                        searchCriteria = "inpublisher";
                        break;

                    case R.id.is_subject:
                        searchCriteria = "subject";
                        break;

                }
            }
        });

        searchButton = findViewById(R.id.search_button);
        //Go to Search Results Activity when submitting user input
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(1,null,MainActivity.this);
                progressBar = findViewById(R.id.progress_bar);
                searchButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        getLoaderManager().destroyLoader(1);
        getLoaderManager().destroyLoader(2);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args){
        int loaderId = id;
        if (loaderId==1) {
            BooksInfoLoader booksInfoLoader = new BooksInfoLoader(this, getFinalQueryURL());
            return booksInfoLoader;
        }

        if (loaderId==2){
            String imageLinks[]=new String[RESULTS.size()];
            BookInfo line;
            int i;
            for (i=0; i<RESULTS.size(); i++){
                line = RESULTS.get(i);
                imageLinks[i] = line.getBookCoverImageUrl();
            }
            CoverImageLoader coverImageLoader = new CoverImageLoader(this, imageLinks);
            return coverImageLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        int loaderId = loader.getId();
        if(loaderId==1) {
            RESULTS = (ArrayList<BookInfo>) data;
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(2,null,MainActivity.this);
        }

        if(loaderId==2){
            Intent goToSearchResults = new Intent(MainActivity.this, ShowResultsActivity.class);
            //goToSearchResults.putExtra("BookList", data);
            startActivity(goToSearchResults);
            searchButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            COVERS = (ArrayList<Bitmap>) data;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public String getFinalQueryURL() {
        StringBuilder finalQueryUrl = new StringBuilder();
        finalQueryUrl.append(INITIAL_QUERY);
        finalQueryUrl.append(userInput.getText().toString().replace(" ", "+"));
        finalQueryUrl.append("+");
        finalQueryUrl.append(searchCriteria);
        finalQueryUrl.append("&maxResults=10");
        return finalQueryUrl.toString();
    }

}
