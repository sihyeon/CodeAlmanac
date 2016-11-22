package com.example.teamalmanac.codealmanac;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        TextView tv = (TextView)findViewById(R.id.testView);

        DataManager dm = new DataManager(getApplicationContext());
//        dm.setUserName("ChoiJaung?");
//        dm.setUserName("hahahaha");
        tv.setText(dm.getUserName());
    }

    public static Context getContext(){
        return mContext;
    }
}
