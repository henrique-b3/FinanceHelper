package com.app.FinanceHelper.repository;

import com.app.FinanceHelper.model.Transaction;
import com.app.FinanceHelper.payload.response.CategoryExpenseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndUserProfile_Id(UUID transactionID, UUID userID);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.company JOIN FETCH t.category WHERE t.userProfile.id = :userID")
    List<Transaction> findAllByUserProfile_Id(@Param("userID") UUID userID);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.company JOIN FETCH t.category WHERE t.userProfile.id = :userID ORDER BY t.transactionDate DESC")
    Page<Transaction> findByUserProfileIdOrderByTransactionDateDesc(@Param("userID") UUID userID, Pageable pageable);


    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.userProfile.id = :userID " +
            "AND t.category.id = :categoryID " +
            "AND (CAST(:startDate AS java.time.LocalDate) IS NULL OR t.transactionDate >= :startDate) " +
            "AND (CAST(:endDate AS java.time.LocalDate) IS NULL OR t.transactionDate <= :endDate)")
    BigDecimal getTotalSpentByCategoryAndDate(
            @Param("userID") UUID userID,
            @Param("categoryID") UUID categoryID,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.userProfile.id = :userID " +
            "AND t.company.id = :companyID " +
            "AND (CAST(:startDate AS java.time.LocalDate) IS NULL OR t.transactionDate >= :startDate) " +
            "AND (CAST(:endDate AS java.time.LocalDate) IS NULL OR t.transactionDate <= :endDate)")
    BigDecimal getTotalSpentByCompanyAndDate(
            @Param("userID") UUID userID,
            @Param("companyID") UUID companyID,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.userProfile.id = :userID " +
            "AND t.transactionDate >= :startDate " +
            "AND t.transactionDate <= :endDate")
    BigDecimal getTotalSpentByMonth(
            @Param("userID") UUID userID,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT new com.app.FinanceHelper.payload.response.  CategoryExpenseResponse(c.name, c.color, SUM(t.amount)) " +
            "FROM Transaction t JOIN t.category c " +
            "WHERE t.userProfile.id = :userID AND t.transactionDate >= :startDate AND t.transactionDate <= :endDate " +
            "GROUP BY c.name, c.color")
    List<CategoryExpenseResponse> getExpensesByCategory(
            @Param("userID") UUID userID,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    Integer countByUserProfile_Id(UUID userID);

    Integer countByUserProfile_IdAndCategoryId(UUID userID, UUID id);


    public interface CategoryCountProjection {
        UUID getCategoryId();
        Integer getTransactionCount();
    }

    @Query("SELECT t.category.id AS categoryId, CAST(COUNT(t.id) AS int) AS transactionCount " +
            "FROM Transaction t " +
            "WHERE t.userProfile.id = :userID AND t.category IS NOT NULL " +
            "GROUP BY t.category.id")
    List<CategoryCountProjection> countTransactionsPerCategoryByUser(@Param("userID") UUID userID);
}
