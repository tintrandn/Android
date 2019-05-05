package tintrandn.co.jp.moviestore.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.activity.MainActivity;
import tintrandn.co.jp.moviestore.model.MovieBase;
import tintrandn.co.jp.moviestore.model.storage.dao.MovieDao;

public class NotificationServices extends BroadcastReceiver {
    private static final int TIME_VIBRATE = 1000;
    public static String noti_movie_id;
    // Sets an ID for the notification, so it can be updated
    int notifyID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {

        String remind_movie_id = intent.getExtras().getString("remind_id");
        noti_movie_id = remind_movie_id;
//        String remind_time = intent.getExtras().getString("remind_time");
        MovieDao movieDao = new MovieDao(context.getApplicationContext());
        MovieBase movieBase = movieDao.getAllFavouriteMovie(remind_movie_id);
        String name = movieBase.getTitle();
        String year = movieBase.getRelease_year();
        String rate = movieBase.getRate();
        String contentText = "Year: "+year +" Rate: " +rate+"/10";
        Log.d("Notification","Show notification " +name+" "+contentText);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("movie_id",remind_movie_id);
        notificationIntent.putExtras(bundle);
        notificationIntent
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent
                .getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(name)
                        .setContentText(contentText)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE,
                                TIME_VIBRATE})
                        .setContentIntent(contentIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, builder.build());
    }
}
