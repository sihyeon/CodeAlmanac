package com.example.teamalmanac.codealmanac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.teamalmanac.codealmanac.adapter.TodoAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class LeftActivity extends AppCompatActivity {
    RecyclerView todoListview;
    LinearLayoutManager layoutManager;
    ArrayList<CheckBox> todos;
    TodoAdapter todoAdapter;
    DataManager dataManager;
    TextView hi;
    TextView whatIsYourMainfocus;
    TextView mainfocus_text;
    EditText mainfocus_edit;
    EditText todo_edit;
    Calendar c;
    LinearLayout todo_layout;
    LinearLayout mainfocus_layout;

    @Override
    protected void onResume() {
        super.onResume();
        refreshGreat();
        refreshMainfocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshGreat();
    }

    private void refreshGreat(){
        if(dataManager.getUserName()!=null) {
            hi.setText(setHelloMessage() + ", " + dataManager.getUserName());
        }
    }

    private String setHelloMessage(){
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if(4 <= hour && hour <= 10) {
            return "Good Morning";
        } else if(11 <= hour && hour <= 15) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }


    private void invisibleMode(){
        hi.setVisibility(View.INVISIBLE);
        whatIsYourMainfocus.setText("what is your name");
        todo_layout.setVisibility(View.INVISIBLE);
    }

    private void visibleMode() {
        hi.setText(setHelloMessage() + ", " + dataManager.getUserName());
        hi.setVisibility(View.VISIBLE);
        whatIsYourMainfocus.setText("What is your main focus for today?");
        todo_layout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left);
        dataManager = new DataManager(LeftActivity.this);
        hi = (TextView) findViewById(R.id.hi);
        mainfocus_edit = (EditText) findViewById(R.id.mainfocus_edittext);
        todo_edit = (EditText) findViewById(R.id.todo_edittext);
        mainfocus_text = (TextView) findViewById(R.id.mainfocus_text);
        whatIsYourMainfocus = (TextView) findViewById(R.id.whatIsYourMainfocus);
        c = Calendar.getInstance();
        todo_layout = (LinearLayout) findViewById(R.id.todo_box);
        mainfocus_layout = (LinearLayout) findViewById(R.id.mainfocus_layout);

        if(dataManager.getUserName()==null) {
            invisibleMode();
        }

        todoListview = (RecyclerView) findViewById(R.id.todo_listview);
        todoListview.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        todoListview.setLayoutManager(layoutManager);

        todos = todosToCheckboxes(dataManager.getTodos());
        todoAdapter = new TodoAdapter(todos);
        todoListview.setAdapter(todoAdapter);

        refreshMainfocus();
    }

    private ArrayList<CheckBox> todosToCheckboxes(ArrayList<String> st) {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        if(st.isEmpty()) return checkBoxes;
        for(int i = 0; i < st.size(); i++) {
            CheckBox ch = new CheckBox(this);
            Log.i("!~~~~~~~~~~~~~~", "todosToCheckboxes: " + st.get(i));
            ch.setText(st.get(i));
            checkBoxes.add(ch);
        }
        return checkBoxes;
    }

    private String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
        Date currentTime = new Date( );
        String dTime = formatter.format ( currentTime );
        return dTime;
    }

    private void refreshMainfocus(){
        Map<String, String> mainAndDate = dataManager.getMainFocusAndDate();
        if(mainAndDate.isEmpty()) return;
        String[] mainfocus_date = mainAndDate.get(SQLContract.MainFocusEntry.COLUMN_NAME_DATE).split("\\s+");
        String[] getTime_date = getTime().split("\\s+");
        if(mainfocus_date[0].compareTo(getTime_date[0]) > 0) {
            mainfocus_layout.setVisibility(View.VISIBLE);
            mainfocus_text.setVisibility(View.INVISIBLE);
        } else {
            mainfocus_text.setText(mainAndDate.get(SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS));
            mainfocus_layout.setVisibility(View.INVISIBLE);
            mainfocus_text.setVisibility(View.VISIBLE);
        }
    }

    public void onMainfocusClick(View view) {
        //user name is set
        if(!hi.isShown() && !mainfocus_edit.getText().toString().isEmpty()) {
            dataManager.setUserName(mainfocus_edit.getText().toString());
            mainfocus_edit.setText("");
            mainfocus_edit.clearFocus();
            visibleMode();
        }
        //mainfocus_edit is set
        else {
            if(mainfocus_edit.getText().toString().isEmpty()){
                Toast.makeText(LeftActivity.this, "Type your name..", Toast.LENGTH_SHORT).show();
            }
            dataManager.setMainFocus(mainfocus_edit.getText().toString(), getTime());
            refreshMainfocus();
        }
    }

    public void onTodoButtonClick(View view) {
        dataManager.setTodo(todo_edit.getText().toString(), getTime());
        todo_edit.setText("");
        todo_edit.clearFocus();
        todos.clear();
        todos.addAll(todosToCheckboxes(dataManager.getTodos()));
        todoAdapter.notifyDataSetChanged();
    }
}
