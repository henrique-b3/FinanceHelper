package com.app.FinanceHelper.model;

import com.app.FinanceHelper.enums.GoalStatus;
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

    String description;

    String icon;
    String color;

    @Transient
    public GoalStatus getStatus() {
        boolean isExceeded = spendAmount != null && limitAmount != null && spendAmount.compareTo(limitAmount) > 0;

        if (isExceeded) {
            return GoalStatus.EXCEEDED;
        }

        LocalDate today = LocalDate.now();

        if (startDate != null && startDate.isAfter(today)) {
            return GoalStatus.UPCOMING;
        }

        if (endDate != null && endDate.isBefore(today)) {
            return GoalStatus.FINISHED;
        }

        return GoalStatus.ACTIVE;
    }

    @NotNull(message = "Limit amount cannot be empty!")
    @Column(nullable = false)
    BigDecimal limitAmount;

    @Transient
    BigDecimal spendAmount;

    @Transient
    public BigDecimal getRemainingAmount() {
        if (limitAmount == null || spendAmount == null) {
            return BigDecimal.ZERO;
        }
        return limitAmount.subtract(spendAmount);
    }

    LocalDate startDate;
    LocalDate endDate;


    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category cannot be empty!")
    Category category;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User cannot be empty!")
    UserProfile userProfile;


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
            throw new APIexception("A data final não pode ser nula");
        } else if (this.startDate == null && this.endDate != null) {
            throw new APIexception("A data inicial não pode ser nula");
        }

        if(this.startDate != null && this.endDate != null && (this.startDate.isAfter(endDate) || this.startDate.isEqual(endDate))){
            throw new APIexception("A data de inicio não pode ser após a data final");
        }
    }
}
