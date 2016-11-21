package com.example.teamalmanac.codealmanac;

import android.graphics.Typeface;
import android.renderscript.Type;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.teamalmanac.codealmanac.R.id.digitalclock;
import static com.example.teamalmanac.codealmanac.R.id.textView;


public class UnlockActivity extends AppCompatActivity {
    private ImageView mSlideIcon;
    private RelativeLayout mMainLayout;
    private RelativeLayout mSlideLayout;
    private boolean mIsSlideIconTouch = false;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        mMainLayout = (RelativeLayout)findViewById(R.id.activity_main);
        mSlideLayout = (RelativeLayout)findViewById(R.id.slideLayout);
        mSlideIcon = (ImageView)findViewById(R.id.imageView);
        tv = (TextView)findViewById(textView);
        Log.d("테스트", mMainLayout.getPaddingLeft()+"");

        mSlideLayout.setOnTouchListener(mSlideTouchListener);

        //Digital Clock FONT asset
        TextClock digitalClock = (TextClock) findViewById(R.id.digitalclock);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"digital-7.ttf");
        digitalClock.setTypeface(typeface);

        //datetime
        TextView dt = (TextView)findViewById(R.id.datetime);
        String format = new String("MM 월 dd일 EEEE");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
        dt.setText(sdf.format(new Date()));
        Typeface type = Typeface.createFromAsset(getAssets(),"Spoqa_Han_Sans_Regular.ttf");
        dt.setTypeface(type);

    }

    private View.OnTouchListener mSlideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(mIsSlideIconTouch) {
                if (mSlideIcon.getX() <= 0) {
                    //왼쪽
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mSlideLayout = (RelativeLayout) findViewById(R.id.slideLayout);
                        mSlideIcon.setX(mSlideLayout.getWidth() / 2 - mSlideIcon.getWidth() / 2);
                        Toast.makeText(UnlockActivity.this, "왼쪽으로 슬라이드", Toast.LENGTH_LONG).show();
                        mIsSlideIconTouch = false;
                    }
                } else if (mSlideIcon.getX() >= mSlideLayout.getWidth() - mSlideIcon.getWidth()) {
                    //오른쪽
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mSlideLayout = (RelativeLayout) findViewById(R.id.slideLayout);
                        mSlideIcon.setX(mSlideLayout.getWidth() / 2 - mSlideIcon.getWidth() / 2);
                        Toast.makeText(UnlockActivity.this, "오른쪽으로 슬라이드", Toast.LENGTH_LONG).show();
                        mIsSlideIconTouch = false;
                    }
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mSlideLayout = (RelativeLayout) findViewById(R.id.slideLayout);
                        mSlideIcon.setX(mSlideLayout.getWidth() / 2 - mSlideIcon.getWidth() / 2);
                        mIsSlideIconTouch = false;
                    } else {
                        mSlideIcon.setX(motionEvent.getX() - mSlideIcon.getWidth() / 2);
                    }
                }
            } else {
                if (mSlideIcon.getX() + mMainLayout.getPaddingLeft() - mSlideIcon.getWidth() / 2 <= motionEvent.getX()
                        && motionEvent.getX() <= mSlideIcon.getX() + mMainLayout.getPaddingLeft() + mSlideIcon.getWidth() / 2) {
                    mSlideIcon.setX(motionEvent.getX() - mSlideIcon.getWidth() / 2);
                    mIsSlideIconTouch = true;
                }
            }
            tv.setText("layout: " + motionEvent.getX() + " image: " + mSlideIcon.getX() + " width: " + mSlideIcon.getWidth());
            return true;
        }
    };
}
