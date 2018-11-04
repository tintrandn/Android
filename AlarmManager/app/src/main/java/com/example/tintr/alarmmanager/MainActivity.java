package com.example.tintr.alarmmanager;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.tintr.alarmmanager.Database.AlarmDBHelper;
import com.example.tintr.alarmmanager.Database.AlarmModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener, AlarmAdapter.AdapterButtonPressListener {

    private RecyclerView mAlarmListView;
    private List<AlarmModel> mAlarmModelList;
    private AlarmAdapter mAlarmAdapter;
    private AlarmDBHelper mAlarmDBHelper;
    private AlarmModel mAlarmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final View mFabButton = findViewById(R.id.add_new_alarm);
        mFabButton.setOnClickListener(this);
        mAlarmListView = findViewById(R.id.list_alarm);
        mAlarmListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAlarmDBHelper = new AlarmDBHelper(getApplicationContext());
        ViewTreeObserver observer = mAlarmListView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heigh = mFabButton.getHeight();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, heigh);
                mAlarmListView.setLayoutParams(params);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //load data
        mAlarmModelList = new ArrayList<>();
        if (mAlarmDBHelper.getAlarms() != null && mAlarmDBHelper.getAlarms().size() > 0) {
            mAlarmModelList = mAlarmDBHelper.getAlarms();
        }
        mAlarmAdapter = new AlarmAdapter(mAlarmModelList, getApplicationContext(), this);
        mAlarmListView.setAdapter(mAlarmAdapter);
        mAlarmAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            startActivity(new Intent(MainActivity.this, DeleteActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_alarm:
                AlarmModel alarmModel = new AlarmModel();
                setTimer(alarmModel, false);
                break;
            default:

                break;
        }
    }

    private void setTimer(final AlarmModel alarmModel, final boolean update) {
        Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        final int minute = myCalender.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                alarmModel.setTimeHour(hourOfDay);
                alarmModel.setTimeMinute(minutes);
                if (update) {
                    mAlarmDBHelper.updateAlarm(alarmModel);
                } else {
                    mAlarmModelList.add(alarmModel);
                    mAlarmDBHelper.createAlarm(alarmModel);
                }
                mAlarmAdapter.notifyDataSetChanged();
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onRepeatClick(AlarmModel alarmModel, int weekDay) {
        boolean[] repeatingDay = alarmModel.getRepeatingDays();
        alarmModel.setRepeatingDay(weekDay, !repeatingDay[weekDay]);
        mAlarmAdapter.notifyDataSetChanged();
        mAlarmDBHelper.updateAlarm(alarmModel);
    }

    @Override
    public void setAlarmOnOff(AlarmModel alarmModel) {
        alarmModel.setEnabled(!alarmModel.isEnabled());
        mAlarmDBHelper.updateAlarm(alarmModel);
        mAlarmAdapter.notifyDataSetChanged();
    }

    @Override
    public void resetExpandItem(int position) {
        for (int i = 0; i < mAlarmModelList.size(); i++) {
            if (i == position && !mAlarmModelList.get(i).getExpand()) {
                mAlarmModelList.get(i).setExpand(true);
                mAlarmListView.smoothScrollToPosition(position);
            } else {
                mAlarmModelList.get(i).setExpand(false);
            }
        }
        mAlarmAdapter.notifyDataSetChanged();
    }

    @Override
    public void editTimer(AlarmModel alarmModel) {
        setTimer(alarmModel, true);
    }

    @Override
    public void setAlarmTextMessage(final AlarmModel alarmModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(40, 30, 40, 10);
        // Set up the input
        TextView title = new TextView(this);
        final EditText input = new EditText(this);
        final TextView maxText = new TextView(this);
        title.setText(getResources().getText(R.string.alarm_text));
        title.setTextColor(getResources().getColor(R.color.white));
        title.setTypeface(Typeface.DEFAULT_BOLD);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(alarmModel.getName());
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        input.setTextColor(getResources().getColor(R.color.white));
        maxText.setText(getResources().getText(R.string.alarm_text_max));
        maxText.setGravity(Gravity.END);
        maxText.setTextColor(getResources().getColor(R.color.white));
        linearLayout.addView(title);
        linearLayout.addView(input);
        linearLayout.addView(maxText);
        builder.setView(linearLayout);
        final TextWatcher txwatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = String.valueOf(s.length()) + "/30";
                maxText.setText(number);
            }

            public void afterTextChanged(Editable s) {
            }
        };
        input.addTextChangedListener(txwatcher);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarmModel.setName(input.getText().toString());
                mAlarmDBHelper.updateAlarm(alarmModel);
                mAlarmAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void openSoundSetting(AlarmModel alarmModel) {
        mAlarmModel = alarmModel;
        final Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
        startActivityForResult(intent , 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case 1: {
                    mAlarmModel.alarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    mAlarmDBHelper.updateAlarm(mAlarmModel);
                    mAlarmAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}
