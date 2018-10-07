package com.example.tintr.listallapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AppDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private AppInfos mAppInfos;

    private TextView mAppName;
    private TextView mAppPackage;
    private TextView mAppVersion;
    private TextView mAppStorage;
    private TextView mAppListPermission;
    private TextView mAppInstallDay;
    private TextView mAppUpdatedDay;
    private TextView mAppCHPlay;

    public AppDetailFragment() {
        // Required empty public constructor
    }

    public static AppDetailFragment newInstance(AppInfos param1) {
        AppDetailFragment fragment = new AppDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAppInfos = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mAppName = view.findViewById(R.id.app_info_name);
        mAppPackage = view.findViewById(R.id.app_info_p_name);
        mAppVersion = view.findViewById(R.id.app_info_version);
        mAppListPermission = view.findViewById(R.id.app_info_permission);
        mAppStorage = view.findViewById(R.id.app_info_storage);
        mAppInstallDay = view.findViewById(R.id.app_info_install);
        mAppUpdatedDay = view.findViewById(R.id.app_info_updated);
        mAppCHPlay = view.findViewById(R.id.app_info_chplay);
        if (savedInstanceState != null) {
            mAppInfos = savedInstanceState.getParcelable(ARG_PARAM1);
        }
        updatedLayout();
        return view;
    }

    private void updatedLayout() {
        mAppName.setText(mAppInfos.getAppName());
        mAppPackage.setText(mAppInfos.getAppPackage());
        mAppVersion.setText(mAppInfos.getAppVersion());
        mAppInstallDay.setText(mAppInfos.getAppInstallDay());
        mAppUpdatedDay.setText(mAppInfos.getAppUpdatedDay());
        mAppCHPlay.setText(mAppInfos.getAppCHPlay());
        mAppStorage.setText(mAppInfos.getAppStorage());
        mAppListPermission.setText(mAppInfos.getAppListPermission());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_PARAM1, mAppInfos);
    }
}
