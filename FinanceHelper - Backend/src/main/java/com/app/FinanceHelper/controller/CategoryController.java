package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.payload.dto.CategoryDTO;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import com.app.FinanceHelper.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category/{userID}")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @PathVariable UUID userID,
            @Valid @RequestBody CategoryDTO categoryDTO
    ){
        CategoryResponse categoryResponse = categoryService.createCategory(userID,categoryDTO);
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @GetMapping("/categoryById/{categoryID}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable UUID userID,
            @PathVariable UUID categoryID
    ){
        CategoryResponse categoryResponse = categoryService.getCategory(userID,categoryID);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/categoryByName/{categoryName}")
    public ResponseEntity<CategoryResponse> getCategoryByName(
            @PathVariable UUID userID,
            @PathVariable String categoryName
    ){
        CategoryResponse categoryResponse = categoryService.getCategoryByName(userID,categoryName);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @PathVariable UUID userID
    ){
        List<CategoryResponse> categoryResponse = categoryService.getAllCategories(userID);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PutMapping("/update/{categoryID}")
    public ResponseEntity<CategoryResponse> updateCategory(
           @Valid @RequestBody CategoryDTO categoryDTO
    ){
        return null;
    }

    @PutMapping("/image/{categoryID}")
    public ResponseEntity<CategoryResponse> updateImage(){
        return null;
    }

    @PutMapping("/color/{categoryID}")
    public ResponseEntity<CategoryResponse> updateColor(){
        return null;
    }

    @DeleteMapping("/{categoryID}")
    public ResponseEntity<CategoryResponse> deleteCategory(
            @PathVariable UUID userID,
            @PathVariable UUID categoryID
    ){
        return null;
    }

    @DeleteMapping("/deleteAllCategories")
    public ResponseEntity<List<CategoryResponse>> deleteAllCategories(){
        return null;
    }
}
