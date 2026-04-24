package com.app.FinanceHelper.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyResponse {
    UUID id;
    String name;
    String color;
    UUID categoryID;
}
