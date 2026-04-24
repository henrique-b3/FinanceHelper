package com.app.FinanceHelper.payload.dto;

import java.time.LocalDate;
import java.util.UUID;

public record TransactionFilterDTO(String description, UUID companyID, UUID categoryID, LocalDate startDate, LocalDate endDate, String orderBy, String direction) {
}
