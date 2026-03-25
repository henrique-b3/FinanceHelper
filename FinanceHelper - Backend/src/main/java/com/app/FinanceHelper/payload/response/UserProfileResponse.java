package com.app.FinanceHelper.payload.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UserProfileResponse {
    UUID id;
    String name;
    String lastName;
}
