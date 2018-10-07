package com.example.tintr.listallapp;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppInfos implements Parcelable {

    private String appName;
    private String appPackage;
    private String appVersion;
    private String appStorage;
    private String appListPermission;
    private String appInstallDay;
    private String appUpdatedDay;
    private String appCHPlay;
    private Drawable appIcon;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppStorage() {
        return appStorage;
    }

    public void setAppStorage(String appStorage) {
        this.appStorage = appStorage;
    }

    public String getAppListPermission() {
        return appListPermission;
    }

    public void setAppListPermission(String appListPermission) {
        this.appListPermission = appListPermission;
    }

    public String getAppInstallDay() {
        return appInstallDay;
    }

    public void setAppInstallDay(String appInstallDay) {
        this.appInstallDay = appInstallDay;
    }

    public String getAppUpdatedDay() {
        return appUpdatedDay;
    }

    public void setAppUpdatedDay(String appUpdatedDay) {
        this.appUpdatedDay = appUpdatedDay;
    }

    public String getAppCHPlay() {
        return appCHPlay;
    }

    public void setAppCHPlay(String appCHPlay) {
        this.appCHPlay = appCHPlay;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public AppInfos() {

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(appName);
        parcel.writeString(appPackage);
        parcel.writeString(appVersion);
        parcel.writeString(appStorage);
        parcel.writeString(appListPermission);
        parcel.writeString(appInstallDay);
        parcel.writeString(appUpdatedDay);
        parcel.writeString(appCHPlay);
    }

    protected AppInfos(Parcel in) {
        appName = in.readString();
        appPackage = in.readString();
        appVersion = in.readString();
        appStorage = in.readString();
        appListPermission = in.readString();
        appInstallDay = in.readString();
        appUpdatedDay = in.readString();
        appCHPlay = in.readString();
    }

    public static final Creator<AppInfos> CREATOR = new Creator<AppInfos>() {
        @Override
        public AppInfos createFromParcel(Parcel in) {
            return new AppInfos(in);
        }

        @Override
        public AppInfos[] newArray(int size) {
            return new AppInfos[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }
}
