package com.example.teamalmanac.codealmanac;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamalmanac.codealmanac.adapter.TodoAdapter;

import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LeftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeftFragment extends Fragment {
    private DataManager mDb;
//    private OnFragmentInteractionListener mListener;

    // ui layout elements
    private LinearLayout whatisyourname_layout;
    private LinearLayout whatisyourmainfocusEdit_layout;
    private LinearLayout whatisyourmainfocus_layout;
    private LinearLayout todo_layout;

    // text
    private TextView greet;
    private TextView mainfocus;
    private TextView userNameText;
    private TextView whatisyourname;
    private TextView whatIsYourMainfocus;
    private TextView todolist_text;

    // listview
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView todo_listview;
    private TodoAdapter todoAdapter;
    private List<String> todos;

    // button
    private Button name_button;
    private Button mainfocus_button;
    private Button todo_button;
    private ImageView logo_icn;

    public LeftFragment() {
        // Required empty public constructor

    }

    public static LeftFragment newInstance() {
        LeftFragment fragment = new LeftFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = DataManager.getSingletonInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_left, container, false);

        whatisyourname_layout = (LinearLayout) rootView.findViewById(R.id.whatIsYourName_layout);
        whatisyourmainfocusEdit_layout = (LinearLayout) rootView.findViewById(R.id.whatIsYourMainfocusEdit_layout);
        whatisyourmainfocus_layout = (LinearLayout) rootView.findViewById(R.id.whatIsYourMainfocus_layout);
        todo_layout = (LinearLayout) rootView.findViewById(R.id.todo_layout);

        userNameText = (TextView) rootView.findViewById(R.id.text_user_name);
        logo_icn = (ImageView)rootView.findViewById(R.id.logo_icn);

        greet = (TextView) rootView.findViewById(R.id.greet);

        todo_listview = (RecyclerView) rootView.findViewById(R.id.todo_listview);
        todo_listview.setHasFixedSize(true);

        //WHAT IS YOUR NAME 부분 FONT 설정
        whatisyourname = (TextView)rootView.findViewById(R.id.whatIsYourName);
        whatIsYourMainfocus = (TextView)rootView.findViewById(R.id.whatIsYourMainfocus);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "FRAMDCN.TTF");
        whatisyourname.setTypeface(typeface);
        whatIsYourMainfocus.setTypeface(typeface);
        userNameText.setTypeface(typeface);
        greet.setTypeface(typeface);

        greet.setGravity(Gravity.CENTER);
        userNameText.setGravity(Gravity.CENTER);
        whatIsYourMainfocus.setGravity(Gravity.CENTER);
        whatisyourmainfocus_layout.setGravity(Gravity.CENTER);
        whatisyourmainfocusEdit_layout.setGravity(Gravity.CENTER);

        name_button = (Button) rootView.findViewById(R.id.name_button);
        name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name_edit = (EditText) getView().findViewById(R.id.name_edittext);
                String name = name_edit.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(view.getContext(), "fill the name...", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDb.setUserName(name);
                nameAdded();
            }
        });

        mainfocus_button = (Button)rootView.findViewById(R.id.mainfocus_button);
        mainfocus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mainfocus_edit = (EditText) getView().findViewById(R.id.mainfocus_edittext);
                String mainfocus = mainfocus_edit.getText().toString();
                if (mainfocus.isEmpty()) {
                    Toast.makeText(view.getContext(), "fill the mainfocus...", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDb.setMainFocus(mainfocus, Calendar.getInstance().getTime().toString());
                mainFocusAdded();
            }
        });

        todo_button = (Button) rootView.findViewById(R.id.todo_botton);

        todo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText todo_edit = (EditText) getView().findViewById(R.id.todo_edittext);

                String todo = todo_edit.getText().toString();
                if (todo.isEmpty()) {
                    Toast.makeText(view.getContext(), "fill the todo...", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDb.setTodo(todo, Calendar.getInstance().getTime().toString());
                notifyTodoDataChanged(todo);
                todo_edit.setText("");
                todo_edit.clearFocus();
            }
        });

        initTodoList();
        initlayout();

        return rootView;
    }

    private void initTodoList() {
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        todo_listview.setLayoutManager(linearLayoutManager);

        todos = mDb.getTodos();
        todoAdapter = new TodoAdapter(todos);
        todo_listview.setAdapter(todoAdapter);
    }

    private String setHelloMessage() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (4 <= hour && hour <= 10) {
            return "Good Morning";
        } else if (11 <= hour && hour <= 15) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }

    private void nameAdded() {
        whatisyourname_layout.setVisibility(View.GONE);
        greet.setText(setHelloMessage() + ", ");
        userNameText.setText(mDb.getUserName());
        whatisyourmainfocus_layout.setVisibility(View.VISIBLE);
        todo_layout.setVisibility(View.VISIBLE);
        logo_icn.setVisibility(View.GONE);
    }

    private void mainFocusAdded() {
        whatisyourmainfocusEdit_layout.setVisibility(View.GONE);
        mainfocus.setText(mDb.getMainFocus());
    }

    private void initlayout() {
        if (isNameAvailable()) {
            whatisyourname_layout.setVisibility(View.GONE);
            logo_icn.setVisibility(View.GONE);
            greet.setText(setHelloMessage() + ", " + mDb.getUserName());
            whatisyourmainfocus_layout.setVisibility(View.VISIBLE);
            todo_layout.setVisibility(View.VISIBLE);
        } else {
            whatisyourname_layout.setVisibility(View.VISIBLE);
            whatisyourmainfocus_layout.setVisibility(View.GONE);
            todo_layout.setVisibility(View.GONE);
            return;
        }

        if (isMainFocusAvailable() && !mDb.getMainFocus().isEmpty()) {
            //TODO set time out
            whatisyourmainfocusEdit_layout.setVisibility(View.GONE);
            mainfocus.setVisibility(View.VISIBLE);
            mainfocus.setText(mDb.getMainFocus());
            return;
        } else {
            whatisyourmainfocusEdit_layout.setVisibility(View.VISIBLE);
        }
    }

    public void notifyTodoDataChanged(String todo) {
        todos.add(todo);
        todoAdapter.notifyDataSetChanged();
    }

    public boolean isNameAvailable() {
        return mDb.getUserName() != null;
    }

    public boolean isMainFocusAvailable() {
        return mDb.getMainFocus() != null;
    }
}