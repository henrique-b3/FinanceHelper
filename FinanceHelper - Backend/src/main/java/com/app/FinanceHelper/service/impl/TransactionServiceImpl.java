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
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository transactionRepository;
    UserProfileRepository userProfileRepository;
    CategoryRepository categoryRepository;
    CompanyRepository companyRepository;
    ModelMapper modelMapper;


    @Override
    public TransactionResponse createTransaction(UUID userID, TransactionDTO transactionDTO) {


        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);

        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userID));

        Category category = categoryRepository.findByIdAndUserProfile_Id(transaction.getCategory().getId(), userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", transaction.getCategory().getId()));


        Company company = companyRepository.findByIdAndUserProfile_Id(transaction.getCompany().getId(), userID)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", transaction.getCompany().getId()));

        transaction.setUserProfile(user);
        transaction.setCategory(category);
        transaction.setCompany(company);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return modelMapper.map(savedTransaction, TransactionResponse.class);
    }

    @Override
    public TransactionResponse getTransaction(UUID userID, UUID transactionID) {
        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userID));

        Transaction foundTransaction = transactionRepository.findByIdAndUserProfile_Id(transactionID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionID));

        return modelMapper.map(foundTransaction, TransactionResponse.class);
    }

    @Override
    public TransactionResponse deleteTransaction(UUID userID, UUID transactionID) {
        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userID));

        Transaction foundTransaction = transactionRepository.findByIdAndUserProfile_Id(transactionID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionID));

        transactionRepository.delete(foundTransaction);

        return modelMapper.map(foundTransaction, TransactionResponse.class);
    }
}
