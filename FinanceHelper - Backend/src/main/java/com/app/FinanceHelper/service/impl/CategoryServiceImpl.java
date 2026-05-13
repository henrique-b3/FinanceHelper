package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.helper.ImageSaver;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
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
    public CategoryResponse createCategory(UUID userID, CategoryDTO categoryDTO, MultipartFile file) {

        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userID", userID));

        if(categoryRepository.existsByNameAndUserProfile_Id(categoryDTO.getName(), userID)){
            throw new APIexception("Já existe uma categoria com esse nome");
        }

        Category categoryToCreate = modelMapper.map(categoryDTO, Category.class);
        categoryToCreate.setUserProfile(user);

        String fileName = ImageSaver.saveImageLocally(file);
        if (fileName != null) categoryToCreate.setImage(fileName);

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

        List<Category> categories = categoryRepository.findAllByUserProfile_Id(userID);

        if (categories.isEmpty()) return Set.of();

        List<TransactionRepository.CategoryCountProjection> counts =
                transactionRepository.countTransactionsPerCategoryByUser(userID);

        Map<UUID, Integer> countsMap = counts.stream()
                .collect(Collectors.toMap(
                        TransactionRepository.CategoryCountProjection::getCategoryId,
                        TransactionRepository.CategoryCountProjection::getTransactionCount
                ));

        return categories.stream()
                .map(category -> {
                    CategoryResponse response = modelMapper.map(category, CategoryResponse.class);
                    response.setTransactionsCount(countsMap.getOrDefault(category.getId(), 0));
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

        if(categories.isEmpty()){
            throw new ResourceNotFoundException("Category", "name", categoryName);
        }

        List<UUID> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
        List<TransactionRepository.CategoryCountProjection> counts = transactionRepository.countTransactionsForCategoryIds(categoryIds);

        Map<UUID, Integer> countsMap = counts.stream()
                .collect(Collectors.toMap(
                        TransactionRepository.CategoryCountProjection::getCategoryId,
                        TransactionRepository.CategoryCountProjection::getTransactionCount
                ));

        return categories.stream()
                .map(category -> {
                    CategoryResponse response = modelMapper.map(category, CategoryResponse.class);
                    response.setTransactionsCount(countsMap.getOrDefault(category.getId(), 0));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse updateCategory(UUID userID, UUID categoryID, CategoryDTO categoryDTO, MultipartFile file) {
        Category category = categoryRepository
                .findByIdAndUserProfile_Id(categoryID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));

        if(categoryDTO.getName() != null && !categoryDTO.getName().isBlank() && !categoryDTO.getName().equalsIgnoreCase(category.getName())){
            category.setName(categoryDTO.getName());
        }

        if(categoryDTO.getColor() != null && !categoryDTO.getColor().isBlank() && !categoryDTO.getColor().equalsIgnoreCase(category.getColor())){
            category.setColor(categoryDTO.getColor());
        }

        if(categoryDTO.getDescription() != null && !categoryDTO.getDescription().isBlank() && !categoryDTO.getDescription().equalsIgnoreCase(category.getDescription())){
            category.setDescription(categoryDTO.getDescription());
        }

        if(categoryDTO.getImage() != null && !categoryDTO.getImage().isBlank() && !categoryDTO.getImage().equalsIgnoreCase(category.getImage())){
            String fileName = ImageSaver.saveImageLocally(file);
            if (fileName != null) category.setImage(fileName);
        }

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryResponse.class);
    }

    @Override
    public CategoryResponse deleteCategory(UUID userID, UUID categoryID) {
        Category category = categoryRepository
                .findByIdAndUserProfile_Id(categoryID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));

        try {
            categoryRepository.deleteById(categoryID);
            categoryRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new com.app.FinanceHelper.exceptions.DataIntegrityViolationException(category.getName(), "Category");
        }

        return modelMapper.map(category, CategoryResponse.class);
    }
}
