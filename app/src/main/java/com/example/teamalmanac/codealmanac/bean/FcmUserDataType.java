package com.example.teamalmanac.codealmanac.bean;

/**
 * Created by somin on 16. 12. 7.
 */

public class FcmUserDataType {
    String uuid;
    String reg_id;

    public FcmUserDataType(String uuid, String reg_id) {
        this.uuid = uuid;
        this.reg_id = reg_id;
    }

    public FcmUserDataType() {
        uuid = null;
        reg_id = null;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }
}
