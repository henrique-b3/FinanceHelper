package com.app.FinanceHelper.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileResponse {
    UUID id;
    String name;
    String lastName;
    String email;
}
