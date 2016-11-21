package com.example.teamalmanac.codealmanac;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UnlockScreenService extends Service {
    public UnlockScreenService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
