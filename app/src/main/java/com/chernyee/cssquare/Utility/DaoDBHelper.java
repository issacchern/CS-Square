package com.chernyee.cssquare.Utility;

import android.content.Context;

import com.chernyee.cssquare.model.DaoMaster;
import com.chernyee.cssquare.model.DaoSession;
import com.chernyee.cssquare.model.Note;
import com.chernyee.cssquare.model.NoteDao;

import java.util.List;

import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Issac on 4/9/2016.
 */
public class DaoDBHelper {
    private static DaoDBHelper INSTANCE = null;

    /**
     * not thread-safe
     */
    public static DaoDBHelper getInstance(Context context) {
        if(INSTANCE == null)
            INSTANCE = new DaoDBHelper(context);
        return INSTANCE;
    }

    private static final String DB_NAME = "db_file.db";
    private DaoSession daoSession;
    private AsyncSession asyncSession;

    private DaoDBHelper(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);

        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());

        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
    }

    public NoteDao getNoteDao(){
        return daoSession.getNoteDao();
    }

    public AsyncSession getAsyncSession(){
        return asyncSession;
    }

    public List<Note> getAllNotes(){
        QueryBuilder<Note> queryBuilder = getNoteDao().queryBuilder();
        return queryBuilder.list();
    }
}