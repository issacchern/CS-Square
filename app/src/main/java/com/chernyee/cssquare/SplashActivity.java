package com.chernyee.cssquare;

/**
 * Created by Issac on 3/23/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class SplashActivity extends AppCompatActivity {


    private static final String CS_FOLDER = "CS-Square";
    private static final String CS_FOLDER_DATABASE = "database";
    public static String databasePath = "";
    public static int databaseVersion = 7;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,CS_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        String filepath2 = file.getAbsolutePath() ;
        File file2 = new File(filepath2,CS_FOLDER_DATABASE);

        if(!file2.exists()){
            file2.mkdirs();
        }

        databasePath = file2.getAbsolutePath();

        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        databaseVersion = sharedPreferences.getInt("csdbversion", 7);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }, 2000);

    }
}