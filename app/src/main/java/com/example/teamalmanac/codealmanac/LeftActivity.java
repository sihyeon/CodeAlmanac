package com.example.teamalmanac.codealmanac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class LeftActivity extends AppCompatActivity {
    RecyclerView todoListview;
    LinearLayoutManager layoutManager;
    ArrayList<CheckBox> todos;
    TodoAdapter todoAdapter;
    DataManager dataManager;
    TextView hi;
    TextView up_mainfocus_text;
    EditText mainfocus;
    Calendar c;
    LinearLayout todo_layout;

    @Override
    protected void onResume() {
        super.onResume();
        if(dataManager.getUserName()==null) {
            return;
        } else {
            hi.setText(setHelloMessage() + ", " + dataManager.getUserName());
        }
    }

    private String setHelloMessage(){
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if(hour >= 4 && hour <= 10) {
            return "Good Morning";
        } else if(hour >= 11 && hour >= 5) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }


    private void invisibleMode(){
        hi.setVisibility(View.INVISIBLE);
        up_mainfocus_text.setText("what is your name");
        todo_layout.setVisibility(View.INVISIBLE);
    }

    private void visibleMode() {
        hi.setText(setHelloMessage() + ", " + dataManager.getUserName());
        hi.setVisibility(View.VISIBLE);
        up_mainfocus_text.setText("What is your main focus for today?");
        todo_layout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left);
        dataManager = new DataManager(LeftActivity.this);
        hi = (TextView) findViewById(R.id.hi);
        mainfocus = (EditText) findViewById(R.id.mainfocus_edittext);
        up_mainfocus_text = (TextView) findViewById(R.id.up_mainfocus_text);
        c = Calendar.getInstance();
        todo_layout = (LinearLayout) findViewById(R.id.todo_box);

        if(dataManager.getUserName()==null) {
            invisibleMode();
        }

        todoListview = (RecyclerView) findViewById(R.id.todo_listview);

        todoListview.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        todoListview.setLayoutManager(layoutManager);

        todos = new ArrayList<>();
        todoAdapter = new TodoAdapter(todos);
        todoListview.setAdapter(todoAdapter);

        CheckBox ch = new CheckBox(this);
        ch.setText("test");
        //test
        //test
        //test
        //test
        todos.add(ch);
        todos.add(ch);
        todos.add(ch);
    }

    public void onMainfocusClick(View view) {
        if(!hi.isShown() && !mainfocus.getText().toString().isEmpty()) {
            dataManager.setUserName(mainfocus.getText().toString());
            mainfocus.setText("");
            mainfocus.clearFocus();
            visibleMode();
        } else {
            if(mainfocus.getText().toString().isEmpty()){
                Toast.makeText(LeftActivity.this, "Type your name..", Toast.LENGTH_SHORT).show();
            }

            //TODO add Mainfocus to table

        }
    }

    public void onTodoButtonClick(View view) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
        Date currentTime = new Date( );
        String dTime = formatter.format ( currentTime );
        dataManager.setTodo(mainfocus.getText().toString(), dTime);
        mainfocus.setText("");
        mainfocus.clearFocus();
        todoAdapter.notifyDataSetChanged();
    }
}
