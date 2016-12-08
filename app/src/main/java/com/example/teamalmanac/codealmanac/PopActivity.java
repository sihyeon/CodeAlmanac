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

import com.example.teamalmanac.codealmanac.bean.TodoDataType;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopActivity extends Activity {
    private final int APPINFO_REQUESTCODE = 1;
    private Button popplus;
    private GridView selected;

    String receivedName;
    String receivedPath;

    Activity act = this;
    private TextView name;
    private ImageView ic;
    private PackageManager myPackageManager;

    ResolveInfo resolveInfo;
    ResolveInfo clickedResolveInfo;
    ActivityInfo clickedActivityInfo;
    Bitmap bitmap;


    public class PopAdapter extends BaseAdapter{
        LayoutInflater inflater;
        ArrayList<Infos> items;

        public PopAdapter(ArrayList<Infos> items){
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
            Infos infos = items.get(position);

            name = (TextView)convertView.findViewById(R.id.appname);
            ic = (ImageView)convertView.findViewById(R.id.appicon);

            try {
                name.setText(infos.getNAME());
                ic.setImageDrawable(PopActivity.this.getPackageManager().getApplicationIcon(infos.getPATH()));
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

        ArrayList<Infos> mItems = new ArrayList<>();

//        StringToBitMap(receivedIcon);
        receivedName = "메시지";
        receivedPath = "com.android.mms";

        Infos infos = new Infos(receivedName,receivedPath);
        mItems.add(infos);

        selected = (GridView)findViewById(R.id.appgrid);

        selected.setAdapter(new PopAdapter(mItems));
        //selected.setOnItemClickListener(myOnItemClickListener);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == APPINFO_REQUESTCODE){
            if(resultCode == Activity.RESULT_OK){
                //클릭한 앱 정보 변수에 저장
                receivedName = data.getStringExtra("name");
                receivedPath = data.getStringExtra("path");
            }
        }
    }

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

   public class Infos {
        private String NAME;
        private String PATH;

            public Infos(String _NAME, String _PATH){
                this.NAME = _NAME;
                this.PATH = _PATH;
            }

            public String getNAME(){
                return NAME;
            }
            public String getPATH(){
                return PATH;
            }
    }
}



