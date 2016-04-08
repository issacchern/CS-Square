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
    public static final String CS_FOLDER_IMAGES = "images";
    public static final String CS_FOLDER_RECORD = "recording";
    public static final String CS_FOLDER_PATH = Environment.getExternalStorageDirectory().getPath()+"/"+
            SplashActivity.CS_FOLDER;
    public static final String DATABASE_PATH = CS_FOLDER_PATH +"/"+ CS_FOLDER_DATABASE;
    public static final String IMAGES_PATH = CS_FOLDER_PATH +"/"+ CS_FOLDER_IMAGES;
    public static final String RECORD_PATH = CS_FOLDER_PATH +"/"+ CS_FOLDER_RECORD;
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
        File folder = new File(filepath2,CS_FOLDER_DATABASE);

        if(!folder.exists()){
            folder.mkdirs();
        }

        folder = new File(filepath2, CS_FOLDER_IMAGES);
        if(!folder.exists()){
            folder.mkdir();
        }

        folder = new File(filepath2, CS_FOLDER_RECORD);
        if(!folder.exists()){
            folder.mkdir();
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

        if(sharedPreferences.getInt("csmonth"+month,-1) == -1){
            editor.putInt("csmonth"+month, 0);
        }
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