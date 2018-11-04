package com.example.tintr.alarmmanager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.example.tintr.alarmmanager.Database.AlarmModel;

import java.util.Locale;

class AlarmUtil {

    static String getAlarmTime(AlarmModel alarmModel) {
        int hour = alarmModel.getTimeHour();
        if (hour == 0) hour = 12;
        else if (hour > 12) hour = hour - 12;
        String time = String.format(Locale.getDefault(), "%02d:%02d", hour, alarmModel.getTimeMinute());
        time = alarmModel.getTimeHour() >= 12 ? time + " PM" : time + " AM";
        return time;
    }

    static Spannable getViewRepeatDay(AlarmModel alarmModel) {
        String REPEATE_DAYS = "Mo,Tu,We,Th,Fr,Sa,Su";
        Spannable wordtoSpan = new SpannableString(REPEATE_DAYS);
        boolean[] repeateList = alarmModel.getRepeatingDays();
        for (int i = 0; i < repeateList.length; i++) {
            if (repeateList[i]) {
                wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 3 * i, i == 6 ? 3 * i + 2 : 3 * i + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.WHITE), 3 * i, i == 6 ? 3 * i + 2 : 3 * i + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return wordtoSpan;
    }
}
