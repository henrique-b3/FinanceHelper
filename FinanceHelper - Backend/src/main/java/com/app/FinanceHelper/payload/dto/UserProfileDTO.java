package com.app.FinanceHelper.payload.dto;

import jakarta.validation.constraints.Email;
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
    @NotBlank(message = "User name cannot be empty!")
    @Size(min = 5, max = 50, message = "The name must has more than {min} and less than {max} characters")
    String name;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "Last name cannot be empty!")
    @Size(min = 3, max = 50, message = "The name must has more than {min} and less than {max} characters")
    String lastName;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 6, max = 50, message = "The name must has more than {min} and less than {max} characters")
    String password;
}
