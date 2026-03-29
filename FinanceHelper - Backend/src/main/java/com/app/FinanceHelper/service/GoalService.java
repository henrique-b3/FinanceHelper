package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.GoalDTO;
import com.app.FinanceHelper.payload.response.GoalResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface GoalService {
    GoalResponse createGoal(UUID userID, @Valid GoalDTO goalDTO);

    GoalResponse getGoal(UUID userID, UUID goalID);

    GoalResponse deleteGoal(UUID userID, UUID goalID);
}
