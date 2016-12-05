package com.example.teamalmanac.codealmanac.bean;

import android.view.View;

/**
 * Created by somin on 16. 12. 5.
 */

public class MainfocusDataType {
    private String mainfocus;
    private String date;
    private String button_visibility;

    public MainfocusDataType(String mainfocus, String date) {
        this.mainfocus = mainfocus;
        this.date = date;
        this.button_visibility = String.valueOf(View.INVISIBLE);
    }

    public MainfocusDataType(String mainfocus, String date, String button_visibility) {
        this.mainfocus = mainfocus;
        this.date = date;
        this.button_visibility = button_visibility;
    }

    public MainfocusDataType() {
        mainfocus = null;
        date = null;
    }

    public String getMainfocus() {
        return mainfocus;
    }

    public void setMainfocus(String mainfocus) {
        this.mainfocus = mainfocus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getButton_visibility() {
        return button_visibility;
    }

    public void setButton_visibility(String button_visibility) {
        this.button_visibility = button_visibility;
    }
}
