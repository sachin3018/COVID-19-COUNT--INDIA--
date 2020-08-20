package com.covidcountindia.covid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView icon;
    TextView total,active,recoverd,death;
    ProgressBar progressBar;
    Button state;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Index.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selected = item.getItemId();
        if(selected==R.id.refresh);{
            total.setVisibility(View.INVISIBLE);
            active.setVisibility(View.INVISIBLE);
            recoverd.setVisibility(View.INVISIBLE);
            death.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            updateCount();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            icon=findViewById(R.id.covid_icon);
        AnimationDrawable animationDrawable=(AnimationDrawable) icon.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        total = findViewById(R.id.total);
        active = findViewById(R.id.active);
        recoverd = findViewById(R.id.recoverd);
        death = findViewById(R.id.death);
        state  = findViewById(R.id.state);
        progressBar = findViewById(R.id.pb);

// update count initialy by application automatically
        updateCount();

        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),State.class));
                finish();
            }
        });

    }


    public class ConnectionTask extends AsyncTask<URL,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl=urls[0];
            String urlResult=null;
            try {
                urlResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return urlResult;
        }
        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            total.setVisibility(View.VISIBLE);
            active.setVisibility(View.VISIBLE);
            recoverd.setVisibility(View.VISIBLE);
            death.setVisibility(View.VISIBLE);
            if(s!=null && !s.equals("")) {
                //jsonShow();
                try {
                    JSONObject totalo = new JSONObject(s);
                    JSONObject data = totalo.getJSONObject("data");
                    JSONObject casec = data.getJSONObject("summary");
                    String totalcase = casec.getString("total");
                    String confirmedcase_indian = casec.getString("confirmedCasesIndian");
                    String confirmedcase_foreign = casec.getString("confirmedCasesForeign");
                    String recoveredc = casec.getString("discharged");
                    String deathc = casec.getString("deaths");
                    total.setText("TOTAL : " + totalcase);
                    active.setText("ACTIVE : " + String.valueOf(Integer.valueOf(totalcase)-Integer.valueOf(recoveredc)-Integer.valueOf(deathc))
                    );
                    recoverd.setText("RECOVERED : "+recoveredc);
                    death.setText("DEATH : "+deathc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                 errorShow();
            }

        }
    }

    private void errorShow() {
        total.setVisibility(View.INVISIBLE);
        recoverd.setVisibility(View.INVISIBLE);
        death.setVisibility(View.INVISIBLE);
        active.setText("Check Your Network Connection"+"\n"+"Try Again");
    }

    public void updateCount(){
        URL url = NetworkUtils.buildUrl();
        // url.setText(gitHubSearchUrl.toString());
        String urlResult=null;
        new ConnectionTask().execute(url);
    }
}
