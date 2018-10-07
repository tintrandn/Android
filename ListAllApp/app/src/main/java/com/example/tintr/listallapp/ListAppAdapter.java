package com.example.tintr.listallapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ListAppAdapter extends RecyclerView.Adapter<ListAppAdapter.ViewHolder> {
    private List<AppInfos> mAppInfos;
//    private AppIconClick mAppIconClick;
//    private AppInfoClick mAppInfoClick;

//    public ListAppAdapter(Context mContext, List<AppInfo> mAppInfo, AppIconClick mAppIconClick, AppInfoClick mAppInfoClick) {
//        this.mAppInfo = mAppInfo;
//        this.mContext = mContext;
//        this.mAppIconClick = mAppIconClick;
//        this.mAppInfoClick = mAppInfoClick;
//    }

    ListAppAdapter(List<AppInfos> mAppInfos) {
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
        holder.appIcon.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return mAppInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView appName;
        private ImageView appIcon;
        private ImageView appInfo;

        ViewHolder(View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.app_name);
            appIcon = itemView.findViewById(R.id.app_icon);
            appInfo = itemView.findViewById(R.id.app_info);
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
