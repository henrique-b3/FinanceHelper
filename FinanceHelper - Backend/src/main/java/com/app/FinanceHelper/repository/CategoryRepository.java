package com.app.FinanceHelper.repository;

import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    public Boolean existsByName(String name);

    List<Category> findAllByUserProfile_Id(UUID userID);
    Optional<Category> findByIdAndUserProfile_Id(UUID categoryID, UUID userID);
    Optional<Category> findByNameAndUserProfile_Id(String name, UUID userID);
    boolean existsByNameAndUserProfile_Id(String name, UUID userID);
}
