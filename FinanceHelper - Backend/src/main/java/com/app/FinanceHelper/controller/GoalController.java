package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.GoalDTO;
import com.app.FinanceHelper.payload.response.GoalResponse;
import com.app.FinanceHelper.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/goal")
public class GoalController {

    @Autowired
    GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(
            @AuthenticationPrincipal UserProfile user,
            @Valid @RequestBody GoalDTO goalDTO
    ){
        GoalResponse goalResponse = goalService.createGoal(user.getId(),goalDTO);
        return new ResponseEntity<>(goalResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{goalID}")
    public ResponseEntity<GoalResponse> getGoal(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID goalID
    ){
        GoalResponse goalResponse = goalService.getGoal(user.getId(),goalID);
        return new ResponseEntity<>(goalResponse, HttpStatus.OK);
    }

    @PutMapping("/{goalID}")
    public ResponseEntity<GoalResponse> updateGoal(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID goalID,
            @Valid @RequestBody GoalDTO goalDTO
    ){
        return null;
    }

    @PutMapping("/limitAmount/{goalID}")
    public ResponseEntity<GoalResponse> updateGoalLimitAmount(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID goalID,
            @RequestParam BigDecimal amount
            ){
        return null;
    }

    @PutMapping("/name/{goalID}")
    public ResponseEntity<GoalResponse> updateGoalName(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID goalID,
            @RequestParam String name
    ){
        return null;
    }

    @DeleteMapping("/{goalID}")
    public ResponseEntity<GoalResponse> deleteGoal(
            @AuthenticationPrincipal UserProfile user,
            @PathVariable UUID goalID
    ){
        GoalResponse goalResponse = goalService.deleteGoal(user.getId(),goalID);
        return new ResponseEntity<>(goalResponse, HttpStatus.OK);
    }
}
