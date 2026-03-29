package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.model.Company;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.CompanyDTO;
import com.app.FinanceHelper.payload.response.CategoryResponse;
import com.app.FinanceHelper.payload.response.CompanyResponse;
import com.app.FinanceHelper.repository.CategoryRepository;
import com.app.FinanceHelper.repository.CompanyRepository;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepository;
    CategoryRepository categoryRepository;
    UserProfileRepository userProfileRepository;
    ModelMapper modelMapper;


    @Override
    public CompanyResponse createCompany(UUID userID, CompanyDTO companyDTO) {

        UserProfile userProfile = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userID", userID));

        Category category = categoryRepository.findByIdAndUserProfile_Id(companyDTO.getCategoryID(), userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", companyDTO.getCategoryID()));

        if (companyRepository.existsByNameAndUserProfile_Id(companyDTO.getName(), userID)) {
            throw new APIexception("Company already exists with name: " + companyDTO.getName());
        }

        Company companyToCreate = modelMapper.map(companyDTO, Company.class);
        companyToCreate.setCategory(category);
        companyToCreate.setUserProfile(userProfile);

        companyRepository.save(companyToCreate);

        return modelMapper.map(companyToCreate, CompanyResponse.class);
    }


    @Override
    public CompanyResponse getCompany(UUID userID, UUID companyID) {
        Company company = companyRepository.findByIdAndUserProfile_Id(companyID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyID", companyID));

        return modelMapper.map(company, CompanyResponse.class);
    }

    @Override
    public CompanyResponse deleteCompany(UUID userID, UUID companyID) {
        Company company = companyRepository.findByIdAndUserProfile_Id(companyID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyID", companyID));

        companyRepository.delete(company);
        return modelMapper.map(company, CompanyResponse.class);
    }

}
