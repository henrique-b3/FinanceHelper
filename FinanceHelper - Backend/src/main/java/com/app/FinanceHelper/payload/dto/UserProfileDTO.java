package com.app.FinanceHelper.payload.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDTO {
    @NotBlank(message = "{user.name.notblank}")
    @Size(min = 5, max = 50, message = "{user.name.size}")
    private String name;

    @NotBlank(message = "{user.lastname.notblank}")
    @Size(min = 3, max = 50, message = "{user.lastname.size}")
    private String lastName;

    @NotBlank(message = "{user.email.notblank}")
    @Email(message = "{user.email.invalid}")
    private String email;

    @NotBlank(message = "{user.pwd.notblank}")
    @Size(min = 6, max = 20, message = "{user.pwd.size}")
    private String password;
}
