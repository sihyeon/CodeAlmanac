package com.example.teamalmanac.codealmanac;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.util.Log;

import com.example.teamalmanac.codealmanac.database.DataManager;
import com.example.teamalmanac.codealmanac.util.HttpPoster;
import com.example.teamalmanac.codealmanac.util.HttpPosterCallBack;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import java.util.UUID;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private DataManager mDb = DataManager.getSingletonInstance();

    public FirebaseInstanceIDService() {
        String token = mDb.getFcmUser().getReg_id();
        String x = null;
        System.out.println(x.toUpperCase());
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
        System.out.println(token);
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        String uuid = UUID.randomUUID().toString();
        mDb.setFcmUser(token, uuid);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("reg_id", token)
                .add("dev_id", uuid)
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://pi.phople.us:7070/fcm/v1/devices")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}