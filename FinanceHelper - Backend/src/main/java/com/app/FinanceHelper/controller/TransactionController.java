package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.model.Transaction;
import com.app.FinanceHelper.payload.dto.TransactionDTO;
import com.app.FinanceHelper.payload.response.TransactionResponse;
import com.app.FinanceHelper.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transaction/{userID}")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @PathVariable UUID userID,
            @Valid @RequestBody TransactionDTO transactionDTO
    ){
        TransactionResponse transactionResponse = transactionService.createTransaction(userID,transactionDTO);
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionID}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @PathVariable UUID userID,
            @PathVariable UUID transactionID
    ){
        TransactionResponse transactionResponse = transactionService.getTransaction(userID,transactionID);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{transactionID}")
    public ResponseEntity<TransactionResponse> deleteTransaction(
            @PathVariable UUID userID,
            @PathVariable UUID transactionID
    ){
        TransactionResponse transactionResponse = transactionService.deleteTransaction(userID, transactionID);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }
}
