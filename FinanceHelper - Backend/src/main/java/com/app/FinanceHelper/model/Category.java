package com.app.FinanceHelper.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    String description;
    String image;
    String color;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserProfile userProfile;

    @OneToMany(mappedBy = "category")
    List<Transaction> transactions;

    @PrePersist
    public void defaultValues() {
        if (this.image == null || this.image.isBlank()) {
            this.image = "icone_padrao.png";
        }

        if (this.color == null || this.color.isBlank()) {
            this.color = "#CCCCCC";
        }
    }

}
