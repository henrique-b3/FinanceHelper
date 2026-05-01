package com.app.FinanceHelper.repository;

import com.app.FinanceHelper.enums.GoalStatus;
import com.app.FinanceHelper.model.Goal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {
    boolean existsByNameAndUserProfile_Id(String name, UUID userID);

    Optional<Goal> findByIdAndUserProfile_Id(UUID goalID, UUID userID);

    List<Goal> findAllByUserProfile_Id(UUID userID);

    Page<Goal> findAll(Specification<Goal> spec, Pageable pageable);
}
