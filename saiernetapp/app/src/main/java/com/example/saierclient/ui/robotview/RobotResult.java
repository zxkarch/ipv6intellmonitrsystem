package com.example.saierclient.ui.robotview;

import androidx.annotation.Nullable;

import com.example.saierclient.data.Result;


public class RobotResult {
    @Nullable
    private Result success;
    @Nullable
    private Integer error;
    @Nullable
    private String failure;

    RobotResult(@Nullable Integer error) {
        this.error = error;
    }

    RobotResult(@Nullable Result success) {
        this.success = success;
    }

    RobotResult(@Nullable String failure) {
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
