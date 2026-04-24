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

        return companyResponse;
    }


    @Override
    public CompanyResponse getCompany(UUID userID, UUID companyID) {

        Company company = companyRepository.findByIdAndUserProfile_Id(companyID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyID", companyID));

        CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
        companyResponse.setCategoryID(company.getCategory().getId());

        return companyResponse;
    }

    @Override
    public Set<CompanyResponse> getAllCompanies(UUID userID) {

        return companyRepository.findAllByUserProfile_Id(userID)
                .stream()
                .map(company -> {
                    CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
                    companyResponse.setCategoryID(company.getCategory().getId());
                    return companyResponse;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public List<CompanyResponse> getAllCompaniesByName(UUID userID, String companyName) {
        return companyRepository.findByNameStartingWithIgnoreCaseAndUserProfile_Id(companyName, userID)
                .stream()
                .map(company -> {
                    CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
                    companyResponse.setCategoryID(company.getCategory().getId());
                    return companyResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CompanyResponse updateCompany(UUID userID,UUID companyID, CompanyDTO companyDTO) {
        Company company = companyRepository.findByIdAndUserProfile_Id(companyID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyID", companyID));

        if(companyDTO.getName() != null && !companyDTO.getName().equals(company.getName())){
            company.setName(company.getName());
        }

        if(companyDTO.getColor() != null && !companyDTO.getColor().equals(company.getColor())){
            company.setColor(company.getColor());
        }

        if(companyDTO.getCategoryID() != null && !companyDTO.getCategoryID().equals(company.getCategory().getId())){
            Category category = categoryRepository.findByIdAndUserProfile_Id(companyDTO.getCategoryID(), userID)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", companyDTO.getCategoryID()));

            company.setCategory(category);
        }

        Company savedCompany = companyRepository.save(company);

        CompanyResponse companyResponse = modelMapper.map(savedCompany, CompanyResponse.class);
        companyResponse.setCategoryID(company.getCategory().getId());

        return companyResponse;
    }

    @Override
    public CompanyResponse deleteCompany(UUID userID, UUID companyID) {
        Company company = companyRepository.findByIdAndUserProfile_Id(companyID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyID", companyID));

        companyRepository.delete(company);

        CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
        companyResponse.setCategoryID(company.getCategory().getId());

        return companyResponse;
    }

}
