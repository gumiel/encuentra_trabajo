package com.gumiel.perez.tareadiplomado.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.gumiel.perez.tareadiplomado.data.JobPostDbContract.JobPost;


/**
 * Created by henry on 20/10/2015.
 */
public class JobPostDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "job_posts.db";
    private static int VERSION = 1;

    public JobPostDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateJobPost = "CREATE TABLE " + JobPost.TABLE_NAME + "(" +
                JobPost._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE," +
                JobPost.TITLE_COLUMN + " TEXT NOT NULL," +
                JobPost.DESCRIPTION_COLUMN + " TEXT NOT NULL," +
                JobPost.POSTED_DATE_COLUMN + " TEXT NOT NULL)";

        db.execSQL(sqlCreateJobPost);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + JobPost.TABLE_NAME);
        onCreate(db);
    }
}
