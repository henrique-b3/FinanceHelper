package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.TransactionDTO;
import com.app.FinanceHelper.payload.response.CategoryExpenseResponse;
import com.app.FinanceHelper.payload.response.TransactionResponse;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface TransactionService {
    TransactionResponse createTransaction(UUID userID, @Valid TransactionDTO transactionDTO);

    TransactionResponse getTransaction(UUID userID, UUID transactionID);

    TransactionResponse deleteTransaction(UUID userID, UUID transactionID);

    List<TransactionResponse> getAllTransactions(UUID userID, Integer limit);

    List<CategoryExpenseResponse> getExpensesByCategoryByMonth(UUID userID);

    BigDecimal getTotalSpentByMonth(UUID userID);

    TransactionResponse updateTransaction(UUID userID, UUID transactionID, TransactionDTO transactionDTO);

    Integer countTransactions(UUID userID);
}
