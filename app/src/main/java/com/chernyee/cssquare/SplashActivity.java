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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chernyee.cssquare.Notification.MyAlarmManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    public static final String CS_FOLDER = "CS-Square";
    public static final String CS_FOLDER_DATABASE = "database";
    public static final String DATABASE_PATH = Environment.getExternalStorageDirectory().getPath()+"/" +
            SplashActivity.CS_FOLDER + "/database";
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

        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int databaseVersion = sharedPreferences.getInt("csdbversion", 7);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("M");
        int month = Integer.parseInt(month_date.format(cal.getTime()));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("csnoads", 0); // bare with it :(
        editor.putInt("cslockmedium", 30);
        editor.putInt("cslockhard", 60);
        editor.putInt("csbeginner", 0); // reset count
        editor.putInt("cseasy", 0);
        editor.putInt("csmedium", 0);
        editor.putInt("cshard", 0);
        editor.putInt("cscomplete", 0);
        editor.putInt("csmonth" + 3, 0); // add March
        editor.commit();





       new VarInit(this,databaseVersion).initializeVariable();

        MyAlarmManager.register(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}