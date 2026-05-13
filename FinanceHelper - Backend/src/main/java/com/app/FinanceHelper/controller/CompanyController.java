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

import java.util.List;
import java.util.Set;
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

    @GetMapping("/all")
    public ResponseEntity<Set<CompanyResponse>> getAllCompanies(
            @AuthenticationPrincipal UserProfile user
    ){
        Set<CompanyResponse> companyResponse = companyService.getAllCompanies(user.getId());
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }

    @GetMapping("/all/byName")
    public ResponseEntity<List<CompanyResponse>> getAllCompaniesByName(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam String companyName
    ){
        List<CompanyResponse> companyResponse = companyService.getAllCompaniesByName(user.getId(), companyName);
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<CompanyResponse> updateCompany(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam UUID companyID,
            @Valid @RequestBody CompanyDTO companyDTO
    ){
        CompanyResponse companyResponse = companyService.updateCompany(user.getId(),companyID, companyDTO);
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CompanyResponse> deleteCompanyById(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam UUID companyID
    ){
        CompanyResponse companyResponse = companyService.deleteCompany(user.getId(), companyID);
        return new ResponseEntity<>(companyResponse, HttpStatus.OK);
    }

}
