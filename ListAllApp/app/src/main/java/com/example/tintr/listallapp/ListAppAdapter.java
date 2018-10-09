package com.example.tintr.listallapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

public class ListAppAdapter extends RecyclerView.Adapter<ListAppAdapter.ViewHolder> {

    private List<AppInfos> mAppInfos;
    private int mLastPosition = -1;
    private Context mContext;

//    private AppIconClick mAppIconClick;
//    private AppInfoClick mAppInfoClick;

//    public ListAppAdapter(Context mContext, List<AppInfo> mAppInfo, AppIconClick mAppIconClick, AppInfoClick mAppInfoClick) {
//        this.mAppInfo = mAppInfo;
//        this.mContext = mContext;
//        this.mAppIconClick = mAppIconClick;
//        this.mAppInfoClick = mAppInfoClick;
//    }

    ListAppAdapter(Context mContext, List<AppInfos> mAppInfos) {
        this.mContext = mContext;
        this.mAppInfos = mAppInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_app_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AppInfos applicationInfo = mAppInfos.get(position);
        holder.appIcon.setImageDrawable(applicationInfo.getAppIcon());
        holder.appName.setText(applicationInfo.getAppName());
        holder.appFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mAppIconClick.onAppIconClick(applicationInfo);
                EventBus.getDefault().post(new BusEvent("AppIcon", applicationInfo));
            }
        });
        holder.appInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mAppInfoClick.onAppInfoClick(applicationInfo);
                EventBus.getDefault().post(new BusEvent("AppInfo", applicationInfo));
            }
        });

        if (holder.getAdapterPosition() > mLastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_up);
            holder.itemView.startAnimation(animation);
            mLastPosition = holder.getAdapterPosition();
        } else {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_up);
            holder.itemView.startAnimation(animation);
            mLastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mAppInfos.size();
    }

    void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mAppInfos, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mAppInfos, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView appName;
        private ImageView appIcon;
        private ImageView appInfo;
        private CardView appFrame;

        ViewHolder(View itemView) {
            super(itemView);
            appFrame = itemView.findViewById(R.id.app_frame);
            appName = itemView.findViewById(R.id.app_name);
            appIcon = itemView.findViewById(R.id.app_icon);
            appInfo = itemView.findViewById(R.id.app_info);
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            int size = displayMetrics.widthPixels < displayMetrics.heightPixels ? displayMetrics.widthPixels / 3 : displayMetrics.widthPixels / 4;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size - 30, size - 30);
            appIcon.setLayoutParams(layoutParams);
        }
    }

//    public interface AppIconClick {
//        void onAppIconClick(AppInfos applicationInfo);
//    }
//
//    public interface AppInfoClick {
//        void onAppInfoClick(AppInfos applicationInfo);
//    }
}
