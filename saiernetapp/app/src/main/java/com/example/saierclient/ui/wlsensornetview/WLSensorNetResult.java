package com.example.saierclient.ui.wlsensornetview;

import androidx.annotation.Nullable;

import com.example.saierclient.data.Result;

public class WLSensorNetResult {
    @Nullable
    private Result success;
    @Nullable
    private Integer error;
    @Nullable
    private String failure;

    WLSensorNetResult(@Nullable Integer error) {
        this.error = error;
    }

    WLSensorNetResult(@Nullable Result success) {
        this.success = success;
    }

    WLSensorNetResult(@Nullable String failure) {
        this.failure = failure;
    }

    @Nullable
    Result getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

    @Nullable
    String getFailure() {
        return failure;
    }

}
