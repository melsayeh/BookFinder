package com.example.android.booksfinder;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
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

        //EditText that contains user input
        userInput = findViewById(R.id.edit_text);

        //Button shows up to clear the EditText all at once
        final ImageButton clearText = findViewById(R.id.clear_text);


        //Show clearText button when user start typing, if the editText is empty, hide it
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!userInput.getText().toString().isEmpty()){
                    clearText.setVisibility(View.VISIBLE);
                    clearText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userInput.setText("");
                        }
                    });

                }else clearText.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!userInput.getText().toString().isEmpty()){
                    clearText.setVisibility(View.VISIBLE);
                    clearText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userInput.setText("");
                        }
                    });

                }else clearText.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!userInput.getText().toString().isEmpty()){
                    clearText.setVisibility(View.VISIBLE);
                    clearText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userInput.setText("");
                        }
                    });

                }else clearText.setVisibility(View.GONE);
            }
        });



        //Trigger the search button when user hits "Enter" key instead of Search button
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchButton.performClick();

                    //Hide the keyboard after the user hits "Enter"
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert in != null;
                    in.hideSoftInputFromWindow(userInput.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        //Radio group that contains radio buttons for search criteria
        radioGroup = findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.is_title:
                        searchCriteria = "intitle"; //add inTitle to the search query
                        break;

                    case R.id.is_author:
                        searchCriteria = "inauthor"; //add inauthor to the search criteria
                        break;

                    case R.id.is_publisher:
                        searchCriteria = "inpublisher"; //add inpublisher to the search criteria
                        break;

                    case R.id.is_subject:
                        searchCriteria = "subject"; //add subject to the search criteria
                        break;

                }
            }
        });

        searchButton = findViewById(R.id.search_button);

        //Go to Search Results Activity when submitting user input
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert in != null;
                in.hideSoftInputFromWindow(userInput.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //If user left the search field empty show alert dialogue box
                if(userInput.getText().toString().isEmpty()){
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("What?"); //Error title
                    alert.setMessage("Nothing to search for!"); //Error message
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else {
                    LoaderManager loaderManager = getLoaderManager(); //Initiate the loader
                    loaderManager.initLoader(1, null, MainActivity.this);
                    clearText.setVisibility(View.GONE);
                    progressBar = findViewById(R.id.progress_bar);
                    searchButton.setVisibility(View.GONE); //hide the search button
                    progressBar.setVisibility(View.VISIBLE); //show the progress bar
                }
            }
        });

        //hide the keyboard when user touch anywhere in the screen
        final RelativeLayout layout = this.findViewById(R.id.parent_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert im != null;
                im.hideSoftInputFromWindow(layout.getWindowToken(),0);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //destroy loaders if the activity paused
        getLoaderManager().destroyLoader(1);
        getLoaderManager().destroyLoader(2);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args){
        int loaderId = id;
        //Starts the book info loader
        if (loaderId==1) {
            return new BooksInfoLoader(this, getFinalQueryURL());
        }

        //Start loader to fetch images from the corresponding links
        if (loaderId==2){
            ArrayList<String> imageLinks=new ArrayList<>();
            BookInfo line;
            int i;
            for (i=0; i<RESULTS.size(); i++){
                line = RESULTS.get(i);
                imageLinks.add(line.getBookCoverImageUrl()); //array of image links
                Log.v("imageLinks:",imageLinks.get(i));
            }
            //start the loader
            return new CoverImageLoader(this, imageLinks);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        int loaderId = loader.getId();
        if(loaderId==1) {
            //Store the ArrayList parsed from JSON query to the variable RESULT.
            RESULTS = (ArrayList<BookInfo>) data;
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(2,null,MainActivity.this);
        }

        if(loaderId==2){
            //trigger the SHowResultsActivity
            Intent goToSearchResults = new Intent(MainActivity.this, ShowResultsActivity.class);
            startActivity(goToSearchResults);
            searchButton.setVisibility(View.VISIBLE); //show the search button
            progressBar.setVisibility(View.GONE); //hide the progress bar
            COVERS = (ArrayList<Bitmap>) data; //store images in ArrayList Variable COVER
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    //Concatenate INITIAL_QUERY with search criteria and userInput to form the finalQuery
    public String getFinalQueryURL() {
        StringBuilder finalQueryUrl = new StringBuilder();
        finalQueryUrl.append(INITIAL_QUERY);
        finalQueryUrl.append(searchCriteria);
        finalQueryUrl.append(":");
        finalQueryUrl.append(userInput.getText().toString().replace(" ", "+"));
        finalQueryUrl.append("&maxResults=10");
        Log.v(LOG_TAG, "QueryLink"+finalQueryUrl);
        return finalQueryUrl.toString();
    }

}
