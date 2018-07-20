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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<BookInfo>> {

    private static final String LOG_TAG = "MainActivity";
    static String INITIAL_QUERY = "https://www.googleapis.com/books/v1/volumes?q=";
    String searchCriteria = "intitle";
    RadioGroup radioGroup;
    ImageButton searchButton;
    EditText userInput;
    static ArrayList<BookInfo> RESULTS;
    ProgressBar progressBar;

     public static ArrayList<BookInfo> getResults(){
        return RESULTS;
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
    }

    @Override
    public Loader<ArrayList<BookInfo>> onCreateLoader(int id, Bundle args) {
        BooksInfoLoader booksInfoLoader = new BooksInfoLoader(this,getFinalQueryURL());
        return booksInfoLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<BookInfo>> loader, ArrayList<BookInfo> data) {
        Intent goToSearchResults = new Intent(MainActivity.this, ShowResultsActivity.class);
        //goToSearchResults.putExtra("BookList", data);
        startActivity(goToSearchResults);
        searchButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        RESULTS = data;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BookInfo>> loader) {

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
