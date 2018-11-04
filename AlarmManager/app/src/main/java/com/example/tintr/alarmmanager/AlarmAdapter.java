package com.example.tintr.alarmmanager;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tintr.alarmmanager.Database.AlarmDBHelper;
import com.example.tintr.alarmmanager.Database.AlarmModel;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private List<AlarmModel> mAlarmModelList;
    private Context mContext;
    private AdapterButtonPressListener mAdapterButtonPressListener;
    private AlarmDBHelper mAlarmDBHelper;

    AlarmAdapter(List<AlarmModel> mAlarmModelList, Context mContext, AdapterButtonPressListener mAdapterButtonPressListener) {
        this.mAlarmModelList = mAlarmModelList;
        this.mContext = mContext;
        this.mAdapterButtonPressListener = mAdapterButtonPressListener;
        mAlarmDBHelper = new AlarmDBHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmAdapter.ViewHolder holder, final int position) {
        AlarmManagerHelper.setAlarms(mContext);
        final AlarmModel alarmModel = mAlarmModelList.get(position);
        holder.alarmTime.setText(AlarmUtil.getAlarmTime(alarmModel));
        holder.alarmRepeateDays.setText(AlarmUtil.getViewRepeatDay(alarmModel));
        holder.alarmStatus.setBackground(alarmModel.isEnabled() ?
                mContext.getResources().getDrawable(R.drawable.selected_circle_drawable) :
                mContext.getResources().getDrawable(R.drawable.unselected_circle_drawable));
        holder.repeatCheckBox.setChecked(alarmModel.isRepeatWeekly());
        holder.repeatList.setVisibility(alarmModel.isRepeatWeekly() ? View.VISIBLE : View.GONE);
        holder.alarmIndicator.setImageResource(alarmModel.getExpand() ? R.drawable.ic_baseline_keyboard_arrow_down_24px : R.drawable.ic_baseline_keyboard_arrow_up_24px);
        holder.alarmSettingArea.setVisibility(alarmModel.getExpand() ? View.VISIBLE : View.GONE);
        holder.alarmEditTime.setVisibility(alarmModel.getExpand() ? View.VISIBLE : View.GONE);
        holder.alarmTextMessage.setText(alarmModel.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (alarmModel.getExpand()) {
                holder.alarmArea.setForeground(null);
                holder.alarmTime.setForeground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_selector));
            } else {
                holder.alarmArea.setForeground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_selector));
                holder.alarmTime.setForeground(null);
            }
        } else {
            if (alarmModel.getExpand()) {
                holder.alarmTime.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_selector));
            } else {
                holder.alarmTime.setBackground(null);
            }
        }
        // update repeated button
        updateWeeklyRepeat(holder, mAlarmModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlarmModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout alarmArea;
        private ImageView alarmStatus;
        private TextView alarmEditTime;
        private TextView alarmTime;
        private TextView alarmRepeateDays;
        private TextView alarmTextMessage;
        private TextView soundSetting;
        private LinearLayout repeatList;
        private LinearLayout alarmSettingArea;
        private CheckBox repeatCheckBox;
        private Button btnMonday;
        private Button btnTuesday;
        private Button btnWednesday;
        private Button btnThursday;
        private Button btnFriday;
        private Button btnSaturday;
        private Button btnSunday;

        private ImageView alarmIndicator;

        ViewHolder(View itemView) {
            super(itemView);
            alarmArea = itemView.findViewById(R.id.alarm_area);
            alarmStatus = itemView.findViewById(R.id.alarm_status_icon);
            alarmTime = itemView.findViewById(R.id.alarm_time);
            alarmRepeateDays = itemView.findViewById(R.id.alarm_repeate_days);
            alarmEditTime = itemView.findViewById(R.id.edit_btn);
            repeatCheckBox = itemView.findViewById(R.id.repeat_check_box);
            alarmSettingArea = itemView.findViewById(R.id.alarm_setting_area);
            alarmIndicator = itemView.findViewById(R.id.alarm_indicator);
            alarmTextMessage = itemView.findViewById(R.id.alarm_text_message);
            soundSetting = itemView.findViewById(R.id.sound_setting_btn);
            repeatList = itemView.findViewById(R.id.repeat_list);
            btnMonday = itemView.findViewById(R.id.monday_btn);
            btnTuesday = itemView.findViewById(R.id.tuesday_btn);
            btnWednesday = itemView.findViewById(R.id.wednesday_btn);
            btnThursday = itemView.findViewById(R.id.thursday_btn);
            btnFriday = itemView.findViewById(R.id.friday_btn);
            btnSaturday = itemView.findViewById(R.id.saturday_btn);
            btnSunday = itemView.findViewById(R.id.sunday_btn);
            alarmArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    if (!alarmModel.getExpand()) {
                        mAdapterButtonPressListener.setAlarmOnOff(alarmModel);
                    }
                }
            });
            alarmTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    if (alarmModel.getExpand()) {
                        mAdapterButtonPressListener.editTimer(alarmModel);
                    }
                }
            });
            alarmStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    mAdapterButtonPressListener.setAlarmOnOff(alarmModel);
                }
            });
            btnMonday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    mAdapterButtonPressListener.onRepeatClick(alarmModel, AlarmModel.MONDAY);
                }
            });
            btnTuesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    mAdapterButtonPressListener.onRepeatClick(alarmModel, AlarmModel.TUESDAY);
                }
            });
            btnWednesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    mAdapterButtonPressListener.onRepeatClick(alarmModel, AlarmModel.WEDNESDAY);
                }
            });
            btnThursday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    mAdapterButtonPressListener.onRepeatClick(alarmModel, AlarmModel.THURSDAY);
                }
            });
            btnFriday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    mAdapterButtonPressListener.onRepeatClick(alarmModel, AlarmModel.FRIDAY);
                }
            });
            btnSaturday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    mAdapterButtonPressListener.onRepeatClick(alarmModel, AlarmModel.SATURDAY);
                }
            });
            btnSunday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    mAdapterButtonPressListener.onRepeatClick(alarmModel, AlarmModel.SUNDAY);
                }
            });

            alarmIndicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterButtonPressListener.resetExpandItem(getAdapterPosition());
                }
            });

            repeatCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmModel alarmModel = mAlarmModelList.get(getAdapterPosition());
                    repeatList.setVisibility(repeatCheckBox.isChecked() ? View.VISIBLE : View.GONE);
                    if (!alarmModel.isRepeatWeekly()) {
                        boolean[] repeatingDays = {true, true, true, true, true, true, true};
                        alarmModel.setRepeatingDays(repeatingDays);
                        notifyDataSetChanged();
                    }
                    mAlarmDBHelper.updateAlarm(alarmModel);
                }
            });

            alarmTextMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterButtonPressListener.setAlarmTextMessage(mAlarmModelList.get(getAdapterPosition()));
                }
            });

            soundSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterButtonPressListener.openSoundSetting(mAlarmModelList.get(getAdapterPosition()));
                }
            });
        }

    }

    private void updateWeeklyRepeat(ViewHolder viewHolder, AlarmModel alarmModel) {
        boolean[] repeatingDay = alarmModel.getRepeatingDays();
        viewHolder.btnMonday.setBackground(repeatingDay[AlarmModel.MONDAY] ?
                mContext.getResources().getDrawable(R.drawable.selected_circle_drawable) :
                mContext.getResources().getDrawable(R.drawable.unselected_circle_drawable));
        viewHolder.btnTuesday.setBackground(repeatingDay[AlarmModel.TUESDAY] ?
                mContext.getResources().getDrawable(R.drawable.selected_circle_drawable) :
                mContext.getResources().getDrawable(R.drawable.unselected_circle_drawable));
        viewHolder.btnWednesday.setBackground(repeatingDay[AlarmModel.WEDNESDAY] ?
                mContext.getResources().getDrawable(R.drawable.selected_circle_drawable) :
                mContext.getResources().getDrawable(R.drawable.unselected_circle_drawable));
        viewHolder.btnThursday.setBackground(repeatingDay[AlarmModel.THURSDAY] ?
                mContext.getResources().getDrawable(R.drawable.selected_circle_drawable) :
                mContext.getResources().getDrawable(R.drawable.unselected_circle_drawable));
        viewHolder.btnFriday.setBackground(repeatingDay[AlarmModel.FRIDAY] ?
                mContext.getResources().getDrawable(R.drawable.selected_circle_drawable) :
                mContext.getResources().getDrawable(R.drawable.unselected_circle_drawable));
        viewHolder.btnSaturday.setBackground(repeatingDay[AlarmModel.SATURDAY] ?
                mContext.getResources().getDrawable(R.drawable.selected_circle_drawable) :
                mContext.getResources().getDrawable(R.drawable.unselected_circle_drawable));
        viewHolder.btnSunday.setBackground(repeatingDay[AlarmModel.SUNDAY] ?
                mContext.getResources().getDrawable(R.drawable.selected_circle_drawable) :
                mContext.getResources().getDrawable(R.drawable.unselected_circle_drawable));
    }

    interface AdapterButtonPressListener {
        void onRepeatClick(AlarmModel alarmModel, int weekDay);

        void setAlarmOnOff(AlarmModel alarmModel);

        void editTimer(AlarmModel alarmModel);

        void setAlarmTextMessage(AlarmModel alarmModel);

        void openSoundSetting(AlarmModel alarmModel);

        void resetExpandItem(int position);
    }
}
