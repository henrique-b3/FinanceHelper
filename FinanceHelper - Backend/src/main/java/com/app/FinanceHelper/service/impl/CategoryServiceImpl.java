package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.CategoryDTO;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import com.app.FinanceHelper.repository.CategoryRepository;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    UserProfileRepository userProfileRepository;
    ModelMapper modelMapper;

    @Override
    public CategoryResponse createCategory(UUID userID, CategoryDTO categoryDTO) {

        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userID", userID));


        if(categoryRepository.existsByName(categoryDTO.getName())){
            throw new APIexception("Category already exists with name: " + categoryDTO.getName());
        }

        Category categoryToCreate = modelMapper.map(categoryDTO, Category.class);
        categoryToCreate.setUserProfile(user);

        Category createdCategory = categoryRepository.save(categoryToCreate);

        return modelMapper.map(createdCategory, CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getAllCategories(UUID userID) {
        if(!userProfileRepository.existsById(userID)){
            throw new ResourceNotFoundException("UserProfile","userID", userID);
        }

        return categoryRepository.findAllByUserProfile_Id(userID)
                .stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(UUID userID, UUID categoryID) {
        if (!userProfileRepository.existsById(userID)) {
            throw new ResourceNotFoundException("UserProfile", "userID", userID);
        }

        Category category = categoryRepository
                .findByIdAndUserProfile_Id(categoryID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));

        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse getCategoryByName(UUID userID, String categoryName) {
        if (!userProfileRepository.existsById(userID)) {
            throw new ResourceNotFoundException("UserProfile", "userID", userID);
        }

        Category category = categoryRepository
                .findByNameAndUserProfile_Id(categoryName, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", categoryName));

        return modelMapper.map(category, CategoryResponse.class);
    }
}
