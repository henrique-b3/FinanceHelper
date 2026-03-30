package com.app.FinanceHelper.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoalResponse {
    UUID id;
    String name;
    String icon;
    String color;

    String status;

    LocalDate startDate;
    LocalDate endDate;

    BigDecimal limitAmount;
    BigDecimal spendAmount;
    BigDecimal remainingAmount;
}
