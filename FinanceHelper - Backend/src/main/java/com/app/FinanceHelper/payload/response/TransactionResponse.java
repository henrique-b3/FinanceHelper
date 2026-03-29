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
public class TransactionResponse {

    UUID id;
    String description;
    BigDecimal amount;
    LocalDate transaction_date;

    UUID userID;
    UUID companyID;
    UUID categoryID;
}
