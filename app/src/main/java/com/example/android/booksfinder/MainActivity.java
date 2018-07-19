package com.example.android.booksfinder;

import android.content.Context;
import android.content.Intent;
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

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    static String INITIAL_QUERY = "https://www.googleapis.com/books/v1/volumes?q=";
    String searchCriteria = "intitle";
    RadioGroup radioGroup;
    ImageButton searchButton;
    EditText userInput;

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
                final ProgressBar progressBar = findViewById(R.id.progress_bar);
                searchButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                final Intent goToSearchResults = new Intent(MainActivity.this, ShowResultsActivity.class);
                goToSearchResults.putExtra("QueryURL", getFinalQueryURL());

                //Delay triggering to the Search Results intent for 2 seconds
                //until the progress bar spinner fakes loading
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //Timer will execute this code in the background
                        //runOnUiThread will force it to work on the UI thread
                        //Otherwise, the app will crash
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(goToSearchResults);
                                searchButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }
                }, 2000);

            }
        });


    }

    public String getFinalQueryURL() {
        StringBuilder finalQueryUrl = new StringBuilder();
        finalQueryUrl.append(INITIAL_QUERY);
        finalQueryUrl.append(userInput.getText().toString().replace(" ", "20%"));
        finalQueryUrl.append("+");
        finalQueryUrl.append(searchCriteria);
        finalQueryUrl.append("&maxResults=40");
        return finalQueryUrl.toString();
    }

}
