package com.app.FinanceHelper.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryExpenseResponse {
    private String name;
    private String color;
    private BigDecimal value;
}