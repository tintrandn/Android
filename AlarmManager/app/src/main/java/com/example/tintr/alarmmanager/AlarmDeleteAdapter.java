package com.example.tintr.alarmmanager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tintr.alarmmanager.Database.AlarmColumns;
import com.example.tintr.alarmmanager.Database.AlarmDBHelper;
import com.example.tintr.alarmmanager.Database.AlarmModel;

import java.util.List;
import java.util.Locale;

public class AlarmDeleteAdapter extends RecyclerView.Adapter<AlarmDeleteAdapter.ViewHolder> {
    private List<AlarmModel> mAlarmModelList;
    private Context mContext;
    private DeleteAlarmListener mDeleteAlarmListener;
    private AlarmDBHelper mAlarmDBHelper;

    AlarmDeleteAdapter(List<AlarmModel> mAlarmModelList, Context mContext, DeleteAlarmListener mDeleteAlarmListener) {
        this.mAlarmModelList = mAlarmModelList;
        this.mContext = mContext;
        this.mDeleteAlarmListener = mDeleteAlarmListener;
        mAlarmDBHelper = new AlarmDBHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_delete_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmDeleteAdapter.ViewHolder holder, final int position) {
        final AlarmModel alarmModel = mAlarmModelList.get(position);
        holder.alarmTime.setText(AlarmUtil.getAlarmTime(alarmModel));
        holder.alarmRepeateDays.setText(AlarmUtil.getViewRepeatDay(alarmModel));
        holder.alarmStatus.setBackground(alarmModel.isEnabled() ?
                mContext.getResources().getDrawable(R.drawable.selected_circle_drawable) :
                mContext.getResources().getDrawable(R.drawable.unselected_circle_drawable));
        holder.deleteCheckBox.setChecked(alarmModel.isDelete());
        if (holder.deleteCheckBox.isChecked()){
            mDeleteAlarmListener.setDeleteAlarm(position);
        }
    }

    @Override
    public int getItemCount() {
        return mAlarmModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout deleteArea;
        private ImageView alarmStatus;
        private TextView alarmTime;
        private TextView alarmRepeateDays;
        private CheckBox deleteCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            deleteArea = itemView.findViewById(R.id.delete_area);
            alarmStatus = itemView.findViewById(R.id.alarm_status_icon);
            alarmTime = itemView.findViewById(R.id.alarm_time);
            alarmRepeateDays = itemView.findViewById(R.id.alarm_repeate_days);
            deleteCheckBox = itemView.findViewById(R.id.delete_check_box);
            deleteArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCheckBox.setChecked(!deleteCheckBox.isChecked());
                    mDeleteAlarmListener.setDeleteAlarm(getAdapterPosition());
                }
            });
            deleteCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeleteAlarmListener.setDeleteAlarm(getAdapterPosition());
                }
            });
        }
    }

    interface DeleteAlarmListener {
        void setDeleteAlarm(int position);
    }
}
