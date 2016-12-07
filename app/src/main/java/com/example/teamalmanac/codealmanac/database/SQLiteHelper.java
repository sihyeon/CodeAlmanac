package com.example.teamalmanac.codealmanac.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, SQLContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        init(db);
    }

    private void init(SQLiteDatabase db){
        //삭제
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.FcmUserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.ToDoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.MainFocusEntry.TABLE_NAME);

        //생성
        db.execSQL(
                "CREATE TABLE " + SQLContract.UserEntry.TABLE_NAME + " ( " +
                        SQLContract.UserEntry._ID + " INTEGER PRIMARY KEY, " +
                        SQLContract.UserEntry.COLUMN_NAME_NAME + " text " + " ) "
        );

        db.execSQL(
                "CREATE TABLE " + SQLContract.FcmUserEntry.TABLE_NAME + " ( " +
                        SQLContract.FcmUserEntry._ID + "INTEGER PRIMARY KEY, " +
                        SQLContract.FcmUserEntry.COLUMN_NAME_UUID + " text , " +
                        SQLContract.FcmUserEntry.COLUMN_NAME_REG + " text" + " ) "
        );

        db.execSQL("CREATE TABLE " + SQLContract.ToDoEntry.TABLE_NAME + " ( " +
                SQLContract.ToDoEntry._ID + " INTEGER PRIMARY KEY, " +
                SQLContract.ToDoEntry.COLUMN_NAME_TODO + " text , " +
                SQLContract.ToDoEntry.COLUMN_NAME_DATE + " text , " +
                SQLContract.ToDoEntry.COLUMN_NAME_BUTTON_VISIBLE + " text , " +
                SQLContract.ToDoEntry.COLUMN_NAME_SHOW + " text " + " ) "
        );

        db.execSQL(
                "CREATE TABLE " + SQLContract.MainFocusEntry.TABLE_NAME + " ( " +
                        SQLContract.MainFocusEntry._ID + " INTEGER PRIMARY KEY, " +
                        SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS + " text ," +
                        SQLContract.MainFocusEntry.COLUMN_NAME_DATE + " text , " +
                        SQLContract.MainFocusEntry.COLUMN_NAME_BUTTON_VISIBLE+ " text " +" ) "
        );
    }
}
