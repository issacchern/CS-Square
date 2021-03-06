package com.chernyee.cssquare.Utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.chernyee.cssquare.Question1;
import com.chernyee.cssquare.Question2;
import com.chernyee.cssquare.SQLiteAssetHelper.SQLiteAssetHelper;
import com.chernyee.cssquare.SplashActivity;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "Questions.db";
    private static DatabaseHelper mInstance = null;

    public static DatabaseHelper getInstance(Context context, int databaseVersion){
        if (mInstance == null){
            mInstance = new DatabaseHelper(context.getApplicationContext(), databaseVersion);
        }
        return mInstance;
    }


    public DatabaseHelper(Context context, int databaseVersion) {
        super(context, DATABASE_NAME, SplashActivity.DATABASE_PATH, null, databaseVersion);
    }

    public void resetDB(){

        mInstance = null;
    }


    public List<Question1> getCodingQuestions() {

        List<Question1> codeList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"Id", "Title", "Description", "Code", "Answer", "Hint", "Tags", "Category", "Difficulty", "Additional"};
        String sqlTables = "CodingQuestions";
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        if(c.moveToFirst()){
            do{
                Question1 q = new Question1(c.getString(0),c.getString(1),c.getString(2),
                        c.getString(3),new String(c.getBlob(4)),c.getString(5),c.getString(6),
                        c.getString(7),c.getString(8),c.getString(9));
                codeList.add(q);
            }while(c.moveToNext());
        }
        c.close();
        return codeList;
    }

    public List<Question2> getInterviewQuestions() {
        List<Question2> qaList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"Id", "Question", "Answer", "Category", "Tag"};
        String sqlTables = "QuestionsAndAnswers";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        if(c.moveToNext()){
            do{
                Question2 q = new Question2(c.getString(0),c.getString(1),c.getString(2),
                        c.getString(3),c.getString(4));
                qaList.add(q);
            }while(c.moveToNext());
        }
        c.close();
        return qaList;
    }

}