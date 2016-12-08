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
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teamalmanac.codealmanac.bean.AppFolderDataType;
import com.example.teamalmanac.codealmanac.bean.TodoDataType;
import com.example.teamalmanac.codealmanac.database.DataManager;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopActivity extends Activity {
    private final int APPINFO_REQUESTCODE = 1;
    private Button popplus;
    private GridView selected;

    private DataManager mDB;

    public class PopAdapter extends BaseAdapter{
        LayoutInflater inflater;
        ArrayList<AppFolderDataType> items;

        public PopAdapter(ArrayList<AppFolderDataType> items){
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("TestDebug", "convertView: " + convertView);
            if (convertView == null) {
                inflater = (LayoutInflater)PopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.popup_item, parent, false);
            }
            AppFolderDataType infos = items.get(position);

            TextView appName = (TextView)convertView.findViewById(R.id.appname);
            ImageView appIcon = (ImageView)convertView.findViewById(R.id.appicon);

            try {
                appName.setText(infos.getApp_name());
                appIcon.setImageDrawable(PopActivity.this.getPackageManager().getApplicationIcon(infos.getApp_path()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }

    @Override
        protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);

        /*  FLAG_SHOW_WHEN_LOCKED = 잠금화면 위로 액티비티 실행
            FLAG_DISMISS_KEYGUARD = 키 가드 해제 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        mDB = DataManager.getSingletonInstance();

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
    protected void onStart() {
        super.onStart();
        ArrayList<AppFolderDataType> mItems = mDB.getAppFolderList();

        selected = (GridView)findViewById(R.id.appgrid);
        selected.setAdapter(new PopAdapter(mItems));

        //selected.setOnItemClickListener(myOnItemClickListener);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == APPINFO_REQUESTCODE){
//            if(resultCode == Activity.RESULT_OK){
//                //클릭한 앱 정보 변수에 저장
//                receivedName = data.getStringExtra("name");
//                receivedPath = data.getStringExtra("path");
//            }
//        }
//    }

    // 앱 서랍 아이템 클릭 시 해당 앱 실행 리스너
//    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            clickedResolveInfo = (ResolveInfo)parent.getItemAtPosition(position);   // info
//            clickedActivityInfo = clickedResolveInfo.activityInfo;
//
//            Intent intent = new Intent(Intent.ACTION_MAIN, null);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//            intent.setClassName(
//                    clickedActivityInfo.applicationInfo.packageName,
//                    clickedActivityInfo.name);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            startActivity(intent);
//
//            finish();
//        }
//    };
}



