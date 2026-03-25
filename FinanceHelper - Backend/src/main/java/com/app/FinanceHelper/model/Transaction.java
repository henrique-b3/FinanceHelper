package com.app.FinanceHelper.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String description;
    @Column(name = "amount")
    BigDecimal value;

    @Column(name = "transaction_date")
    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
