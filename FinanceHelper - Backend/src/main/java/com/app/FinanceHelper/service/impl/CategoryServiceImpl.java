package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.payload.dto.CategoryDTO;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import com.app.FinanceHelper.repository.CategoryRepository;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CategoryResponse createCategory(UUID userID, CategoryDTO categoryDTO) {

        if(userProfileRepository.existsById(userID)) return null; //Change to throw
        if(categoryRepository.existsByName(categoryDTO.getName())) return null; //Change to throw

        Category categoryToCreate = modelMapper.map(categoryDTO, Category.class);

        Category createdCategory = categoryRepository.save(categoryToCreate);

        return modelMapper.map(createdCategory, CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getAllCategories(UUID userID) {
        if(!userProfileRepository.existsById(userID)) return null;
        return categoryRepository.findAllByUserProfile_Id(userID);
    }
}
