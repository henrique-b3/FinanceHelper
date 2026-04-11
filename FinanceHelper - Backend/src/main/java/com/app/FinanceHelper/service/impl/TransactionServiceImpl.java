package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.model.Company;
import com.app.FinanceHelper.model.Transaction;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.TransactionDTO;
import com.app.FinanceHelper.payload.response.CompanyResponse;
import com.app.FinanceHelper.payload.response.TransactionResponse;
import com.app.FinanceHelper.repository.CategoryRepository;
import com.app.FinanceHelper.repository.CompanyRepository;
import com.app.FinanceHelper.repository.TransactionRepository;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public TransactionResponse createTransaction(UUID userID, TransactionDTO transactionDTO) {


        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userID));

        Category category = categoryRepository.findByIdAndUserProfile_Id(transactionDTO.getCategoryID(), userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", transactionDTO.getCategoryID()));


        Company company = companyRepository.findByIdAndUserProfile_Id(transactionDTO.getCompanyID(), userID)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", transactionDTO.getCompanyID()));

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setUserProfile(user);
        transaction.setCategory(category);
        transaction.setCompany(company);

        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionResponse transactionResponse = modelMapper.map(savedTransaction, TransactionResponse.class);
        transactionResponse.setUserID(savedTransaction.getUserProfile().getId());
        transactionResponse.setCompanyID(savedTransaction.getCompany().getId());
        transactionResponse.setCategoryID(savedTransaction.getCategory().getId());

        return transactionResponse;
    }

    @Override
    public TransactionResponse getTransaction(UUID userID, UUID transactionID) {
        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userID));

        Transaction foundTransaction = transactionRepository.findByIdAndUserProfile_Id(transactionID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionID));

        TransactionResponse transactionResponse = modelMapper.map(foundTransaction, TransactionResponse.class);
        transactionResponse.setUserID(foundTransaction.getUserProfile().getId());
        transactionResponse.setCompanyID(foundTransaction.getCompany().getId());
        transactionResponse.setCategoryID(foundTransaction.getCategory().getId());

        return transactionResponse;
    }

    @Override
    public TransactionResponse deleteTransaction(UUID userID, UUID transactionID) {
        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userID));

        Transaction foundTransaction = transactionRepository.findByIdAndUserProfile_Id(transactionID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionID));

        transactionRepository.delete(foundTransaction);

        TransactionResponse transactionResponse = modelMapper.map(foundTransaction, TransactionResponse.class);
        transactionResponse.setUserID(foundTransaction.getUserProfile().getId());
        transactionResponse.setCompanyID(foundTransaction.getCompany().getId());
        transactionResponse.setCategoryID(foundTransaction.getCategory().getId());

        return transactionResponse;
    }
}
