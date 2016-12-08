package com.example.teamalmanac.codealmanac;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.teamalmanac.codealmanac.adapter.LogAdapter;
import com.example.teamalmanac.codealmanac.bean.MainfocusDataType;
import com.example.teamalmanac.codealmanac.bean.TodoDataType;
import com.example.teamalmanac.codealmanac.database.DataManager;
import com.example.teamalmanac.codealmanac.database.SQLContract;

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
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDB = DataManager.getSingletonInstance();
        mLogList = (RecyclerView)findViewById(R.id.recyclerview_log);
        setParentList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        mLogList.setAdapter(new LogAdapter(LogListItem));
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
            tempHash.put(SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, temp.getMainfocus());
            tempHash.put(SQLContract.MainFocusEntry.COLUMN_NAME_DATE, temp.getDate());
            adapterItem.add(tempHash);
        }

        SimpleAdapter mainfocusAdapter = new SimpleAdapter(this, adapterItem, R.layout.listview_log_child,
                new String[]{SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, SQLContract.MainFocusEntry.COLUMN_NAME_DATE},
                new int[]{R.id.child_text_content, R.id.child_text_date});
        return mainfocusAdapter;
    }

    private Adapter getChildTodoListItem(String date){
        ArrayList<HashMap<String, Object>> adapterItem = new ArrayList<>();
        ArrayList<TodoDataType> todoBeenList = mDB.selectionDateTodo(date);

        for (TodoDataType temp : todoBeenList){
            HashMap <String, Object> tempHash = new HashMap<>();
            tempHash.put(SQLContract.ToDoEntry.COLUMN_NAME_TODO, temp.getTodo());
            tempHash.put(SQLContract.ToDoEntry.COLUMN_NAME_DATE, temp.getDate());
            adapterItem.add(tempHash);
        }

        SimpleAdapter todoAdapter = new SimpleAdapter(this, adapterItem, R.layout.listview_log_child,
                new String[]{SQLContract.ToDoEntry.COLUMN_NAME_TODO, SQLContract.ToDoEntry.COLUMN_NAME_DATE},
                new int[]{R.id.child_text_content, R.id.child_text_date});
        return todoAdapter;
    }
}
