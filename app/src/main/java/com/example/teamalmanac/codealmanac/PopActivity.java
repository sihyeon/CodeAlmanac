package com.example.teamalmanac.codealmanac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PopActivity extends Activity{
    private Button popplus;
    private TextView name;
    private ImageView ic;
    private GridView selected;
    private Bitmap bitmap;
    private String APP_PATH;
    private String APP_ICON;

@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.popup);

    selected = (GridView)findViewById(R.id.appgrid);
    name = (TextView)findViewById(R.id.appname);
    ic = (ImageView)findViewById(R.id.appicon);

    //인텐트 받기
    Intent get_intent = getIntent();
    String APP_NAME = get_intent.getStringExtra("APP_NAME");
//    get_intent.getStringExtra("APP_PATH");
//    get_intent.getStringExtra("APP_ICON");

//    Log.v("전달", APP_NAME);
    //StringToBitMap(APP_ICON);
    //ic.setImageBitmap(bitmap);
//   name.setText(APP_NAME);

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

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
