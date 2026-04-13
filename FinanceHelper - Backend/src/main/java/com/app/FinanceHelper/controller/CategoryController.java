package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.CategoryDTO;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import com.app.FinanceHelper.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @AuthenticationPrincipal UserProfile user,
            @Valid @RequestBody CategoryDTO categoryDTO
    ){
        CategoryResponse categoryResponse = categoryService.createCategory(user.getId(),categoryDTO);
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @GetMapping("/id/{categoryID}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID categoryID
    ){
        CategoryResponse categoryResponse = categoryService.getCategory(user.getId(),categoryID);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<CategoryResponse> getCategoryByName(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable String categoryName
    ){
        CategoryResponse categoryResponse = categoryService.getCategoryByName(user.getId(),categoryName);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<Set<CategoryResponse>> getAllCategories(
            @AuthenticationPrincipal UserProfile user
    ){
        Set<CategoryResponse> categoryResponse = categoryService.getAllCategories(user.getId());
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
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID categoryID
    ){
        return null;
    }

    @DeleteMapping("/deleteAllCategories")
    public ResponseEntity<List<CategoryResponse>> deleteAllCategories(){
        return null;
    }
}
