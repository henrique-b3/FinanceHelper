package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.Filter.TransactionFilter;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.model.Company;
import com.app.FinanceHelper.model.Transaction;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.TransactionDTO;
import com.app.FinanceHelper.payload.dto.TransactionFilterDTO;
import com.app.FinanceHelper.payload.response.CategoryExpenseResponse;
import com.app.FinanceHelper.payload.response.TransactionResponse;
import com.app.FinanceHelper.repository.CategoryRepository;
import com.app.FinanceHelper.repository.CompanyRepository;
import com.app.FinanceHelper.repository.TransactionRepository;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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


        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setUserProfile(user);
        transaction.setCategory(category);


        if(transactionDTO.getCompanyID() != null){
            Company company = companyRepository.findByIdAndUserProfile_Id(transactionDTO.getCompanyID(), userID)
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", transactionDTO.getCompanyID()));

            transaction.setCompany(company);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionResponse transactionResponse = modelMapper.map(savedTransaction, TransactionResponse.class);
        transactionResponse.setCategoryName(savedTransaction.getCategory().getName());
        transactionResponse.setCategoryID(savedTransaction.getCategory().getId());

        if (savedTransaction.getCompany() != null) {
            transactionResponse.setCompanyName(savedTransaction.getCompany().getName());
            transactionResponse.setCompanyID(savedTransaction.getCompany().getId());
        }

        return transactionResponse;
    }

    @Override
    public TransactionResponse getTransaction(UUID userID, UUID transactionID) {

        Transaction foundTransaction = transactionRepository.findByIdAndUserProfile_Id(transactionID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionID));

        TransactionResponse transactionResponse = modelMapper.map(foundTransaction, TransactionResponse.class);
        transactionResponse.setCategoryName(foundTransaction.getCategory().getName());
        transactionResponse.setCategoryID(foundTransaction.getCategory().getId());

        if (foundTransaction.getCompany() != null) {
            transactionResponse.setCompanyName(foundTransaction.getCompany().getName());
            transactionResponse.setCompanyID(foundTransaction.getCompany().getId());
        }

        return transactionResponse;
    }

    @Override
    public List<TransactionResponse> getAllTransactions(UUID userID, Integer limit) {

        List<Transaction> transactions;

        if (limit == null || limit == 0) {
            transactions = transactionRepository.findAllByUserProfile_Id(userID);
        } else {
            Pageable pageable = PageRequest.of(0, limit);
            transactions = transactionRepository.findByUserProfileIdOrderByTransactionDateDesc(userID, pageable).getContent();
        }

        return transactions.stream()
                .map(transaction -> {
                    TransactionResponse response = modelMapper.map(transaction, TransactionResponse.class);
                    response.setCategoryName(transaction.getCategory().getName());
                    response.setCategoryColor(transaction.getCategory().getColor());
                    response.setCategoryID(transaction.getCategory().getId());

                    if (transaction.getCompany() != null) {
                        response.setCompanyName(transaction.getCompany().getName());
                        response.setCompanyID(transaction.getCompany().getId());
                    }

                    return response;
                }).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalSpentByMonth(UUID userID) {
        if(!userProfileRepository.existsById(userID)){
            throw new ResourceNotFoundException("UserProfile","userID", userID);
        }

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        return transactionRepository.getTotalSpentByMonth(userID, startDate, endDate);
    }

    @Override
    public TransactionResponse updateTransaction(UUID userID, UUID transactionID, TransactionDTO transactionDTO) {

        Transaction foundTransaction = transactionRepository.findByIdAndUserProfile_Id(transactionID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionID));

        if (transactionDTO.getTransactionDate() != null && !transactionDTO.getTransactionDate().equals(foundTransaction.getTransactionDate())) {
            foundTransaction.setTransactionDate(transactionDTO.getTransactionDate());
        }

        if (transactionDTO.getAmount() != null && transactionDTO.getAmount().compareTo(foundTransaction.getAmount()) != 0) {
            foundTransaction.setAmount(transactionDTO.getAmount());
        }

        if (transactionDTO.getCategoryID() != null && !transactionDTO.getCategoryID().equals(foundTransaction.getCategory().getId())) {
            Category category = categoryRepository.findByIdAndUserProfile_Id(transactionDTO.getCategoryID(), userID)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", transactionDTO.getCategoryID()));
            foundTransaction.setCategory(category);
        }

        if (transactionDTO.getCompanyID() != null && !transactionDTO.getCompanyID().equals(foundTransaction.getCompany().getId())) {
            Company company = companyRepository.findByIdAndUserProfile_Id(transactionDTO.getCompanyID(), userID)
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", transactionDTO.getCompanyID()));
            foundTransaction.setCompany(company);
        }

        Transaction updatedTransaction = transactionRepository.save(foundTransaction);

        TransactionResponse transactionResponse = modelMapper.map(updatedTransaction, TransactionResponse.class);
        transactionResponse.setCategoryName(updatedTransaction.getCategory().getName());
        transactionResponse.setCategoryColor(updatedTransaction.getCategory().getColor());
        transactionResponse.setCategoryID(updatedTransaction.getCategory().getId());

        if (updatedTransaction.getCompany() != null) {
            transactionResponse.setCompanyName(updatedTransaction.getCompany().getName());
            transactionResponse.setCompanyID(updatedTransaction.getCompany().getId());
        }

        return transactionResponse;
    }

    @Override
    public Integer countTransactions(UUID userID) {
        return transactionRepository.countByUserProfile_Id(userID);
    }

    @Override
    public Page<TransactionResponse> getTransactionsWithFilters(UUID userID, TransactionFilterDTO filter, Pageable pageable) {

        if(filter.orderBy() != null && !filter.orderBy().isBlank()) {
            Sort.Direction direction = (filter.direction() != null && filter.direction().equalsIgnoreCase("desc"))
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;


            String sortBy = filter.orderBy();

            if (sortBy.equalsIgnoreCase("data")) sortBy = "transactionDate";
            if (sortBy.equalsIgnoreCase("valor")) sortBy = "amount";

            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
        }

        Specification <Transaction> spec = TransactionFilter.filter(userID, filter);

        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);


        return transactionPage.map(transaction -> {
            TransactionResponse response = modelMapper.map(transaction, TransactionResponse.class);
            response.setCategoryName(transaction.getCategory().getName());
            response.setCategoryColor(transaction.getCategory().getColor());
            response.setCategoryID(transaction.getCategory().getId());

            if (transaction.getCompany() != null) {
                response.setCompanyName(transaction.getCompany().getName());
                response.setCompanyID(transaction.getCompany().getId());
            }
            return response;
        });
    }

    @Override
    public List<CategoryExpenseResponse> getExpensesByCategoryByMonth(UUID userID) {
        if(!userProfileRepository.existsById(userID)){
            throw new ResourceNotFoundException("UserProfile","userID", userID);
        }

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        return transactionRepository.getExpensesByCategory(userID, startDate, endDate);
    }

    @Override
    public TransactionResponse deleteTransaction(UUID userID, UUID transactionID) {

        Transaction foundTransaction = transactionRepository.findByIdAndUserProfile_Id(transactionID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionID));

        TransactionResponse transactionResponse = modelMapper.map(foundTransaction, TransactionResponse.class);
        transactionResponse.setCategoryName(foundTransaction.getCategory().getName());
        transactionResponse.setCategoryColor(foundTransaction.getCategory().getColor());
        transactionResponse.setCategoryID(foundTransaction.getCategory().getId());

        if (foundTransaction.getCompany() != null) {
            transactionResponse.setCompanyName(foundTransaction.getCompany().getName());
            transactionResponse.setCompanyID(foundTransaction.getCompany().getId());
        }

        transactionRepository.delete(foundTransaction);

        return transactionResponse;
    }
}
