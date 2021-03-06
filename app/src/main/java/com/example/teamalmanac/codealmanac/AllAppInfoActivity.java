package com.example.teamalmanac.codealmanac;


import android.app.Activity;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.TextView;

import com.example.teamalmanac.codealmanac.database.DataManager;

public class AllAppInfoActivity extends Activity {
    Activity act = this;
    private PackageManager myPackageManager;
    private Context myContext;
    private DataManager mDB;
    public List<ResolveInfo> MyAppList;

    GridView gridView;
    private ImageView imageView;
    private TextView textView;
    ResolveInfo resolveInfo;
    ResolveInfo clickedResolveInfo;
    ActivityInfo clickedActivityInfo;

    private String APP_NAME;
    private String APP_PATH;


    public class MyBaseAdapter extends BaseAdapter {

        LayoutInflater inflater;


        public MyBaseAdapter(){
           inflater = (LayoutInflater)act.getSystemService(myContext.LAYOUT_INFLATER_SERVICE);
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
                convertView = inflater.inflate(R.layout.grid_item_all_app_info, parent, false);
            }

            resolveInfo = MyAppList.get(position);

            imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            textView = (TextView) convertView.findViewById(R.id.textView);

            imageView.setImageDrawable(resolveInfo.activityInfo.loadIcon(getPackageManager()));
            textView.setText(resolveInfo.activityInfo.loadLabel(myPackageManager).toString());
            Log.v("[Program]", resolveInfo.activityInfo.packageName + "," + resolveInfo.activityInfo.name+","+ resolveInfo.activityInfo.loadIcon(getPackageManager()));


            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*  FLAG_SHOW_WHEN_LOCKED = 잠금화면 위로 액티비티 실행
            FLAG_DISMISS_KEYGUARD = 키 가드 해제 */
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        mDB = DataManager.getSingletonInstance(getApplicationContext());

        myPackageManager = getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        MyAppList = myPackageManager.queryIntentActivities(intent,0);


        setContentView(R.layout.activity_all_app_info);
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new MyBaseAdapter());
        gridView.setOnItemClickListener(myOnItemClickListener);

    }

    OnItemClickListener myOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            clickedResolveInfo = (ResolveInfo)parent.getItemAtPosition(position);   // info
            clickedActivityInfo = clickedResolveInfo.activityInfo;

            APP_NAME = clickedActivityInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            APP_PATH = clickedActivityInfo.applicationInfo.packageName;

            mDB.insertApp(APP_NAME, APP_PATH);

            finish();
        }
    };
}