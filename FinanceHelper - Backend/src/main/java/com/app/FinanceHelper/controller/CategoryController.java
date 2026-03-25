package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.payload.dto.CategoryDTO;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import com.app.FinanceHelper.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories/{userID}")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader UUID userID,
            @RequestBody CategoryDTO categoryDTO
            ){
        CategoryResponse createdCategory = categoryService.createCategory(userID,categoryDTO);
        return new ResponseEntity<CategoryResponse>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestHeader UUID userID
    ){
        List<CategoryResponse> allCategories = categoryService.getAllCategories(userID);
        return new ResponseEntity<List<CategoryResponse>>(allCategories, HttpStatus.CREATED);
    }

    @GetMapping("/{categoryID}")
    public ResponseEntity<CategoryResponse> getCategory(){
        return null;
    }

    @PutMapping("/{categoryID}")
    public ResponseEntity<CategoryResponse> updateCategory(){
        return null;
    }

    @DeleteMapping("/{categoryID}")
    public ResponseEntity<CategoryResponse> deleteCategory(){
        return null;
    }

    @DeleteMapping("/deleteAllCategories")
    public ResponseEntity<List<CategoryResponse>> deleteAllCategories(){
        return null;
    }
}
