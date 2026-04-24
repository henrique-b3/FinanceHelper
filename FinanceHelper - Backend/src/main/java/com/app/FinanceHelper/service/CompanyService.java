package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.CompanyDTO;
import com.app.FinanceHelper.payload.response.CompanyResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface CompanyService {
    CompanyResponse createCompany(UUID userID, CompanyDTO companyDTO);

    CompanyResponse deleteCompany(UUID userID, UUID companyID);

    CompanyResponse getCompany(UUID userID, UUID companyID);

    Set<CompanyResponse> getAllCompanies(UUID userID);

    List<CompanyResponse> getAllCompaniesByName(UUID id, String companyName);

    CompanyResponse updateCompany(UUID userID, UUID companyID, CompanyDTO companyDTO);
}
