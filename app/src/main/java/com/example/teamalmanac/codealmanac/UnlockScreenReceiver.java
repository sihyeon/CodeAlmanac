package com.example.teamalmanac.codealmanac;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UnlockScreenReceiver extends BroadcastReceiver {
    public UnlockScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        //스크린의 온 오프 이벤트. 작동확인
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
           Log.d("LockTest", "off event");
       } else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
           Log.d("LockTest", "on event");
       }
    }
}
