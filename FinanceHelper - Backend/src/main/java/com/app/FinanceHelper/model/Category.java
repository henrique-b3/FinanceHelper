package com.app.FinanceHelper.model;

import jakarta.persistence.*;
import lombok.*;

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
}
