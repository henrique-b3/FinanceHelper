package com.app.FinanceHelper.repository;

import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    public Boolean existsByName(String name);

    List<CategoryResponse> findAllByUserProfile_Id(UUID userID);
}
