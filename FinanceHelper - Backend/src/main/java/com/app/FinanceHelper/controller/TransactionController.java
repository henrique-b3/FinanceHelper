package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.TransactionDTO;
import com.app.FinanceHelper.payload.dto.TransactionFilterDTO;
import com.app.FinanceHelper.payload.response.CategoryExpenseResponse;
import com.app.FinanceHelper.payload.response.TransactionResponse;
import com.app.FinanceHelper.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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

    @GetMapping("/get")
    public ResponseEntity<TransactionResponse> getTransaction(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam UUID transactionID
    ){
        TransactionResponse transactionResponse = transactionService.getTransaction(user.getId(),transactionID);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @GetMapping("/totalMonth")
    public ResponseEntity<BigDecimal> getTotalSpentThisMonth(
            @AuthenticationPrincipal UserProfile user
    ){
        BigDecimal total = transactionService.getTotalSpentByMonth(user.getId());
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @GetMapping("/chartData")
    public ResponseEntity<List<CategoryExpenseResponse>> getExpensesByCategoryThisMonth(
            @AuthenticationPrincipal UserProfile user
    ){
        List<CategoryExpenseResponse> chartData = transactionService.getExpensesByCategoryByMonth(user.getId());
        return new ResponseEntity<>(chartData, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam Integer limit
    ){
        List<TransactionResponse> transactionResponse = transactionService.getAllTransactions(user.getId(), limit);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countTransactions(
            @AuthenticationPrincipal UserProfile user
    ){
        Integer countTransactions = transactionService.countTransactions(user.getId());
        return new ResponseEntity<>(countTransactions, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TransactionResponse>> getFilteredTransactions(
            @AuthenticationPrincipal UserProfile user,
            TransactionFilterDTO filterDTO,
            Pageable pageable
    ) {
        Page<TransactionResponse> result = transactionService.getTransactionsWithFilters(user.getId(), filterDTO, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam UUID transactionID,
            @Valid @RequestBody TransactionDTO transactionDTO
    ){
        TransactionResponse transactionResponse = transactionService.updateTransaction(user.getId(), transactionID, transactionDTO);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<TransactionResponse> deleteTransaction(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam UUID transactionID
    ){
        TransactionResponse transactionResponse = transactionService.deleteTransaction(user.getId(), transactionID);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }
}
