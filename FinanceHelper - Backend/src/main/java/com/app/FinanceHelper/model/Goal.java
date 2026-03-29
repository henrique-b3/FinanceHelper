package com.app.FinanceHelper.model;

import com.app.FinanceHelper.exceptions.APIexception;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Name cannot be empty!")
    @Column(nullable = false)
    String name;

    String icon;
    String color;

    @NotNull(message = "Limit amount cannot be empty!")
    @Column(nullable = false)
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

        if(this.startDate != null && this.endDate == null){
            throw new APIexception("End date cannot be null");
        } else if (this.startDate == null && this.endDate != null) {
            throw new APIexception("Start date cannot be null");
        }

        if(this.startDate != null && this.endDate != null && (this.startDate.isAfter(endDate) || this.startDate.isEqual(endDate))){
            throw new APIexception("Start date cannot be after end date");
        }
    }
}
