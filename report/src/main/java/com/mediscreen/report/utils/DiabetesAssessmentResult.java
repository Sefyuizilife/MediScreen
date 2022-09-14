package com.mediscreen.report.utils;

public enum DiabetesAssessmentResult {

    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("In Danger"),
    EARLY_ONSET("Early Onset");

    private String result;

    DiabetesAssessmentResult(String result) {

        this.result = result;
    }

    public String getResult() {

        return this.result;
    }
}
