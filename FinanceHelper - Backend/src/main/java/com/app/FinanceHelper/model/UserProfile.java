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

    @OneToMany(mappedBy = "userProfile")
    List<Company> companies;

}
