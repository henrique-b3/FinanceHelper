package com.app.FinanceHelper.service;

import com.app.FinanceHelper.model.Transaction;
import com.app.FinanceHelper.payload.dto.TransactionDTO;
import com.app.FinanceHelper.payload.response.CompanyResponse;
import com.app.FinanceHelper.payload.response.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface TransactionService {
    TransactionResponse createTransaction(UUID userID, @Valid TransactionDTO transactionDTO);

    TransactionResponse getTransaction(UUID userID, UUID transactionID);

    TransactionResponse deleteTransaction(UUID userID, UUID transactionID);
}
