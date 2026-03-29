package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.payload.dto.CompanyDTO;
import com.app.FinanceHelper.payload.response.CompanyResponse;
import com.app.FinanceHelper.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/company/{userID}")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @PathVariable UUID userID,
            @Valid @RequestBody CompanyDTO companyDTO
    ){
        CompanyResponse createdCompany = companyService.createCompany(userID, companyDTO);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }

    @GetMapping("/{companyID}")
    public ResponseEntity<CompanyResponse> getCompanyById(
            @PathVariable UUID userID,
            @PathVariable UUID companyID
    ){
        CompanyResponse searchCompany = companyService.getCompany(userID, companyID);
        return new ResponseEntity<>(searchCompany, HttpStatus.OK);
    }

    @DeleteMapping("/{companyID}")
    public ResponseEntity<CompanyResponse> deleteCompanyById(
            @PathVariable UUID userID,
            @PathVariable UUID companyID
    ){
        CompanyResponse deletedCompany = companyService.deleteCompany(userID, companyID);
        return new ResponseEntity<>(deletedCompany, HttpStatus.OK);
    }

}
