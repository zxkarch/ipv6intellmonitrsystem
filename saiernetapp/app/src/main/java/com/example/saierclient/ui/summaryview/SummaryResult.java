package com.example.saierclient.ui.summaryview;

import androidx.annotation.Nullable;

import com.example.saierclient.data.Result;


public class SummaryResult {
    @Nullable
    private Result success;
    @Nullable
    private Integer error;
    @Nullable
    private String failure;

    SummaryResult(@Nullable Integer error) {
        this.error = error;
    }

    SummaryResult(@Nullable Result success) {
        this.success = success;
    }

    SummaryResult(@Nullable String failure) {
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
