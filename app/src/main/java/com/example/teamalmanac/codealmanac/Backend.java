package com.example.teamalmanac.codealmanac;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Backend {
    private static void request(final String path, final Map<String, String> items) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder body = new FormBody.Builder();
                OkHttpClient client = new OkHttpClient();

                for (Map.Entry<String, String> item : items.entrySet())
                    body.add(item.getKey(), item.getValue());

                Request request = new Request.Builder()
                        .url(Constants.API_SERVER + path)
                        .post(body.build())
                        .build();

                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void saveToken(String token) {
        Map<String, String> items = new HashMap<>();
        items.put("token", token);
        request("/token", items);
    }

    public static void saveMainFocus(final String content) {
        Map<String, String> items = new HashMap<>();
        items.put("token", FirebaseInstanceId.getInstance().getToken());
        items.put("content", content);
        request("/mainfocus", items);
    }
}