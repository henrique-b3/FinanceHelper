package com.app.FinanceHelper.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String description;

    @NotNull(message = "{transaction.amount.notnull}")
    private BigDecimal amount;

    @NotNull(message = "{transaction.date.notnull}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    // A transação deve pertencer a uma categoria
    @NotNull(message = "{transaction.category.notnull}")
    private UUID categoryID;

    private UUID companyID;
}
