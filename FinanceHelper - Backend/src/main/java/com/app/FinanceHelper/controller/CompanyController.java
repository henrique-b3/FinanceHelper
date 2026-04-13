package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.CompanyDTO;
import com.app.FinanceHelper.payload.response.CompanyResponse;
import com.app.FinanceHelper.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @AuthenticationPrincipal UserProfile user,
            @Valid @RequestBody CompanyDTO companyDTO
    ){
        CompanyResponse companyResponse = companyService.createCompany(user.getId(), companyDTO);
        return new ResponseEntity<>(companyResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{companyID}")
    public ResponseEntity<CompanyResponse> getCompany(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID companyID
    ){
        CompanyResponse companyResponse = companyService.getCompany(user.getId(), companyID);
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{companyID}")
    public ResponseEntity<CompanyResponse> deleteCompanyById(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID companyID
    ){
        CompanyResponse companyResponse = companyService.deleteCompany(user.getId(), companyID);
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }

}
