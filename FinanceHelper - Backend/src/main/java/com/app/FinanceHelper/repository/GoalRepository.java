package com.app.FinanceHelper.repository;

import com.app.FinanceHelper.enums.GoalStatus;
import com.app.FinanceHelper.model.Goal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {
    boolean existsByNameAndUserProfile_Id(String name, UUID userID);

    Optional<Goal> findByIdAndUserProfile_Id(UUID goalID, UUID userID);

    Page<Goal> findAllByUserProfile_Id(UUID userID, Pageable pageable);

    Page<Goal> findAll(Specification<Goal> spec, Pageable pageable);


    public interface GoalSpendProjection {
        UUID getGoalId();
        java.math.BigDecimal getSpendAmount();
    }


    @Query("SELECT g.id AS goalId, COALESCE(SUM(t.amount), 0) AS spendAmount " +
            "FROM Goal g " +
            "LEFT JOIN Transaction t ON t.userProfile = g.userProfile " +
            "AND ((g.category IS NOT NULL AND t.category = g.category) OR (g.company IS NOT NULL AND t.company = g.company)) " +
            "AND t.transactionDate >= g.startDate AND t.transactionDate <= g.endDate " +
            "WHERE g.id IN :goalIds " +
            "GROUP BY g.id")
    List<GoalSpendProjection> getSpendAmountsForGoals(@org.springframework.data.repository.query.Param("goalIds") List<UUID> goalIds);
}
