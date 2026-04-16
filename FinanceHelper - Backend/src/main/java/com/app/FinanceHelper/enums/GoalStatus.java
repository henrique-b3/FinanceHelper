package com.app.FinanceHelper.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GoalStatus {
    UPCOMING("Upcoming"),
    ACTIVE("Active"),
    WARNING("Warning"),
    EXCEEDED("Exceeded"),
    COMPLETED("Completed"),
    FINISHED("Finished");

    private final String value;

    GoalStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
