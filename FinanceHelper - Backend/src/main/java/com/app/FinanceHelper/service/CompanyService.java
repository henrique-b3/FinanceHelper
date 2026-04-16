package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.CompanyDTO;
import com.app.FinanceHelper.payload.response.CompanyResponse;

import java.util.Set;
import java.util.UUID;


public interface CompanyService {
    CompanyResponse createCompany(UUID userID, CompanyDTO companyDTO);

    CompanyResponse deleteCompany(UUID userID, UUID companyID);

    CompanyResponse getCompany(UUID userID, UUID companyID);

    Set<CompanyResponse> getAllCompanies(UUID userID);
}
