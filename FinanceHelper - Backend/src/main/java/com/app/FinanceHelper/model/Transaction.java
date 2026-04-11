package com.app.FinanceHelper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String description;

    @NotNull(message = "Amount cannot be empty!")
    @Column(nullable = false)
    BigDecimal amount;

    @NotNull(message = "Transaction date cannot be empty!")
    @Column(nullable = false)
    LocalDate transaction_date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "UserProfile cannot be empty!")
    UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @NotNull(message = "Company cannot be empty!")
    Company company;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category cannot be empty!")
    Category category;
}
