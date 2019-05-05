package tintrandn.co.jp.moviestore.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import tintrandn.co.jp.moviestore.model.storage.dao.RemindDao;
import tintrandn.co.jp.moviestore.model.storage.entity.Remind;

public class AlarmUtils {
    public static void create(Context context) {
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationServices.class);
        RemindDao remindDao = new RemindDao(context);
        ArrayList<Remind> remindList = remindDao.getAllRemind();
        for (Remind remind:remindList) {
            String remind_movie_id = remind.getRemind_movie_id();
            String remind_time = remind.getRemind_time();
            Log.d("Get Remind","\nRemind Time: "+ remind_time+ "\nRemind Id: "+ remind_movie_id);
            Bundle bundle = new Bundle();
            bundle.putString("remind_time",remind_time);
            bundle.putString("remind_id",remind_movie_id);
            intent.putExtras(bundle);
            PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            String[] year_time= remind_time.split(" ");
            String[] date = year_time[0].split("/");
            String[] time = year_time[1].split(":");
            Log.d("Alarm","Date:" +date[0]+":"+date[1]+":"+date[2] + " Time:" + time[0]+":"+time[1]);
            Calendar cal = Calendar.getInstance();
            int month = Integer.valueOf(date[1]);
            cal.set(Integer.valueOf(date[0]),
                    month-1,
                    Integer.valueOf(date[2]),
                    Integer.valueOf(time[0]),
                    Integer.valueOf(time[1]),
                    0);

            mAlarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), mPendingIntent);
        }
    }
}