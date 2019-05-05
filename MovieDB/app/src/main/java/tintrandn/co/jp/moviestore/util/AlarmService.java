package tintrandn.co.jp.moviestore.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmService", "onReceive Receiver");
        if (intent !=null && intent.getAction() != null) {
            final String action = intent.getAction();
            Log.d("AlarmService", "Broadcast Receiver" + action);
            if (action.equals(Intent.ACTION_BOOT_COMPLETED) ||
                    action.equals(Intent.ACTION_TIME_CHANGED)) {
                //Create Alarm
                AlarmUtils.create(context.getApplicationContext());
            }
        }
    }
}
