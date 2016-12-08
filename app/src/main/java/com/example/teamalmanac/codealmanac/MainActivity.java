package com.example.teamalmanac.codealmanac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
    private DataManager mDb;

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
                if(id==0) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
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
        switch_btn.setChecked(false);
        switch_btn.setOnCheckedChangeListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseMessaging.getInstance().subscribeToTopic("mainfocus");
                saveToken(FirebaseInstanceId.getInstance().getToken());
            }
        }).start();
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


    public Context getContext() {
        return mContext;
    }
}
