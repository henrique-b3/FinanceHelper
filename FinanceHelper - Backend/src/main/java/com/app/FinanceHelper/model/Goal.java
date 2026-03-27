package com.app.FinanceHelper.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String name;

    String icon;
    String color;

    BigDecimal limitAmount;

    LocalDate startDate;
    LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;


    @PrePersist
    public void defaultValues() {
        if (this.icon == null || this.icon.isBlank()) {
            this.icon = "icone_padrao.png";
        }

        if (this.color == null || this.color.isBlank()) {
            this.color = "#CCCCCC";
        }

        if (this.limitAmount == null || this.limitAmount.signum() < 0) {
            this.limitAmount = BigDecimal.ZERO;
        }
    }
}
