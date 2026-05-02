package com.app.FinanceHelper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull(message = "Company name cannot be empty!")
    @Column(nullable = false)
    String name;

    String color;
    String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User profile cannot be empty!")
    UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @PrePersist
    public void defaultValues() {
        if (this.image == null || this.image.isBlank()) {
            this.image = "default.png";
        }

        if (this.color == null || this.color.isBlank()) {
            this.color = "#CCCCCC";
        }
    }
}
