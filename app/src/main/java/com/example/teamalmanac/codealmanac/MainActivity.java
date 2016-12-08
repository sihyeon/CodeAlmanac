package com.example.teamalmanac.codealmanac;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamalmanac.codealmanac.adapter.ListViewAdapter;
import com.example.teamalmanac.codealmanac.database.DataManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener{
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        ListView listview;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter();


        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                parent.getAdapter().getItem(position);
                if( id == 0 ) {
                    createDesktopIcon();
                    Toast.makeText(MainActivity.this, "홈 화면에 What 2 do를 추가했습니다.", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, 1);
                } else if(id == 2){
                    startActivity(new Intent(MainActivity.this, TodoLogActivity.class));
                } else if ( id == 3 ) {
                    Uri uri = Uri.parse("http://sihyun2139.wixsite.com/codealmanac");
                    Intent web = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(web);
                } else {

                }
            }
        });


        adapter.ArwItem(ContextCompat.getDrawable(this, R.drawable.btn_set_go));
        adapter.UserItem(ContextCompat.getDrawable(this, R.drawable.img_box_userid));
        adapter.ArwItem(ContextCompat.getDrawable(this, R.drawable.btn_set_go));
        adapter.ArwItem(ContextCompat.getDrawable(this,R.drawable.btn_set_go));

        ImageView header_icn = (ImageView) findViewById(R.id.header_icn);
        TextView header_text = (TextView) findViewById(R.id.header_text);
        TextView copyright = (TextView) findViewById(R.id.copyrignt);
        header_icn.setBackgroundResource(R.drawable.icn_logo);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "GOTHIC.TTF");
        header_text.setTypeface(typeface);
        copyright.setTypeface(typeface);
        copyright.setGravity(Gravity.CENTER);

        Switch switch_btn = (Switch) findViewById(R.id.switch_btn);
        if(isServiceRunning("com.example.teamalmanac.codealmanac.UnlockScreenService")){
            switch_btn.setChecked(true);
        } else {
            switch_btn.setChecked(false);
        }
        switch_btn.setOnCheckedChangeListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseMessaging.getInstance().subscribeToTopic("mainfocus");
                saveToken(FirebaseInstanceId.getInstance().getToken());
            }
        }).start();
    }

    public Boolean isServiceRunning(String serviceName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void saveToken(String token) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token", token)
                .build();

        //request
        Request request = new Request.Builder()
                .url(Constants.API_SERVER + "/token")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            startService(new Intent(this, UnlockScreenService.class));
            Toast.makeText(MainActivity.this, "What 2 do를 실행합니다.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            stopService(new Intent(this, UnlockScreenService.class));
            Toast.makeText(MainActivity.this, "What 2 do를 종료합니다.", Toast.LENGTH_SHORT).show();
        }
    }


    public static Context getContext() {
        return mContext;
    }

    // 바탕화면에 바로가기 아이콘 생성
    public void createDesktopIcon() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pref.getString("check", "");

        if(pref.getString("check", "").isEmpty()){
            Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
            shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            shortcutIntent.setClassName(this, getClass().getName());
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name)); //앱 이름
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(this, R.drawable.launcher)); //앱 아이콘
            intent.putExtra("duplicate", false);
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            sendBroadcast(intent);
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("check", "exist");
        editor.commit();
    }
}
