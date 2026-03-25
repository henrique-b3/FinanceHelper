package com.app.FinanceHelper.payload.dto;

import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.model.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDTO {
    UUID id;
    String name;
    String lastName;
    String password;
}
