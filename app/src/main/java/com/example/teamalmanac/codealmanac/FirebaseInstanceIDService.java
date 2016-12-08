package com.example.teamalmanac.codealmanac;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    public FirebaseInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Backend.saveToken(FirebaseInstanceId.getInstance().getToken());
            }
        });
    }
}
