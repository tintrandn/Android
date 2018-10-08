package com.example.tintr.listallapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//public class MainActivity extends AppCompatActivity implements ListAppAdapter.AppInfoClick, ListAppAdapter.AppIconClick {
public class MainActivity extends AppCompatActivity {

    private static final String CHPLAY = "http://play.google.com/store/apps/details?id=";
    //    private static final int MY_PERMISSIONS_REQUEST_GET_PACKAGE_SIZE = 1;
    private List<AppInfos> mAppInfosList = new ArrayList<>();
    private ListAppAdapter mListAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = findViewById(R.id.recycle_view);
        mListAppAdapter = new ListAppAdapter(getApplicationContext(), mAppInfosList);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), displayMetrics.widthPixels < displayMetrics.heightPixels ? 3 : 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mListAppAdapter);
//      checkPermission();
    }

//    private void checkPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.GET_PACKAGE_SIZE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // No explanation needed; request the permission
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.GET_PACKAGE_SIZE},
//                    MY_PERMISSIONS_REQUEST_GET_PACKAGE_SIZE);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppInfosList.clear();
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.US);
        for (PackageInfo packageInfo : packageInfoList) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                AppInfos appInfos = new AppInfos();
                appInfos.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                appInfos.setAppPackage(packageInfo.packageName);
                appInfos.setAppVersion(packageInfo.versionName);
                appInfos.setAppInstallDay(String.valueOf(format.format(new Date(packageInfo.firstInstallTime))));
                appInfos.setAppUpdatedDay(String.valueOf(format.format(new Date(packageInfo.lastUpdateTime))));
                appInfos.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
                appInfos.setAppCHPlay(CHPLAY + packageInfo.packageName);
                String[] permissionList = packageInfo.requestedPermissions;
                StringBuilder sb = new StringBuilder();
                if (permissionList != null) {
                    for (int i = 0; i < permissionList.length; i++) {
                        sb.append(permissionList[i]);
                        if (i < permissionList.length - 1) sb.append("\n");
                    }
                    appInfos.setAppListPermission(sb.toString());
                }
                mAppInfosList.add(appInfos);
            }
        }
        mListAppAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void OnBusEvent(BusEvent busEvent) {
        AppInfos appInfos = busEvent.getAppInfo();
        switch (busEvent.getEvent()) {
            case "AppIcon":
                Intent intent = getPackageManager().getLaunchIntentForPackage(appInfos.getAppPackage());
                if (intent != null) {
                    startActivity(intent);
                }
                break;
            case "AppInfo":
//                checkPermission();
                getAppStorage(appInfos);
                break;
            default:
                break;
        }
    }

    private void getAppStorage(AppInfos appInfos) {
        PackageManager pm = getPackageManager();
        Method getPackageSizeInfo;
        try {
            getPackageSizeInfo = pm.getClass().getMethod(
                    "getPackageSizeInfo", String.class,
                    IPackageStatsObserver.class);
            getPackageSizeInfo.invoke(pm, appInfos.getAppPackage(),
                    new CachePackState(appInfos)); //Call the inner class
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            appInfos.setAppStorage("Because of security, Android 8 above will not support");
            getSupportFragmentManager().beginTransaction().replace(R.id.app_detail_container, AppDetailFragment.newInstance(appInfos)).addToBackStack(null).commit();
        }
    }

    /*
     * Inner class which will get the data size for application
     * */
    private class CachePackState extends IPackageStatsObserver.Stub {
        private AppInfos appInfos;

        private CachePackState(AppInfos appInfos) {
            this.appInfos = appInfos;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
            float appSizeMB = (float) (pStats.codeSize / 1024.0 / 1024.0);
            appInfos.setAppStorage(String.valueOf(Math.round(appSizeMB * 100.0) / 100.0) + "MB");
            getSupportFragmentManager().beginTransaction().replace(R.id.app_detail_container, AppDetailFragment.newInstance(appInfos)).addToBackStack(null).commit();
        }
    }
//    @Override
//    public void onAppIconClick(AppInfos applicationInfo) {
//        Intent intent = getPackageManager().getLaunchIntentForPackage(applicationInfo.getAppPackage());
//        if (intent != null) {
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public void onAppInfoClick(AppInfos applicationInfo) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.app_detail_container, DetailFragment.newInstance()).addToBackStack(null).commit();
//    }
}
