package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.CategoryDTO;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface CategoryService {

    CategoryResponse createCategory(UUID userID, CategoryDTO categoryDTO);

    Set<CategoryResponse> getAllCategories(UUID userID);

    CategoryResponse getCategory(UUID userID, UUID categoryID);

    CategoryResponse getCategoryByName( UUID userID,String categoryName);

    CategoryResponse deleteCategory(UUID userID, UUID categoryID);

    List<CategoryResponse> getCategoriesByName(UUID id, String categoryName);

    CategoryResponse updateCategory(UUID userID, UUID categoryID, CategoryDTO categoryDTO);
}
