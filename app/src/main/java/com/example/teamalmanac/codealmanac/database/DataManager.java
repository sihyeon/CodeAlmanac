package com.example.teamalmanac.codealmanac.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.example.teamalmanac.codealmanac.TabActivity;
import com.example.teamalmanac.codealmanac.TodoDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

public class DataManager {
    //싱글톤
    private static DataManager singletonInstance = null;
    public static DataManager getSingletonInstance(){
        if(singletonInstance == null) singletonInstance = new DataManager(TabActivity.getMainContext());
        return singletonInstance;
    }

    private SQLiteDatabase mDB;

    private DataManager(Context context){
        SQLiteHelper helper = new SQLiteHelper(context);
        mDB = helper.getWritableDatabase();
        if(mDB == null) {
            helper.onCreate(mDB);
        }
        // 디비를 재생성해야하면 이 코드의 주석을 해제하시오
//        helper = new SQLiteHelper(context);
    }

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

    public void setTodo(String todo, String date, Integer visible) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_TODO, todo);
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_DATE, date);
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_BUTTON_VISIBLE, visible.toString());
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_SHOW, "true");
        mDB.insert(SQLContract.ToDoEntry.TABLE_NAME, null, contentValues);
    }

    public void setTodo(String todo, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_TODO, todo);
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_DATE, date);
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_BUTTON_VISIBLE, String.valueOf(View.INVISIBLE));
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_SHOW, "true");
        mDB.insert(SQLContract.ToDoEntry.TABLE_NAME, null, contentValues);
    }


    public void deleteTodoUsingDate(String date) {
        mDB.delete(SQLContract.ToDoEntry.TABLE_NAME, SQLContract.ToDoEntry.COLUMN_NAME_DATE + "=?", new String[] {date});
    }

    public void updateTodoShowing(String date, Boolean bl) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_SHOW, bl.toString());
        mDB.update(SQLContract.ToDoEntry.TABLE_NAME, contentValues,
                SQLContract.ToDoEntry.COLUMN_NAME_DATE+"=?", new String[] {date});
    }

    public ArrayList<TodoDataType> getTodos() {
        Cursor cursor = mDB.query(SQLContract.ToDoEntry.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<TodoDataType> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                //check is showing on lockscreen(app2)
                if(Boolean.valueOf(cursor.getString(4))) {
                    //todo, date
                    list.add(new TodoDataType(cursor.getString(1), cursor.getString(2)));
                }
            } while(cursor.moveToNext());
        }
        return list;
    }

    public void setMainFocus(String name, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, name);
        contentValues.put(SQLContract.MainFocusEntry.COLUMN_NAME_DATE, date);

        mDB.insert(SQLContract.MainFocusEntry.TABLE_NAME, null, contentValues);
    }

    public String getMainFocus() {
        Cursor cursor = mDB.query(SQLContract.MainFocusEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToLast()){
            return cursor.getString(1);
        } else {
            return null;
        }
    }

    public Map<String, String> getMainFocusAndDate() {
        Cursor cursor = mDB.query(SQLContract.MainFocusEntry.TABLE_NAME, null, null, null, null, null, null);
        Map<String, String> mainAndDate = new HashMap<>();
        if (cursor.moveToLast()){
            mainAndDate.put(SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, cursor.getString(1));
            mainAndDate.put(SQLContract.MainFocusEntry.COLUMN_NAME_DATE, cursor.getString(2));
        }
        return mainAndDate;
    }
}