package com.covidcountindia.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class State extends AppCompatActivity {

    AutoCompleteTextView state;
    ImageButton searchB;
    int index =0;
    ProgressBar progressBar;
    TextView totals, actives, recoverds, deaths,statename;
    ImageView icons;
    String[] name = {
            "Andaman and Nicobar Islands",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chandigarh",
            "Chhattisgarh",
            "Dadra and Nagar Haveli and Daman and Diu",
            "Delhi",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu and Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Ladakh",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Puducherry",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttarakhand",
            "Uttar Pradesh",
            "West Bengal"

    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Index.class));
        finish();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        icons = findViewById(R.id.covid_icons);
        AnimationDrawable animationDrawable=(AnimationDrawable) icons.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        state = findViewById(R.id.searchbar);
        searchB  = findViewById(R.id.find);
        progressBar = findViewById(R.id.progress);
        totals  = findViewById(R.id.totals);
        recoverds = findViewById(R.id.recoverds);
        deaths = findViewById(R.id.deaths);
        actives  = findViewById(R.id.actives);
        statename = findViewById(R.id.statename);

        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,name);

        state.setAdapter(adapter);
        state.setThreshold(1);


        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(State.this);
                String a = state.getText().toString();
                for(int i=0;i<name.length;i++){
                    if(name[i].equals(a)){
                        index = i;
                        updateCount();
                        break;
                    }
                }
                state.setText("");
                //Toast.makeText(getApplicationContext(),name[index]+" "+index,Toast.LENGTH_LONG).show();



            }
        });
    }

    public class ConnectionTask extends AsyncTask<URL,Void,String> {
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
            if(s!=null && !s.equals("")) {
                //jsonShow();
                totals.setVisibility(View.VISIBLE);
                recoverds.setVisibility(View.VISIBLE);
                deaths.setVisibility(View.VISIBLE);
                actives.setVisibility(View.VISIBLE);
                statename.setVisibility(View.VISIBLE);
                try {

                    JSONObject totalo = new JSONObject(s);
                    JSONObject data = totalo.getJSONObject("data");
                    JSONArray regional = data.getJSONArray("regional");
                    JSONObject statenumber = regional.getJSONObject(Integer.parseInt(String.valueOf(index)));
                    String total_case = statenumber.getString("totalConfirmed");
                    String death_case = statenumber.getString("deaths");
                    String recovered_case = statenumber.getString("discharged");

                    statename.setText(""+name[index]);
                    totals.setText("TOTAL : "+total_case);
                    deaths.setText("DEATH : "+death_case);
                    recoverds.setText("RECOVERED : "+recovered_case);
                    actives.setText("ACTIVE : "+(Integer.parseInt(total_case)-Integer.parseInt(recovered_case)-Integer.parseInt(death_case)));

                   // Toast.makeText(getApplicationContext(),""+statenumber,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {

                        e.printStackTrace();
                    Toast.makeText(getApplicationContext(),""+s,Toast.LENGTH_LONG).show();
                }

            }else{
                errorShow();
            }

        }
    }

    private void errorShow() {
        statename.setVisibility(View.VISIBLE);
        statename.setText("Something Went Wrong!");
        totals.setVisibility(View.INVISIBLE);
        recoverds.setVisibility(View.INVISIBLE);
        deaths.setVisibility(View.INVISIBLE);
        actives.setVisibility(View.INVISIBLE);
    }

    public void updateCount(){
        URL url = NetworkUtils.buildUrl();
        // url.setText(gitHubSearchUrl.toString());
        String urlResult=null;
        new ConnectionTask().execute(url);
    }
}
