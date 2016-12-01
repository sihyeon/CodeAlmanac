package com.example.teamalmanac.codealmanac;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TabActivity extends AppCompatActivity {
    //마지막 페이지에서 스와이핑하는걸 검출하기 위한 변수
    private int mCounterPageScroll;
    //context를 담기 위한 변수
    private static Context mMainContext;
    //Fragment 수
    private static final int FRAGMENT_TOTAL_NUMBER = 2;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        mMainContext = getApplicationContext();
        DataManager.getSingletonInstance(); //액티비티에서 싱글톤 미리생성

        /*  FLAG_SHOW_WHEN_LOCKED = 잠금화면 위로 액티비티 실행
            FLAG_DISMISS_KEYGUARD = 키 가드 해제 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(mListener);
    }

    public static Context getMainContext(){
        return mMainContext;
    }

    //뷰가 변경될 때마다 불리는 리스너 >> 마지막 페이지때 종료되게하려고 추가하였음
    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if ( position == FRAGMENT_TOTAL_NUMBER-1 && positionOffset == 0 ) {
                if( mCounterPageScroll != 0 ) finish();
                mCounterPageScroll++;
            } else {
                mCounterPageScroll = 0;
            }
        }
        @Override
        public void onPageSelected(int position) {}
        @Override
        public void onPageScrollStateChanged(int state) {}
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_HOME:
            case KeyEvent.KEYCODE_ESCAPE:
                return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 1){
                return LockScreenFragment.newInstance();
            }
            if(position == 0) {
                return LeftFragment.newInstance();
            }
            return LockScreenFragment.newInstance();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
           }
            return null;
        }
    }
}
