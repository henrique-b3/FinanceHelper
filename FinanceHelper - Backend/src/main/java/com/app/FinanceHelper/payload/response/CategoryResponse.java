package com.app.FinanceHelper.payload.response;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryResponse {
    UUID id;
    String name;
    String description;
    String image;
    String color;
    Integer transactionsCount;
}
