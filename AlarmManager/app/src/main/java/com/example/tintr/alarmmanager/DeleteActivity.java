package com.example.tintr.alarmmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.tintr.alarmmanager.Database.AlarmDBHelper;
import com.example.tintr.alarmmanager.Database.AlarmModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteActivity extends AppCompatActivity implements View.OnClickListener, AlarmDeleteAdapter.DeleteAlarmListener {

    private AlarmDBHelper mAlarmDBHelper;
    private AlarmDeleteAdapter mAlarmDeleteAdapter;
    private List<AlarmModel> mAlarmModelList;
    private List<Integer> mDeletePositionList = new ArrayList<>();
    private CheckBox mMarkAllCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_alarm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mMarkAllCheckBox = findViewById(R.id.mark_all);
        mMarkAllCheckBox.setOnClickListener(this);
        ImageView delete = findViewById(R.id.deleted_btn);
        delete.setOnClickListener(this);
        RecyclerView deleteList = findViewById(R.id.delete_list);
        deleteList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAlarmDBHelper = new AlarmDBHelper(getApplicationContext());
        //load data
        mAlarmModelList = new ArrayList<>();
        if (mAlarmDBHelper.getAlarms() != null && mAlarmDBHelper.getAlarms().size() > 0) {
            mAlarmModelList = mAlarmDBHelper.getAlarms();
        }
        mAlarmDeleteAdapter = new AlarmDeleteAdapter(mAlarmModelList, getApplicationContext(), this);
        deleteList.setAdapter(mAlarmDeleteAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void registerDeleteAll(boolean check) {
        if (mAlarmModelList == null || mAlarmModelList.size() == 0)
            return;
        for (int i = 0; i < mAlarmModelList.size(); i++) {
            mAlarmModelList.get(i).setDelete(check);
        }
        mAlarmDeleteAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDeleteAlarm(int position) {
        mDeletePositionList.add(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mark_all:
                if (mMarkAllCheckBox.isChecked()) {
                    registerDeleteAll(true);
                } else {
                    registerDeleteAll(false);
                }
                break;
            case R.id.deleted_btn:
                if (mDeletePositionList != null && mDeletePositionList.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
                    builder.setMessage("Do you want to delete ?");
                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i : mDeletePositionList) {
                                mAlarmDBHelper.deleteAlarm(mAlarmModelList.get(i).getId());
                            }
                            finish();
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
                break;
            default:
                break;
        }
    }
}
