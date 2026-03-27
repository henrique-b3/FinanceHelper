package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.CategoryDTO;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface CategoryService {

    CategoryResponse createCategory(UUID userID, CategoryDTO categoryDTO);

    List<CategoryResponse> getAllCategories(UUID userID);

    CategoryResponse getCategoryById( UUID userID,UUID categoryID);

    CategoryResponse getCategoryByName( UUID userID,String categoryName);
}
