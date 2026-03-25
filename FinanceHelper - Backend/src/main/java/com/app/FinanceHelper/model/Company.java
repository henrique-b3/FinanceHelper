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
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    String color;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
