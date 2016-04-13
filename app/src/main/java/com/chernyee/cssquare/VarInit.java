package com.chernyee.cssquare;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.chernyee.cssquare.Utility.DaoDBHelper;
import com.chernyee.cssquare.Utility.DatabaseHelper;
import com.chernyee.cssquare.model.Note;
import com.chernyee.cssquare.model.Question;
import com.chernyee.cssquare.model.QuestionDao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Issac on 4/1/2016.
 */
public class VarInit {

    private DatabaseHelper db;
    public static List<Question1> sharedCodeList = new ArrayList<>();
    public static List<Question2> sharedQAList = new ArrayList<>();
    private int databaseVersion = 0;

    private DaoDBHelper daoDBHelper;
    private QuestionDao questionDao;
    private List<Question> listQuestion;
    private Context context ;


    public static List<Question1> getSharedCodeListInstance(){
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
            db = DatabaseHelper.getInstance(context, databaseVersion);
            sharedCodeList = db.getCodingQuestions();
            sharedQAList = db.getInterviewQuestions();

            daoDBHelper = DaoDBHelper.getInstance(context);

            questionDao = daoDBHelper.getQuestionDao();
            listQuestion = questionDao.loadAll();

            if(listQuestion.size() < 1) { // no data loaded; expect load once
                for(int i = 0; i < sharedCodeList.size(); i++){
                    Question1 q1 = sharedCodeList.get(i);
                    Question q = new Question(Long.parseLong(q1.getId()), false,false,"",null, q1.getDifficulty());
                    questionDao.insert(q);
                    Log.v("add new", q.getId() + " ---");
                }
            }

            DaoDBHelper daoDBHelper = DaoDBHelper.getInstance(context);
            QuestionDao questionDao = daoDBHelper.getQuestionDao();
            QueryBuilder qb = questionDao.queryBuilder();

            if(qb.buildCount().count() < sharedCodeList.size()){ // data is updated through AWS
                for(int i = (int) qb.buildCount().count(); i < sharedCodeList.size(); i++){

                    Question1 q1 = sharedCodeList.get(i);
                    Question q = new Question(Long.parseLong(q1.getId()), false,false,"",null, q1.getDifficulty());
                    questionDao.insert(q);
                }
            }

            Collections.sort(sharedCodeList, new Comparator<Question1>() {
                @Override
                public int compare(Question1 lhs, Question1 rhs) {
                    return lhs.title.substring(lhs.title.indexOf("]") + 1).compareTo(rhs.title.substring(rhs.title.indexOf("]") + 1));
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

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
