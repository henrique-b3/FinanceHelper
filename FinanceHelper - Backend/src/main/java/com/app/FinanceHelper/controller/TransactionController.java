package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.TransactionDTO;
import com.app.FinanceHelper.payload.response.TransactionResponse;
import com.app.FinanceHelper.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @AuthenticationPrincipal UserProfile user,
            @Valid @RequestBody TransactionDTO transactionDTO
    ){
        TransactionResponse transactionResponse = transactionService.createTransaction(user.getId(),transactionDTO);
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionID}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID transactionID
    ){
        TransactionResponse transactionResponse = transactionService.getTransaction(user.getId(),transactionID);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{transactionID}")
    public ResponseEntity<TransactionResponse> deleteTransaction(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID transactionID
    ){
        TransactionResponse transactionResponse = transactionService.deleteTransaction(user.getId(), transactionID);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }
}
