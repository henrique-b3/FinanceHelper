package com.app.FinanceHelper.payload.dto;

import java.time.LocalDate;
import java.util.UUID;

public record GoalFilterDTO(String name, UUID companyID, UUID categoryID, String status, LocalDate startDate, LocalDate endDate, String orderBy, String direction) {
}
