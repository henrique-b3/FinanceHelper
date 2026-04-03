package com.app.FinanceHelper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_users")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull(message = "User name cannot be empty!")
    @Column(nullable = false)
    String name;

    @NotNull(message = "Last name cannot be empty!")
    @Column(nullable = false)
    String lastName;

    @NotNull(message = "UserProfile cannot be empty!")
    @Column(nullable = false)
    @Email
    String email;

    @NotNull(message = "Password cannot be empty!")
    @Column(nullable = false)
    String password;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Category> categoryList = new HashSet<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Company> companies = new HashSet<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Goal> goals = new HashSet<>();

}
