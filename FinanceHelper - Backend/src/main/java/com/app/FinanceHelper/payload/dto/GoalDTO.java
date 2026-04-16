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

    @NotBlank(message = "Goal Name cannot be empty!")
    @Size(min = 3, max = 50, message = "The name must has more than {min} and less than {max} characters")
    String name;

    String description;

    String icon;
    String color;

    /*@NotBlank(message = "Goal Name cannot be empty!")
    String type;*/ //Desabilitado temporariamente

    @NotNull(message = "Limit amount cannot be empty!")
    BigDecimal limitAmount;

    LocalDate startDate;
    LocalDate endDate;

    @NotNull(message = "Category ID cannot be empty!")
    UUID categoryID;
}
