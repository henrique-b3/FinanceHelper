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
public class TransactionDTO {
    String description;

    @NotNull(message = "Transaction amount cannot be empty!")
    BigDecimal amount;

    @NotNull(message = "Transaction date cannot be empty!")
    LocalDate transactionDate;

    UUID companyID;

    @NotNull(message = "Category ID cannot be empty!")
    UUID categoryID;
}
