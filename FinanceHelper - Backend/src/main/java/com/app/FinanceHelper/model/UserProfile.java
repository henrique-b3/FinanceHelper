package com.app.FinanceHelper.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String name;
    String lastName;

    @Column(nullable = false)
    String password;

    @OneToMany(mappedBy = "userProfile")
    List<Category> categoryList;

    @OneToMany(mappedBy = "userProfile")
    List<Transaction> transactions;

}
