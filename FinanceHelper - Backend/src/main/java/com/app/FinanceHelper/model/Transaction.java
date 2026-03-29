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

    @NotBlank(message = "Amount cannot be empty!")
    @Column(nullable = false)
    BigDecimal amount;

    @NotBlank(message = "Transaction date cannot be empty!")
    @Column(nullable = false)
    LocalDate transaction_date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotBlank(message = "UserProfile cannot be empty!")
    @Column(nullable = false)
    UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @NotBlank(message = "Company cannot be empty!")
    @Column(nullable = false)
    Company company;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotBlank(message = "Category cannot be empty!")
    @Column(nullable = false)
    Category category;
}
