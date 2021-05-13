package com.google.firebase.udacity.counterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.udacity.counterapp.databinding.ActivityIntentsPlaygroundBinding;

public class IntentsPlaygroundActivity extends AppCompatActivity {

    private static final int REQUEST_COUNT = 0;
    private int count=101;
    private int flag = 0;
    ActivityIntentsPlaygroundBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout();
        setupHideErrorForEditText();
        if (savedInstanceState!= null){
            count = savedInstanceState.getInt(Constants.FINAL);
            if (count!=101){
                b.result.setText("Final count recieved : " + count);
                b.result.setVisibility(View.VISIBLE);
            }
        }
    }


    //Initial Setup methods

    /**
     * Setup layout
     */
    private void setupLayout() {
        b = ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Intents Playground");
    }

    /**
     * TextWatcher which hides error when text changes
     */
    private void setupHideErrorForEditText(){
        TextWatcher textWatcher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        b.textField.getEditText().addTextChangedListener(textWatcher);
        b.sendDataField.getEditText().addTextChangedListener(textWatcher);
    }

    //Event Handlers

    /**
     * Sends data to Main Activity
     * @param view
     */
    public void sendData(View view) {
        String input = b.sendDataField.getEditText().getText().toString().trim();

        //Checks if input is empty
        if (input.isEmpty()){
            b.textField.setError("Please Enter Something!");
            return;
        }
        //Get Count
        int initCount = Integer.parseInt(input);

        //Create Intent
        Intent intent = new Intent(this,MainActivity.class);

        //Create bundle to pass
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.INITIAL_COUNT_KEY,initCount);
        bundle.putInt(Constants.MAX_VALUE, 100);
        bundle.putInt(Constants.MIN_VALUE, -100);
        //Pass Bundle
        intent.putExtras(bundle);

        startActivityForResult(intent,REQUEST_COUNT);
    }

    /**
     * Recieves Data from Main Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_COUNT && resultCode == RESULT_OK){
            //get data
            count = data.getIntExtra(Constants.FINAL_VALUE,Integer.MIN_VALUE);

            //Show data
            b.result.setText("Final count recieved : " + count);
            flag = 1;
            b.result.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Explicit Intent to Open Main Activity
     * @param view
     */
    public void openMainActivity(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * Sends implicit intent according to radio button selected
     * @param view
     */
    public void sendImplicitIntent(View view) {
        String input = b.textField.getEditText().getText().toString().trim();

        //Checks if input is empty
        if (input.isEmpty()){
            b.textField.setError("Please Enter Something!");
            return;
        }

        // Checks intent type
        int type = b.intentChoiceRadioBtns.getCheckedRadioButtonId();

        //Handling intent according to choice
        switch (type){
            case R.id.openWebpageRadio:
                openWebPage(input);
                break;
            case R.id.dialNumRadio:
                dialNumber(input);
                break;
            case R.id.shareTextRadio:
                shareText(input);
            default:
                Toast.makeText(this,"Please Select an intent type", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to send Open Webpage Intent
     * @param url
     */
    private void openWebPage(String url) {
        //Checks if url is valid
        if (!url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
            b.textField.setError("Invalid URL!");
            return;
        }

        //Sends intent to open webpage
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
        hideError();
    }

    /**
     * Method to send dial number intent
     * @param number
     */
    private void dialNumber(String number) {
        //Checks if number is valid
        if (!number.matches("^\\d{10}$")){
            b.textField.setError("Invalid Number!");
            return;
        }

        //Sends intent to dial number
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);
        hideError();
    }

    private void shareText(String text) {
        Intent intent = new Intent(); intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "Share text via"));
    }

    // Utility functions

    /**
     * Hides error when option changes
     */
    private void hideError(){
        b.textField.setError(null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.FINAL, count);
    }

}