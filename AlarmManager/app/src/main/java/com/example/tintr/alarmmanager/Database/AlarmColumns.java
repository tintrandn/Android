package com.example.tintr.alarmmanager.Database;

import android.provider.BaseColumns;

public final class AlarmColumns {
    public AlarmColumns() {}

    public static abstract class Alarm implements BaseColumns {
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_NAME_ALARM_NAME = "name";
        public static final String COLUMN_NAME_ALARM_TIME_HOUR = "hour";
        public static final String COLUMN_NAME_ALARM_TIME_MINUTE = "minute";
        public static final String COLUMN_NAME_ALARM_REPEAT_DAYS = "days";
        public static final String COLUMN_NAME_ALARM_TONE = "tone";
        public static final String COLUMN_NAME_ALARM_ENABLED = "isEnabled";
    }
}
