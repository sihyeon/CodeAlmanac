package com.example.teamalmanac.codealmanac;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class LockScreenFragment extends Fragment {
    private DataManager mDB = null;
    private Calendar mCalendar;


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
        mCalendar = Calendar.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lock_screen, container, false);
        //배경 이미지
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.layout);
        relativeLayout.setBackground(rootView.getResources().getDrawable(R.drawable.main,TabActivity.getMainContext().getTheme()));

        //Digital Clock FONT asset
        TextClock digitalClock = (TextClock) rootView.findViewById(R.id.digital_clock);
        Typeface typeface = Typeface.createFromAsset(TabActivity.getMainContext().getAssets(), "LSSM.TTF");
        digitalClock.setTypeface(typeface);

        //datetime
        TextView dt = (TextView)rootView.findViewById(R.id.datetime);
        String format = new String("MM .dd  EEEE");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        dt.setText(sdf.format(new Date()));
        Typeface type = Typeface.createFromAsset(TabActivity.getMainContext().getAssets(), "LSSM.TTF");
        dt.setTypeface(type);

        //화면 이동 메세지 폰트
        TextView mv = (TextView)rootView.findViewById(R.id.textView);
        Typeface mvtype = Typeface.createFromAsset(TabActivity.getMainContext().getAssets(),"NanumSquareR.ttf");
        mv.setTypeface(mvtype);

        //인사말 밑 mainfocus
        setMainText(rootView);

        return (rootView);
    }

    //GPS 정보 가져오기
    private void setGeoLocation(){

    }


    //인사말 설정
    private String setGreetingMessage(){
        int presentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        //아침
        if(4 <= presentHour && presentHour <= 11) return "Good Morning";
            //오후(점심)
        else if(12 <= presentHour && presentHour <= 18) return "Good Afternoon";
            //저녁
        else return "Good Evening";
    }

    //메인 포커스 세팅 (메인포커스 없을 때, 사용자 이름 없을 때의 논의 필요)
    private void setMainText(View rootView){

        String userName = mDB.getUserName();
        String mainFocus = mDB.getMainFocus();
        String greetingMessage = "";
        String mainfocusMessage = "";

        TextView todayText = (TextView)rootView.findViewById(R.id.text_today);
        TextView greetingText = (TextView)rootView.findViewById(R.id.text_greeting);
        TextView mainfocusText = (TextView)rootView.findViewById(R.id.text_mainfocus);


        if(userName != null){
            greetingMessage += setGreetingMessage() + ", " + userName;
        } else {
            greetingMessage += "Name is noting";
        }
        if(mainFocus != null){
            mainfocusMessage += mainFocus;
        } else {
            mainfocusMessage += "MainFocus is noting";
        }
        //메인포커스 글꼴
        Typeface textType = Typeface.createFromAsset(TabActivity.getMainContext().getAssets(),"FRAMDCN.TTF");
        greetingText.setTypeface(textType);
        mainfocusText.setTypeface(textType);

        //Good morning,~ 부분의 글꼴
        Typeface greetType = Typeface.createFromAsset(TabActivity.getMainContext().getAssets(),"FRAMDCN.TTF");
        greetingText.setTypeface(greetType);
        mainfocusText.setTypeface(greetType);

        greetingText.setText(greetingMessage);
        mainfocusText.setText(mainfocusMessage);

        //today 부분의 글꼴
        Typeface todayType = Typeface.createFromAsset(TabActivity.getMainContext().getAssets(),"FRADM.TTF");
        todayText.setTypeface(todayType );
        todayText.setTypeface(todayType );

    }
}
