package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.GoalDTO;
import com.app.FinanceHelper.payload.dto.GoalFilterDTO;
import com.app.FinanceHelper.payload.response.GoalResponse;
import com.app.FinanceHelper.payload.response.GoalStatusResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface GoalService {
    GoalResponse createGoal(UUID userID, @Valid GoalDTO goalDTO);

    GoalResponse getGoal(UUID userID, UUID goalID);

    GoalResponse deleteGoal(UUID userID, UUID goalID);

    Set<GoalResponse> getAllGoals(UUID userID);

    GoalResponse updateGoal(UUID userID, UUID goalID, GoalDTO goalDTO);

    GoalStatusResponse getGoalsStats(UUID userID);

    List<String> getGoalsStatus(UUID userID);

    Page<GoalResponse> getGoalsWithFilters(UUID userID, GoalFilterDTO filter, Pageable pageable);
}
