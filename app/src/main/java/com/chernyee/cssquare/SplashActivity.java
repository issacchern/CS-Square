package com.chernyee.cssquare;

/**
 * Created by Issac on 3/23/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.chernyee.cssquare.Utility.DatabaseHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    public static final String CS_FOLDER = "CS-Square";
    public static final String CS_FOLDER_DATABASE = "database";
    public static String databasePath = "";
    public static int databaseVersion = 7;
    private Cursor cCur;
    private Cursor iCur;
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;
    public static List<Question> sharedCodeList = new ArrayList<>();
    public static List<Question2> sharedQAList = new ArrayList<>();

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

        try{
            db = new DatabaseHelper(this);
            cCur = db.getCodingQuestions();
            iCur = db.getInterviewQuestions();
            db.close();

        } catch(Exception e){
            File file3 = new File(SplashActivity.databasePath + "/" + "Questions.db");
            if(file3.exists()) file3.delete();
        }

        List<Question> codeList = new ArrayList<>();
        List<Question2> qaList = new ArrayList<>();

        if(cCur != null && cCur.moveToFirst()){
            do{
                Question q = new Question(cCur.getString(0),cCur.getString(1),cCur.getString(2),
                        cCur.getString(3),new String(cCur.getBlob(4)),cCur.getString(5),cCur.getString(6),
                        cCur.getString(7),cCur.getString(8),cCur.getString(9));

                codeList.add(q);
            }while(cCur.moveToNext());
            cCur.close();
        }

        if(iCur != null && iCur.moveToFirst()){
            do{
                Question2 q = new Question2(iCur.getString(0),iCur.getString(1),iCur.getString(2),
                        iCur.getString(3),iCur.getString(4));
                qaList.add(q);
            }while(iCur.moveToNext());
            iCur.close();
        }


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("csnoads", 1);
        editor.putInt("csmedium", 30);
        editor.putInt("cshard", 60);
        editor.commit();

        // not recommended
        sharedCodeList.clear();
        sharedCodeList.addAll(codeList);
        Collections.sort(sharedCodeList, new Comparator<Question>() {
            @Override
            public int compare(Question lhs, Question rhs) {
                return lhs.title.compareTo(rhs.title);
            }
        });
        sharedQAList.clear();
        sharedQAList.addAll(qaList);

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