package com.app.FinanceHelper.payload.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Goal Name cannot be empty!")
    @Size(min = 3, max = 50, message = "The name must has more than {min} and less than {max} characters")
    String name;

    String icon;
    String color;

    @NotBlank(message = "Goal Name cannot be empty!")
    String type;

    String status;

    @NotBlank(message = "Limit amount cannot be empty!")
    BigDecimal limitAmount;

    LocalDate startDate;
    LocalDate endDate;
}
