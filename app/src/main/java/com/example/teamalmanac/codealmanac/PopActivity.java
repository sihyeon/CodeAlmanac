package com.example.teamalmanac.codealmanac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PopActivity extends Activity{
    private Button popplus;
@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.popup);



    // 전체 앱 불러오기
    popplus = (Button)this.findViewById(R.id.pop_btn);
        popplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent grid_intent = new Intent(getApplicationContext(),AppInfo.class);
                startActivity(grid_intent);
            }
        });

    }
}
