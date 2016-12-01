package com.example.teamalmanac.codealmanac;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Context.KEYGUARD_SERVICE;

public class UnlockScreenReceiver extends BroadcastReceiver {
    public UnlockScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        //스크린의 온 오프 이벤트. 작동확인
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.d("LockTest", "LockScreen On");
            Intent i = new Intent(context, TabActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            if(!keyguardManager.inKeyguardRestrictedInputMode()){
//                Intent i = new Intent(context, TabActivity.class);
//                i.putExtra("exit", true);
//                Intent.Fl
//                context.startActivity(i);
                TabActivity.getTabActivity().finish();
            }
        }

    }
}
