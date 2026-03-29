package com.app.FinanceHelper.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TransactionDTO {

    String description;

    @NotBlank(message = "Transaction amount cannot be empty!")
    BigDecimal amount;

    @NotBlank(message = "Transaction date cannot be empty!")
    LocalDate transaction_date;

    @NotBlank(message = "Company ID cannot be empty!")
    UUID companyID;

    @NotBlank(message = "Category ID cannot be empty!")
    UUID categoryID;
}
