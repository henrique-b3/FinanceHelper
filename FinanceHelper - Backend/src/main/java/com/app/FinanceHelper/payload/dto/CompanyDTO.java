package com.app.FinanceHelper.payload.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyDTO {
    @NotBlank(message = "{company.name.notblank}")
    @Size(min = 2, max = 50, message = "{company.name.size}")
    private String name;

    @Size(max = 20, message = "{company.color.size}")
    private String color;

    @NotNull(message = "{company.category.notnull}")
    private UUID categoryID;
}
