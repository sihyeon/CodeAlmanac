package com.example.teamalmanac.codealmanac;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.teamalmanac.codealmanac.adapter.LogAdapter;
import com.example.teamalmanac.codealmanac.bean.MainfocusDataType;
import com.example.teamalmanac.codealmanac.bean.TodoDataType;
import com.example.teamalmanac.codealmanac.database.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class TodoLogActivity extends AppCompatActivity {
    private RecyclerView mLogList;
    private DataManager mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_log);
        actionBarInit();
        mDB = DataManager.getSingletonInstance();
        mLogList = (RecyclerView)findViewById(R.id.recyclerview_log);
        setParentList();
    }

    private void actionBarInit(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        View customActionBar = LayoutInflater.from(this).inflate(R.layout.actionbar_log, null);
        AppCompatImageButton backButton = (AppCompatImageButton) customActionBar.findViewById (R.id.bar_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.bar_btn_back){
                    finish();
                }
            }
        });
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255,10,14,22)));
        actionBar.setCustomView(customActionBar);
    }

    private TreeSet<String> getDateTreeSet(){
        TreeSet<String> treeSet = new TreeSet<>();
        ArrayList<MainfocusDataType> mainfocusBeenList = mDB.getMainFocusList();
        ArrayList<TodoDataType> todoBeenList = mDB.getTodoList();
        for(MainfocusDataType temp : mainfocusBeenList) {
            treeSet.add(temp.getDate());
        }
        for(TodoDataType temp : todoBeenList){
            treeSet.add(temp.getDate());
        }
        return treeSet;
    }

    private void setParentList(){
        ArrayList<HashMap<String, Object>> LogListItem = new ArrayList<>();
        TreeSet<String> dateTreeSet = getDateTreeSet();

        for(String date : dateTreeSet){
            HashMap <String, Object> tempHash = new HashMap<>();
            tempHash.put("date", date);
            tempHash.put("mainfocus_adapter", getChildMainFocusListItem(date));
            tempHash.put("todo_adapter", getChildTodoListItem(date));
            LogListItem.add(tempHash);
        }

        mLogList.setAdapter(new LogAdapter(getApplicationContext(), LogListItem));
        mLogList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mLogList.setItemAnimator(new DefaultItemAnimator());
        //parent_text_date - String
        //parent_listview_mainfocus - ListView  *어댑터만 전송
        //parent_listview_todo - ListView   *어댑터만 전송
    }
    private Adapter getChildMainFocusListItem(String date){
        ArrayList<HashMap<String, Object>> adapterItem = new ArrayList<>();
        ArrayList<MainfocusDataType> mainfocusBeenList = mDB.selectionDateMainfocus(date);

        for (MainfocusDataType temp : mainfocusBeenList){
            HashMap <String, Object> tempHash = new HashMap<>();
            tempHash.put("content", temp.getMainfocus());
//            tempHash.put(SQLContract.MainFocusEntry.COLUMN_NAME_DATE, temp.getDate());
            adapterItem.add(tempHash);
        }

        SimpleAdapter mainfocusAdapter = new SimpleAdapter(this, adapterItem, R.layout.listview_log_child,
                new String[]{"content"},
                new int[]{R.id.child_text_content}){
            @Override
            public void setViewText(TextView v, String text) {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "NanumSquareR.ttf");
                v.setTypeface(typeface);
                v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                v.setText(text);
            }
        };
        return mainfocusAdapter;
    }
    private Adapter getChildTodoListItem(String date){
        ArrayList<HashMap<String, Object>> adapterItem = new ArrayList<>();
        ArrayList<TodoDataType> todoBeenList = mDB.selectionDateTodo(date);

        for (TodoDataType temp : todoBeenList){
            HashMap <String, Object> tempHash = new HashMap<>();
            tempHash.put("content", temp.getTodo());
//            tempHash.put(SQLContract.ToDoEntry.COLUMN_NAME_DATE, temp.getDate());
            adapterItem.add(tempHash);
        }
        SimpleAdapter todoAdapter = new SimpleAdapter(this, adapterItem, R.layout.listview_log_child,
                new String[]{"content"},
                new int[]{R.id.child_text_content}){
            @Override
            public void setViewText(TextView v, String text) {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "NanumSquareR.ttf");
                v.setTypeface(typeface);
                v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                v.setText(text);
            }
        };
        return todoAdapter;
    }
}
