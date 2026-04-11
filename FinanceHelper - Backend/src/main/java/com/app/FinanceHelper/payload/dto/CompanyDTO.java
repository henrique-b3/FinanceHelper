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

    @NotBlank(message = "Company name cannot be empty!")
    @Size(min = 2, max = 50, message = "The name must have between {min} and {max} characters")
    String name;

    @Size(max = 20, message = "Color must have at most {max} characters")
    String color;

    @NotNull(message = "Category ID is required!")
    UUID categoryID;
}
