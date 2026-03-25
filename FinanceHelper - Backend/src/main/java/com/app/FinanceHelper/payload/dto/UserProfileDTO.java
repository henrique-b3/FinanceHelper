package com.app.FinanceHelper.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDTO {
    UUID id;

    @NotBlank(message = "Category Name cannot be empty!")
    @Size(min = 3, max = 50, message = "The name must has more than {min} and less than {max} characters")
    String name;

    @NotBlank(message = "Category Name cannot be empty!")
    @Size(min = 3, max = 50, message = "The name must has more than {min} and less than {max} characters")
    String lastName;

    @NotBlank(message = "Category Name cannot be empty!")
    @Size(min = 3, max = 50, message = "The name must has more than {min} and less than {max} characters")
    String password;
}
