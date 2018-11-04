package com.example.tintr.alarmmanager;

import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tintr.alarmmanager.Database.AlarmDBHelper;
import com.example.tintr.alarmmanager.Database.AlarmModel;

public class AlarmManagerHelper extends BroadcastReceiver {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME_HOUR = "timeHour";
    public static final String TIME_MINUTE = "timeMinute";
    public static final String TONE = "alarmTone";

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        setAlarms(context);
    }

    public static void setAlarms(Context context) {
        cancelAlarms(context);

        AlarmDBHelper dbHelper = new AlarmDBHelper(context);

        List<AlarmModel> alarms = dbHelper.getAlarms();

        for (AlarmModel alarm:alarms) {
            if (alarm.isEnabled()) {
                PendingIntent pIntent = createPendingIntent(context, alarm);

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, alarm.getTimeHour());
                calendar.set(Calendar.MINUTE, alarm.getTimeMinute());
                calendar.set(Calendar.SECOND, 0);

                final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
                boolean alarmSet = false;


                if ((alarm.getTimeHour() > nowHour) | ((alarm.getTimeHour() == nowHour) && (alarm.getTimeMinute() > nowMinute))){
                    calendar.set(Calendar.DAY_OF_WEEK, nowDay);
                    setAlarm(context, calendar, pIntent);
                    alarmSet = true;
                } else {
                    for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++ dayOfWeek) {
                        if ( alarm.getRepeatingDay(dayOfWeek -1 ) && dayOfWeek >= nowDay && !(dayOfWeek == nowDay && alarm.getTimeHour() < nowHour)
                                && !(dayOfWeek == nowDay && alarm.getTimeHour() == nowHour && alarm.getTimeMinute() <= nowMinute)) {
                            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                            setAlarm(context, calendar, pIntent);
                            alarmSet = true;
                            break;
                        }
                    }
                }

                if (!alarmSet) {
                    for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++ dayOfWeek) {
                        if (alarm.getRepeatingDay(dayOfWeek -1 ) && dayOfWeek <= nowDay && alarm.isRepeatWeekly()) {
                            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                            calendar.add(Calendar.WEEK_OF_YEAR, 1);
                            setAlarm(context, calendar, pIntent);
                            break;
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    public static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
    }

    public static void cancelAlarms(Context context) {
        AlarmDBHelper dbHelper = new AlarmDBHelper(context);

        List<AlarmModel> alarms = dbHelper.getAlarms();

        if (alarms != null)
        {
            for (AlarmModel alarm:alarms) {
                if (alarm.isEnabled()) {
                    PendingIntent pIntent = createPendingIntent(context, alarm);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    assert alarmManager != null;
                    alarmManager.cancel(pIntent);
                }
            }
        }
    }

    private static PendingIntent createPendingIntent(Context context, AlarmModel model) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(ID, model.getId());
        intent.putExtra(NAME, model.getName());
        intent.putExtra(TIME_HOUR, model.getTimeHour());
        intent.putExtra(TIME_MINUTE, model.getTimeMinute());
        intent.putExtra(TONE, model.getAlarmTone().toString());
        return PendingIntent.getService(context, (int) model.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}