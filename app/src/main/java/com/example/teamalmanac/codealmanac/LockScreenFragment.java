package com.example.teamalmanac.codealmanac;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LockScreenFragment extends Fragment {
    private DataManager mDB = null;
    private TextView mainFocusText;

    public LockScreenFragment() {
        // Required empty public constructor
    }

    public static LockScreenFragment newInstance() {
        LockScreenFragment fragment = new LockScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDB = DataManager.getSingletonInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lock_screen, container, false);

        //Digital Clock FONT asset
        TextClock digitalClock = (TextClock) rootView.findViewById(R.id.digitalclock);
        Typeface typeface = Typeface.createFromAsset(TabActivity.getMainContext().getAssets(),"FRABK.TTF");
        digitalClock.setTypeface(typeface);

        //datetime
        TextView dt = (TextView)rootView.findViewById(R.id.datetime);
        String format = new String("MM . dd EEEE");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
        dt.setText(sdf.format(new Date()));
        Typeface type = Typeface.createFromAsset(TabActivity.getMainContext().getAssets(),"NanumSquareB.ttf");
        dt.setTypeface(type);

        TextView mainfocus_text= (TextView) rootView.findViewById(R.id.mainfocus_text);
        Typeface typetext = Typeface.createFromAsset(TabActivity.getMainContext().getAssets(),"NanumSquareB.ttf");
        mainfocus_text.setTypeface(typetext);

        setMainFocus(rootView);

        return rootView;
    }

    //메인 포커스 세팅 (미완성 / 현재 swipe 작업 중)
    private void setMainFocus(View rootView){
        String userName = mDB.getUserName();
        String mainFocus = mDB.getMainFocus();
        String fullText = "";
        TextView mainFocusText = (TextView)rootView.findViewById(R.id.mainfocus_text);
        if(userName != null){
            fullText += "Good evening, " + userName + "\n\n";
        } else {
            fullText += "Name is nothing\n\n";
        }
        if(mainFocus != null){
            fullText += "Today : " + mainFocus;
        } else {
            fullText += "MainFocus is nothing";
        }
        mainFocusText.setText(fullText);
    }

}
