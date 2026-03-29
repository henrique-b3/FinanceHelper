package com.app.FinanceHelper.repository;

import com.app.FinanceHelper.model.Transaction;
import com.app.FinanceHelper.payload.response.TransactionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByIdAndUserProfile_Id(UUID transactionID, UUID userID);
}
