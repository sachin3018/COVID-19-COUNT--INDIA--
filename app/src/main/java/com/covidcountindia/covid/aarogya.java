package com.covidcountindia.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class aarogya extends AppCompatActivity {

    Button click;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Index.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aarogya);
        click = findViewById(R.id.aarogya);

        boolean installed  =   appInstalledOrNot("nic.goi.aarogyasetu");
        if(installed)
        {
            //System.out.println("App already installed om your phone");
            click.setText("OPEN");

        }

    }

    public void download(View view) {
        if(click.getText().toString().equals("OPEN")){

            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("nic.goi.aarogyasetu");
            if (launchIntent != null) {
                startActivity(launchIntent);//null pointer check in case package name was not found
            }
        }
        else{
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu&hl=en_IN"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),Index.class));
        finish();
    }
}
