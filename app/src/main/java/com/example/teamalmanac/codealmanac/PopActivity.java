package com.example.teamalmanac.codealmanac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopActivity extends Activity {
    private final int APPINFO_REQUESTCODE = 1;
    private Button popplus;
    private GridView selected;
    private Context context;

    String receivedIcon;
    String receivedName;
    String receivedPath;

    Activity act = this;
    public List<ResolveInfo> MyAppList;
    private TextView name;
    private ImageView ic;

    private PackageManager myPackageManager;
    ResolveInfo resolveInfo;
    ResolveInfo clickedResolveInfo;
    ActivityInfo clickedActivityInfo;
    Bitmap bitmap;

    public class PopAdapter extends BaseAdapter {

        LayoutInflater inflater;


        public PopAdapter(){
            inflater = (LayoutInflater)act.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return MyAppList.size();
        }

        @Override
        public Object getItem(int position) {
            return MyAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.popup_item, parent, false);
            }

            resolveInfo = MyAppList.get(position);

            name = (TextView)findViewById(R.id.appname);
            ic = (ImageView)findViewById(R.id.appicon);

            ic.setImageDrawable(clickedActivityInfo.applicationInfo.loadIcon(getPackageManager()));
            name.setText(clickedActivityInfo.applicationInfo.loadLabel(getPackageManager()).toString());
            Log.v("[Program]", clickedActivityInfo.applicationInfo.loadIcon(getPackageManager()) + "!!!" +clickedActivityInfo.applicationInfo.loadLabel(getPackageManager()).toString());

            return convertView;
        }
    }

@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.popup);

    selected = (GridView)findViewById(R.id.appgrid);

    //selected.setAdapter(new PopAdapter(getApplicationContext()));
    selected.setOnItemClickListener(myOnItemClickListener);

    // 전체 앱 불러오기
    popplus = (Button)this.findViewById(R.id.pop_btn);
        popplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent grid_intent = new Intent(getApplicationContext(),AppInfo.class);
                startActivityForResult(grid_intent, APPINFO_REQUESTCODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == APPINFO_REQUESTCODE){
            if(resultCode == Activity.RESULT_OK){
                //클릭한 앱 정보 변수에 저장
                receivedName = data.getStringExtra("name");
                receivedIcon = data.getStringExtra("icon");
                //StringToBitMap(receivedIcon);

                receivedPath = data.getStringExtra("path");
            }
        }
    }

    //전달받은 이미지 string을 bitmap으로 변환
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

    // 앱 서랍 아이템 클릭 시 해당 앱 실행 리스너
    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            clickedResolveInfo = (ResolveInfo)parent.getItemAtPosition(position);   // info
            clickedActivityInfo = clickedResolveInfo.activityInfo;

            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            intent.setClassName(
                    clickedActivityInfo.applicationInfo.packageName,
                    clickedActivityInfo.name);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);

            finish();
        }
    };
}



