package com.example.teamalmanac.codealmanac;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

public class DataManager {
  //  private static SQLiteHelper mSQLiteHelper;
    private SQLiteDatabase mDB;
    public DataManager(Context context){
        SQLiteHelper helper = new SQLiteHelper(context);
        mDB = helper.getWritableDatabase();
        helper.onCreate(mDB);
        if(mDB == null) {
            helper.onCreate(mDB);
        }
//        mSQLiteHelper = new SQLiteHelper(context);
    }
//    private DataManager(){
//        mSQLiteHelper = new SQLiteHelper(MainActivity.getContext());
//    }
//    private static class Singleton{
//        private static final DataManager instance = new DataManager();
//    }
//
//    public static DataManager getInstance(){
//        return Singleton.instance;
//    }

    public void setUserName(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.UserEntry.COLUMN_NAME_NAME, name);
        if(getUserName() != null){
            mDB.update(SQLContract.UserEntry.TABLE_NAME, contentValues, null, null);
        } else {
            mDB.insert(SQLContract.UserEntry.TABLE_NAME, null, contentValues);
        }
    }

    public String getUserName() {
        Cursor cursor = mDB.query(SQLContract.UserEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(1);
        } else {
            return null;
        }
    }

    public void setMainFocus(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, name);
        if(getUserName() != null){
            mDB.update(SQLContract.MainFocusEntry.TABLE_NAME, contentValues, null, null);
        } else {
            mDB.insert(SQLContract.MainFocusEntry.TABLE_NAME, null, contentValues);
        }
    }

    public String getMainFocus() {
        Cursor cursor = mDB.query(SQLContract.MainFocusEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(1);
        } else {
            return null;
        }
    }
}
