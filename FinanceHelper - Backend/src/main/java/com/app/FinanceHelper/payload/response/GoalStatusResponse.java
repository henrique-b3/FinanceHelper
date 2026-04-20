package com.app.FinanceHelper.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoalStatusResponse {
    long current;
    long finished;
    long total;
}
