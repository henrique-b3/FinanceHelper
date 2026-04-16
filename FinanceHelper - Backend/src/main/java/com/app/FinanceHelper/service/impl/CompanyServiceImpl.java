package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.model.Company;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.CompanyDTO;
import com.app.FinanceHelper.payload.response.CompanyResponse;
import com.app.FinanceHelper.repository.CategoryRepository;
import com.app.FinanceHelper.repository.CompanyRepository;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
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
        companyToCreate.setId(null);
        companyToCreate.setCategory(category);
        companyToCreate.setUserProfile(userProfile);

        companyRepository.save(companyToCreate);

        CompanyResponse companyResponse = modelMapper.map(companyToCreate, CompanyResponse.class);
        companyResponse.setCategoryID(category.getId());
        companyResponse.setUserID(userProfile.getId());

        return companyResponse;
    }


    @Override
    public CompanyResponse getCompany(UUID userID, UUID companyID) {

        if(!userProfileRepository.existsById(userID)){
            throw new ResourceNotFoundException("UserProfile","userID", userID);
        }

        Company company = companyRepository.findByIdAndUserProfile_Id(companyID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyID", companyID));

        CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
        companyResponse.setCategoryID(company.getCategory().getId());
        companyResponse.setUserID(company.getUserProfile().getId());

        return companyResponse;
    }

    @Override
    public Set<CompanyResponse> getAllCompanies(UUID userID) {
        if(!userProfileRepository.existsById(userID)){
            throw new ResourceNotFoundException("UserProfile","userID", userID);
        }

        return companyRepository.findAllByUserProfile_Id(userID)
                .stream()
                .map(company -> modelMapper.map(company, CompanyResponse.class))
                .collect(Collectors.toSet());
    }

    @Override
    public CompanyResponse deleteCompany(UUID userID, UUID companyID) {
        Company company = companyRepository.findByIdAndUserProfile_Id(companyID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyID", companyID));

        companyRepository.delete(company);

        CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
        companyResponse.setCategoryID(company.getCategory().getId());
        companyResponse.setUserID(company.getUserProfile().getId());

        return companyResponse;
    }

}
