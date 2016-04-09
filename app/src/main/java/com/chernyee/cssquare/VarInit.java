package com.chernyee.cssquare;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.widget.Toast;

import com.chernyee.cssquare.Utility.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Issac on 4/1/2016.
 */
public class VarInit {

    private DatabaseHelper db;
    public static List<Question> sharedCodeList = new ArrayList<>();
    public static List<Question2> sharedQAList = new ArrayList<>();
    private int databaseVersion = 0;

    private Context context ;


    public static List<Question> getSharedCodeListInstance(){
        return sharedCodeList;
    }

    public static List<Question2> getSharedQAListInstance(){
        return sharedQAList;
    }

    public VarInit(Context context, int databaseVersion){
        this.databaseVersion = databaseVersion;
        this.context = context;
    }

    public void initializeVariable(){

        try{
            db = new DatabaseHelper(context, databaseVersion);
            sharedCodeList = db.getCodingQuestions();
            sharedQAList = db.getInterviewQuestions();

            Collections.sort(sharedCodeList, new Comparator<Question>() {
                @Override
                public int compare(Question lhs, Question rhs) {
                    return lhs.title.compareTo(rhs.title);
                }
            });
        } catch (Exception e){
            Toast.makeText(context, "Error opening database!", Toast.LENGTH_LONG).show();
            File db = new File(SplashActivity.DATABASE_PATH);
            if(db.exists()) db.delete();
        }



    }

    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap, int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }
}
