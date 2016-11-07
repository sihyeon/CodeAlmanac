package com.example.choijaeung.codealmanac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView mSlideIcon;
    private RelativeLayout mMainLayout;
    private RelativeLayout mSlideLayout;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainLayout = (RelativeLayout)findViewById(R.id.activity_main);
        mSlideLayout = (RelativeLayout)findViewById(R.id.slideLayout);
        mSlideIcon = (ImageView)findViewById(R.id.imageView);
        tv = (TextView)findViewById(R.id.textView);
        Log.d("테스트", mMainLayout.getPaddingLeft()+"");

        mSlideLayout.setOnTouchListener(mSlideTouchListener);
    }
    private View.OnTouchListener mSlideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(mSlideIcon.getX() <= 0){
                //왼쪽
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mSlideLayout = (RelativeLayout) findViewById(R.id.slideLayout);
                    mSlideIcon.setX(mSlideLayout.getWidth() / 2 - mSlideIcon.getWidth() / 2);
                    Toast.makeText(MainActivity.this, "왼쪽으로 슬라이드", Toast.LENGTH_LONG).show();
                }
            } else if (mSlideIcon.getX() >= mSlideLayout.getWidth() - mSlideIcon.getWidth()){
                //오른쪽
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSlideLayout = (RelativeLayout)findViewById(R.id.slideLayout);
                    mSlideIcon.setX(mSlideLayout.getWidth()/2 - mSlideIcon.getWidth()/2);
                    Toast.makeText(MainActivity.this, "오른쪽으로 슬라이드", Toast.LENGTH_LONG).show();
                }
            } else {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mSlideLayout = (RelativeLayout)findViewById(R.id.slideLayout);
                    mSlideIcon.setX(mSlideLayout.getWidth()/2 - mSlideIcon.getWidth()/2);
                } else {
                    if(mSlideIcon.getX() + mMainLayout.getPaddingLeft() - mSlideIcon.getWidth()/2 <= motionEvent.getX()
                            && motionEvent.getX() <= mSlideIcon.getX() + mMainLayout.getPaddingLeft() + mSlideIcon.getWidth()/2){
                        mSlideIcon.setX(motionEvent.getX() - mSlideIcon.getWidth()/2);
                    }
                }
            }
            tv.setText("layout: " + motionEvent.getX() + " image: " + mSlideIcon.getX() + " width: " + mSlideIcon.getWidth());
            return true;
        }
    };
}
