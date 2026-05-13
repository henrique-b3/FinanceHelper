package com.app.FinanceHelper.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoalDTO {
    @NotBlank(message = "{goal.name.notblank}")
    @Size(min = 3, max = 50, message = "{goal.name.size}")
    private String name;

    private String description;

    private String icon;
    private String color;

    @NotNull(message = "{goal.limit.notnull}")
    private BigDecimal limitAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    private UUID categoryID;
    private UUID companyID;
}
