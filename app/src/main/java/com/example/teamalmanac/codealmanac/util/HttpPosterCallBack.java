package com.example.teamalmanac.codealmanac.util;

/**
 * Created by somin on 16. 12. 6.
 */

public interface HttpPosterCallBack {
    void onSuccess(String response);
    void onError(Throwable error);
}
