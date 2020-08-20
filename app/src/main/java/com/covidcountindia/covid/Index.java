package com.covidcountindia.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }

    public void count(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    public void precaution(View view) {
        startActivity(new Intent(getApplicationContext(),precaution.class));
        finish();
    }

    public void aarogya(View view) {
        startActivity(new Intent(getApplicationContext(),aarogya.class));
        finish();
    }
}
