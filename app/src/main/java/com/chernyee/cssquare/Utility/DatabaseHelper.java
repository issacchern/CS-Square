package com.chernyee.cssquare.Utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.chernyee.cssquare.SQLiteAssetHelper.SQLiteAssetHelper;
import com.chernyee.cssquare.SplashActivity;


public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "Questions.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, SplashActivity.databasePath,  null, SplashActivity.databaseVersion);
    }



    public Cursor getCodingQuestions() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"Id", "Title", "Description", "Code", "Answer", "Hint", "Tags", "Category", "Difficulty", "Additional"};
        String sqlTables = "CodingQuestions";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }

    public Cursor getInterviewQuestions() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"Id", "Question", "Answer", "Category", "Tag"};
        String sqlTables = "QuestionsAndAnswers";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }

}