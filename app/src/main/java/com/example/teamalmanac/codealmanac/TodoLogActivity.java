package com.example.teamalmanac.codealmanac;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.teamalmanac.codealmanac.bean.MainfocusDataType;
import com.example.teamalmanac.codealmanac.bean.TodoDataType;
import com.example.teamalmanac.codealmanac.database.DataManager;
import com.example.teamalmanac.codealmanac.database.SQLContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class TodoLogActivity extends Activity {
    private ListView mParentList;
    private DataManager mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_log);
        mDB = DataManager.getSingletonInstance(getApplicationContext());
        setParentList();
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
        ArrayList<HashMap<String, Object>> adapterItem = new ArrayList<>();
        TreeSet<String> dateTreeSet = getDateTreeSet();
        ListView parentList = (ListView)findViewById(R.id.listview_log);
        for(String date : dateTreeSet){
            HashMap <String, Object> tempHash = new HashMap<>();
            tempHash.put("parent_text_date", date);
            tempHash.put("parent_listview_mainfocus", getChildMainFocusListItem(date));
            adapterItem.add(tempHash);
        }
        SimpleAdapter parentAdapter = new SimpleAdapter(this, adapterItem, R.layout.listview_log_parent,
                new String[]{"parent_text_date", "parent_listview_mainfocus"},
                new int[]{R.id.parent_text_date, R.id.parent_listview_mainfocus});
        parentList.setAdapter(parentAdapter);
        //parent_text_date - String
        //parent_listview_mainfocus - ListView
        //parent_listview_todo - ListView
    }
    private ListView getChildMainFocusListItem(String date){
        ArrayList<HashMap<String, Object>> adapterItem = new ArrayList<>();
        ArrayList<MainfocusDataType> mainfocusBeenList = mDB.selectionDateMainfocus(date);
        ListView mainfocusListView;
        for (MainfocusDataType temp : mainfocusBeenList){
            HashMap <String, Object> tempHash = new HashMap<>();
            tempHash.put(SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, temp.getMainfocus());
            tempHash.put(SQLContract.MainFocusEntry.COLUMN_NAME_DATE, temp.getDate());
            adapterItem.add(tempHash);
        }
        SimpleAdapter mainfocusAdapter = new SimpleAdapter(this, adapterItem, R.layout.listview_log_child,
                new String[]{SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, SQLContract.MainFocusEntry.COLUMN_NAME_DATE},
                new int[]{R.id.child_text_content, R.id.child_text_date});
        mainfocusListView = (ListView)findViewById(R.id.parent_listview_mainfocus);
        mainfocusListView.setAdapter(mainfocusAdapter);
        return mainfocusListView;
    }

    private ListView setChildTodoListItem(String date){
        ArrayList<HashMap<String, Object>> adapterItem = new ArrayList<>();
        ArrayList<TodoDataType> todoBeenList = mDB.selectionDateTodo(date);
        ListView todoListView;
        for (TodoDataType temp : todoBeenList){
            HashMap <String, Object> tempHash = new HashMap<>();
            tempHash.put(SQLContract.ToDoEntry.COLUMN_NAME_TODO, temp.getTodo());
            tempHash.put(SQLContract.ToDoEntry.COLUMN_NAME_DATE, temp.getDate());
            adapterItem.add(tempHash);
        }
        SimpleAdapter todoAdapter = new SimpleAdapter(this, adapterItem, R.layout.listview_log_child,
                new String[]{SQLContract.ToDoEntry.COLUMN_NAME_TODO, SQLContract.ToDoEntry.COLUMN_NAME_DATE},
                new int[]{R.id.child_text_content, R.id.child_text_date});
        todoListView = (ListView)findViewById(R.id.parent_listview_todo);
        todoListView.setAdapter(todoAdapter);
        return todoListView;
    }
}
