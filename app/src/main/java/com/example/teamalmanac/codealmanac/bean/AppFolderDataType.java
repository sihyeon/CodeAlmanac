package com.example.teamalmanac.codealmanac.bean;

/**
 * Created by Choi Jaeung on 2016-12-09.
 */

public class AppFolderDataType {
    private long _id;
    private String app_name;
    private String app_path;

    public AppFolderDataType(long _id, String app_name, String app_path) {
        this._id = _id;
        this.app_name = app_name;
        this.app_path = app_path;
    }

    public long get_id() {
        return _id;
    }

    public String getApp_name() {
        return app_name;
    }

    public String getApp_path() {
        return app_path;
    }
}
