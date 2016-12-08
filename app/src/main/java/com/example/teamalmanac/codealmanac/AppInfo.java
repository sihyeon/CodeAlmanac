package com.example.teamalmanac.codealmanac;


import android.app.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.TextView;
import android.widget.Toast;

public class AppInfo extends Activity {
    Activity act = this;
    private PackageManager myPackageManager;
    private Context myContext;
    public List<ResolveInfo> MyAppList;

    GridView gridView;
    private ImageView imageView;
    private TextView textView;
    ResolveInfo resolveInfo;
    ResolveInfo clickedResolveInfo;
    ActivityInfo clickedActivityInfo;

    private String APP_NAME;
    private String APP_PATH;
    private String APP_ICON;


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
                convertView = inflater.inflate(R.layout.griditem, parent, false);
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

        myPackageManager = getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        MyAppList = myPackageManager.queryIntentActivities(intent,0);


        setContentView(R.layout.gridview);
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
            APP_ICON = clickedActivityInfo.applicationInfo.loadIcon(getPackageManager()).toString();

            Intent intent = new Intent();
            intent.putExtra("name", APP_NAME);
            intent.putExtra("path", APP_PATH);
            intent.putExtra("icon", APP_ICON);
            setResult(Activity.RESULT_OK, intent);
            finish();

//            Intent intent = new Intent();
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//             클릭한 앱 실행
//            intent.setClassName(
//                    clickedActivityInfo.applicationInfo.packageName,
//                    clickedActivityInfo.name);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            startActivity(intent);
//
//            finish();
        }
    };
}