package com.example.tintr.listallapp;

public class BusEvent {

    private AppInfos appInfo;
    private String event;

    BusEvent(String event, AppInfos appInfo) {
        this.appInfo = appInfo;
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public AppInfos getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfos appInfo) {
        this.appInfo = appInfo;
    }
}
