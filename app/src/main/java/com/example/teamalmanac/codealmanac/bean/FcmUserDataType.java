package com.example.teamalmanac.codealmanac.bean;

/**
 * Created by somin on 16. 12. 7.
 */

public class FcmUserDataType {
    String token;

    public FcmUserDataType(String token) {
        this.token = token;
    }

    public FcmUserDataType() {
        token = null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
