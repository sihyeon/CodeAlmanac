package com.example.teamalmanac.codealmanac.bean;

import android.view.View;

/**
 * Created by somin on 16. 12. 4.
 */

public class TodoDataType {
    private String todo;
    private String date;
    private String button_visibility;
    private boolean isShowing;

    public TodoDataType(String todo, String date, String button_visibility) {
        this.todo = todo;
        this.date = date;
        this.button_visibility = button_visibility;
        this.isShowing = true;
    }

    public TodoDataType(String todo, String date, boolean isShowing) {
        this.todo = todo;
        this.date = date;
        this.button_visibility = String.valueOf(View.INVISIBLE);
        this.isShowing = isShowing;
    }

    public TodoDataType(String todo, String date) {
        this.todo = todo;
        this.date = date;
        this.button_visibility = String.valueOf(View.INVISIBLE);
        this.isShowing = true;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public void setButton_visibility(String button_visibility) {
        this.button_visibility = button_visibility;
    }

    public String getTodo() { return todo; }

    public String getButton_visibility() {
        return button_visibility;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }
}
