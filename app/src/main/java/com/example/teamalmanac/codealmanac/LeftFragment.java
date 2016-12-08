package com.example.teamalmanac.codealmanac;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamalmanac.codealmanac.adapter.TodoAdapter;
import com.example.teamalmanac.codealmanac.bean.MainfocusDataType;
import com.example.teamalmanac.codealmanac.bean.TodoDataType;
import com.example.teamalmanac.codealmanac.database.DataManager;
import com.example.teamalmanac.codealmanac.database.SQLContract;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class LeftFragment extends Fragment {
    private DataManager mDb;

    // ui layout elements
    private LinearLayout whatisyourname_layout;
    private LinearLayout whatisyourmainfocusEdit_layout;
    private LinearLayout whatisyourmainfocus_layout;
    private LinearLayout mainfocus_layout;
    private LinearLayout todo_layout;
    private RelativeLayout top_icons_layout;
    private GridView gridView;
    private RelativeLayout poplayout;

    // text
    private TextView greet;
    private CheckBox mainfocus;
    private TextView userNameText;
    private TextView whatisyourname;
    private TextView whatIsYourMainfocus;
    private TextView todo_title;
    private TextView today;
    private TextView poptitle;

    // editText
    private EditText todo_editt;
    private EditText name_editt;
    private EditText mainfocus_editt;

    // listview
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView todo_listview;
    private TodoAdapter todoAdapter;
    private List<TodoDataType> todos;

    // button
    private Button name_button;
    private Button mainfocus_button;
    private Button mainfocus_deletebutton;
    private Button todo_button;
    private ImageView logo_icn;
    private Button calendar;
    private Button appdesk;
    private Button popplus;


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
        mDb = DataManager.getSingletonInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        greet.setText(setGreetingMessage() + ", ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_left, container, false);
        findViewByIds(rootView);

        setFontAndGravity();
        setNameComponentListener();
        setMainfocusComponentListener();
        setTodoComponentListener();
        setCalendarListener();
        Popup();
        initTodoList();
        initLayout();

        return rootView;
    }

    private void findViewByIds(View rootView){
        whatisyourname_layout = (LinearLayout) rootView.findViewById(R.id.whatIsYourName_layout);
        whatisyourmainfocusEdit_layout = (LinearLayout) rootView.findViewById(R.id.whatIsYourMainfocusEdit_layout);
        whatisyourmainfocus_layout = (LinearLayout) rootView.findViewById(R.id.whatIsYourMainfocus_layout);
        mainfocus_layout = (LinearLayout) rootView.findViewById(R.id.mainfocus_layout);
        top_icons_layout = (RelativeLayout)rootView.findViewById(R.id.top_icons_layout);

        todo_layout = (LinearLayout) rootView.findViewById(R.id.todo_layout);
        todo_title = (TextView) rootView.findViewById(R.id.todo_title);
        todo_editt = (EditText) rootView.findViewById(R.id.todo_edittext);

        userNameText = (TextView) rootView.findViewById(R.id.text_user_name);
        logo_icn = (ImageView) rootView.findViewById(R.id.logo_icn);

        greet = (TextView) rootView.findViewById(R.id.greet);

        todo_listview = (RecyclerView) rootView.findViewById(R.id.todo_listview);
        todo_listview.setHasFixedSize(true);

        //WHAT IS YOUR NAME 부분 FONT 설정
        whatisyourname = (TextView) rootView.findViewById(R.id.whatIsYourName);
        whatIsYourMainfocus = (TextView) rootView.findViewById(R.id.whatIsYourMainfocus);
        mainfocus = (CheckBox) rootView.findViewById(R.id.mainfocus_text2);
        today = (TextView) rootView.findViewById(R.id.today_text2);

        name_editt = (EditText) rootView.findViewById(R.id.name_edittext);
        name_button = (Button) rootView.findViewById(R.id.name_button);
        mainfocus_button = (Button) rootView.findViewById(R.id.mainfocus_button);
        mainfocus_deletebutton = (Button) rootView.findViewById(R.id.mainfocus_deletebutton);
        mainfocus_editt = (EditText) rootView.findViewById(R.id.mainfocus_edittext);
        todo_button = (Button) rootView.findViewById(R.id.todo_button);
        todo_editt = (EditText) rootView.findViewById(R.id.todo_edittext);

        //앱 서랍, 캘린더 버튼
        poplayout = (RelativeLayout)rootView.findViewById(R.id.popup_layout);
        poptitle = (TextView)rootView.findViewById(R.id.pop_title);
        popplus = (Button)rootView.findViewById(R.id.pop_btn);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        appdesk = (Button)rootView.findViewById(R.id.appdesk);
        calendar = (Button)rootView.findViewById(R.id.calendar);

        linearLayoutManager = new LinearLayoutManager(this.getContext());
    }



    //앱 서랍 아이콘 클릭 시 액티비티 팝업
    private void Popup(){
        appdesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Pop_intent = new Intent(getActivity(),PopActivity.class);
                startActivity(Pop_intent);
            }
        });

    }

    private void setFontAndGravity() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "FranklinGothic-MediumCond.TTF");
        whatisyourname.setTypeface(typeface);
        whatIsYourMainfocus.setTypeface(typeface);
        userNameText.setTypeface(typeface);
        greet.setTypeface(typeface);
        todo_title.setTypeface(typeface);
        todo_editt.setTypeface(typeface);
        mainfocus.setTypeface(typeface);

        int color = Color.parseColor("#C0C0C0");
        todo_editt.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        Typeface typeface1 = Typeface.createFromAsset(getContext().getAssets(), "FranklinGothic-Demi.TTF");
        today.setTypeface(typeface1);
        greet.setGravity(Gravity.CENTER);
        userNameText.setGravity(Gravity.CENTER);
        mainfocus_layout.setGravity(Gravity.CENTER);
        whatIsYourMainfocus.setGravity(Gravity.CENTER);
        whatisyourmainfocus_layout.setGravity(Gravity.CENTER);
        whatisyourmainfocusEdit_layout.setGravity(Gravity.CENTER);


    }

    private void setNameComponentListener() {
        name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_editt.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(view.getContext(), "fill the name...", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDb.setUserName(name);
                notifyNameAdded();
                //keyboard down when typing is finished.
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //keyboard down when type enter key.
        name_editt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(getView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;

                }
                return false;
            }
        });
    }

    private void setCalendarListener(){
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean installed = appInstalledOrNot("com.google.android.calendar");
                if(installed) {
                    //This intent will help you to launch if the package is already installed
                    Intent LaunchIntent = new Intent();
                    LaunchIntent.setPackage("com.google.android.calendar");
                    LaunchIntent.setAction(Intent.ACTION_MAIN);
                    LaunchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(LaunchIntent);

                } else {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.calendar");
                    Intent web = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(web);
                }
            }
        });
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private void setMainfocusComponentListener() {
        mainfocus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mainfocus = mainfocus_editt.getText().toString();
                if (mainfocus.isEmpty()) {
                    Toast.makeText(view.getContext(), "fill the mainfocus...", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = SQLContract.convertDateToString(Calendar.getInstance().getTime());

                mDb.setMainFocus(mainfocus, time);
                notifyMainfocusAdded();
                //keyboard down when typing is finished.
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //keyboard down when type enter key.
        mainfocus_editt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(getView().getApplicationWindowToken() , InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;

                }
                return false;
            }
        });

        mainfocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //exchange state. (visible -> invisible // invisible -> visible)
                if(mainfocus_deletebutton.getVisibility() == View.INVISIBLE) {
                    mainfocus_deletebutton.setVisibility(View.VISIBLE);
                    mDb.updateMainFocusButtonVisibility(mDb.getMainFocusInfo().getDate(), String.valueOf(View.VISIBLE));
                }
                else {
                    mainfocus_deletebutton.setVisibility(View.INVISIBLE);
                    mDb.updateMainFocusButtonVisibility(mDb.getMainFocusInfo().getDate(), String.valueOf(View.INVISIBLE));
                }
                mainfocus.setPaintFlags(mainfocus.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        mainfocus_deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mDb.deleteMainFocus();
                mainfocus.setText("");
                mainfocus.setPaintFlags(mainfocus.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                view.setVisibility(View.GONE);
                mainfocus.setChecked(false);
                mainfocus_layout.setVisibility(View.GONE);
                whatIsYourMainfocus.setVisibility(View.VISIBLE);
                whatisyourmainfocus_layout.setVisibility(View.VISIBLE);
                whatisyourmainfocusEdit_layout.setVisibility(View.VISIBLE);
                //TODO
            }
        });
    }

    private void setTodoComponentListener() {
        todo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todo = todo_editt.getText().toString();
                if (todo.isEmpty()) {
                    Toast.makeText(view.getContext(), "fill the todo...", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDb.setTodo(todo, SQLContract.convertDateToString(Calendar.getInstance().getTime()));
                notifyTodoDataChanged(todo);
                todo_editt.setText("");
                todo_editt.clearFocus();
                //keyboard down when typing is finished.
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //keyboard down when type enter key.
        todo_editt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(getView().getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;

                }
                return false;
            }
        });
    }

    private void initTodoList() {
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        todo_listview.setLayoutManager(linearLayoutManager);

        todos = mDb.getShowingTodos();
        todoAdapter = new TodoAdapter(todos);
        todo_listview.setAdapter(todoAdapter);
    }

    private String setGreetingMessage() {
        int presentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (4 <= presentHour && presentHour <= 11) return "Good Morning";
        else if (12 <= presentHour && presentHour <= 18) return "Good Afternoon";
        else return "Good Evening";
    }

    private void notifyNameAdded() {
        whatisyourname_layout.setVisibility(View.GONE);
        greet.setText(setGreetingMessage() + ", ");
        userNameText.setText(mDb.getUserName());
        whatisyourmainfocus_layout.setVisibility(View.VISIBLE);
        today.setVisibility(View.GONE);
        todo_layout.setVisibility(View.VISIBLE);
        logo_icn.setVisibility(View.GONE);
    }

    private void notifyMainfocusAdded() {
        mainfocus_editt.setText("");
        mainfocus_deletebutton.setVisibility(View.INVISIBLE);
        whatisyourmainfocusEdit_layout.setVisibility(View.GONE);
        whatIsYourMainfocus.setVisibility(View.GONE);
        mainfocus_layout.setVisibility(View.VISIBLE);
        mainfocus.setText(mDb.getMainFocusInfo().getMainfocus());
    }

    private void initLayout() {
        //name is available
        if (isNameAvailable()) {
            whatisyourname_layout.setVisibility(View.GONE);
            logo_icn.setVisibility(View.GONE);
            //greet.setText(setHelloMessage() + ",");
            greet.setText(setGreetingMessage() + ", ");
            userNameText.setText(mDb.getUserName());
            whatisyourmainfocus_layout.setVisibility(View.VISIBLE);
            todo_layout.setVisibility(View.VISIBLE);
        }
        //if name is not available, get name through view
        else {
            whatisyourname_layout.setVisibility(View.VISIBLE);
            whatisyourmainfocus_layout.setVisibility(View.GONE);
            todo_layout.setVisibility(View.GONE);
            mainfocus_layout.setVisibility(View.GONE);
            return;
        }

        MainfocusDataType mainfocusData = mDb.getMainFocusInfo();
        //mainfocus is available
        if (isMainFocusAvailable() && mainfocusData.getMainfocus()!=null) {
            //TODO set time out
            whatIsYourMainfocus.setVisibility(View.GONE);
            whatisyourmainfocusEdit_layout.setVisibility(View.GONE);
            mainfocus.setText(mDb.getMainFocusInfo().getMainfocus());
            //mainfocus is checked (confirm from mdb)
            if(mainfocusData.getButton_visibility().equals(String.valueOf(View.VISIBLE))) {
                mainfocus.setChecked(true);
                mainfocus.setPaintFlags(mainfocus.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mainfocus_deletebutton.setVisibility(View.VISIBLE);
            }
        } else {
            mainfocus_layout.setVisibility(View.GONE);
            whatIsYourMainfocus.setVisibility(View.VISIBLE);
            whatisyourmainfocusEdit_layout.setVisibility(View.VISIBLE);
        }
    }

    public void notifyTodoDataChanged(String todo) {
        todos.add(new TodoDataType(todo, SQLContract.convertDateToString(Calendar.getInstance().getTime())));
        todoAdapter.notifyDataSetChanged();
    }

    public boolean isNameAvailable() {
        return mDb.getUserName() != null;
    }

    public boolean isMainFocusAvailable() {
        return mDb.getMainFocusInfo() != null;
    }


}