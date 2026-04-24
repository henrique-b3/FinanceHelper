package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.CategoryDTO;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import com.app.FinanceHelper.repository.CategoryRepository;
import com.app.FinanceHelper.repository.TransactionRepository;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public CategoryResponse createCategory(UUID userID, CategoryDTO categoryDTO) {

        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userID", userID));

        if(categoryRepository.existsByNameAndUserProfile_Id(categoryDTO.getName(), userID)){
            throw new APIexception("Category already exists with name: " + categoryDTO.getName());
        }

        Category categoryToCreate = modelMapper.map(categoryDTO, Category.class);
        categoryToCreate.setUserProfile(user);

        Category createdCategory = categoryRepository.save(categoryToCreate);

        return modelMapper.map(createdCategory, CategoryResponse.class);
    }

    @Override
    public CategoryResponse getCategory(UUID userID, UUID categoryID) {

        Category category = categoryRepository
                .findByIdAndUserProfile_Id(categoryID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));

        Integer count = transactionRepository.countByUserProfile_IdAndCategoryId(userID, category.getId());

        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);

        categoryResponse.setTransactionsCount(count != null ? count : 0);

        return categoryResponse;
    }

    @Override
    public Set<CategoryResponse> getAllCategories(UUID userID) {

        return categoryRepository.findAllByUserProfile_Id(userID)
                .stream()
                .map(category -> {
                    CategoryResponse response = modelMapper.map(category, CategoryResponse.class);

                    Integer count = transactionRepository.countByUserProfile_IdAndCategoryId(userID, category.getId());

                    response.setTransactionsCount(count != null ? count : 0);

                    return response;
                })
                .collect(Collectors.toSet());
    }


    @Override
    public CategoryResponse getCategoryByName(UUID userID, String categoryName) {

        Category category = categoryRepository
                .findByNameAndUserProfile_Id(categoryName, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", categoryName));

        Integer count = transactionRepository.countByUserProfile_IdAndCategoryId(userID, category.getId());

        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);

        categoryResponse.setTransactionsCount(count != null ? count : 0);

        return categoryResponse;
    }

    @Override
    public List<CategoryResponse> getCategoriesByName(UUID id, String categoryName) {
        List<Category> categories = categoryRepository.findByNameStartingWithIgnoreCaseAndUserProfile_Id(categoryName, id);

        return categories.stream()
                .map(category -> {
                    CategoryResponse response = modelMapper.map(category, CategoryResponse.class);

                    Integer count = transactionRepository.countByUserProfile_IdAndCategoryId(id, category.getId());
                    response.setTransactionsCount(count != null ? count : 0);

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse deleteCategory(UUID userID, UUID categoryID) {
        Category category = categoryRepository
                .findByIdAndUserProfile_Id(categoryID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));

        categoryRepository.deleteById(categoryID);

        return modelMapper.map(category, CategoryResponse.class);
    }


}
