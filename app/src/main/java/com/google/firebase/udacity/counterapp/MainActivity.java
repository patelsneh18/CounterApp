package com.google.firebase.udacity.counterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.udacity.counterapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private int qty = 0;
    private ActivityMainBinding b;
    private int maxVal;
    private int minVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(b.getRoot());
        setupEventHandlers();
        getInitCount();
    }

    /**
     * Gets and sets initial count, minValue, maxValue from IntentsPlayground
     */
    private void getInitCount() {
        Bundle bundle = getIntent().getExtras();
        qty = bundle.getInt(Constants.INITIAL_COUNT_KEY, 0);
        maxVal = bundle.getInt(Constants.MAX_VALUE,Integer.MAX_VALUE);
        minVal = bundle.getInt(Constants.MIN_VALUE,Integer.MIN_VALUE);
        b.qty.setText(String.valueOf(qty));

        if (qty != 0){
            b.sendBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sends Final Count to Intent Playground
     * @param view
     */
    public void sendCount(View view) {
        //Validate count
        if (qty<= maxVal && qty>=minVal){
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_VALUE,qty);
            setResult(RESULT_OK,intent);

            //Close Activity
            finish();
        }
        else {
            Toast.makeText(this,"Not in Range!",Toast.LENGTH_SHORT).show();
        }
    }

    //Onclick listeners for inc and dec button
    private void setupEventHandlers() {
        b.decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decQty();
            }
        });

        b.incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incQty();
            }
        });
    }

    /**
     * Decrements counter by 1
     */
    public void decQty() {
        qty--;
        b.qty.setText("" + qty);
    }

    /**
     * Increments counter by 1
     */
    public void incQty() {
        qty++;
        b.qty.setText("" + qty);
    }

}