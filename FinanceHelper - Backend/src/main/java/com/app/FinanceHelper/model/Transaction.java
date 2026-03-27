package com.app.FinanceHelper.model;

import jakarta.persistence.*;
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

    BigDecimal amount;

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
