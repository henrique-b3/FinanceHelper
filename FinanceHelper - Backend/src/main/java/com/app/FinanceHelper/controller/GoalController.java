package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.payload.dto.GoalDTO;
import com.app.FinanceHelper.payload.response.GoalResponse;
import com.app.FinanceHelper.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/goal/{userID}")
public class GoalController {

    @Autowired
    GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(
            @PathVariable UUID userID,
            @Valid @RequestBody GoalDTO goalDTO
    ){
        GoalResponse goalResponse = goalService.createGoal(userID,goalDTO);
        return new ResponseEntity<>(goalResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{goalID}")
    public ResponseEntity<GoalResponse> getGoal(
            @PathVariable UUID userID,
            @PathVariable UUID goalID
    ){
        GoalResponse goalResponse = goalService.getGoal(userID,goalID);
        return new ResponseEntity<>(goalResponse, HttpStatus.OK);
    }

    @PutMapping("/{goalID}")
    public ResponseEntity<GoalResponse> updateGoal(
            @PathVariable UUID userID,
            @PathVariable UUID goalID,
            @Valid @RequestBody GoalDTO goalDTO
    ){
        return null;
    }

    @PutMapping("/limitAmount/{goalID}")
    public ResponseEntity<GoalResponse> updateGoalLimitAmount(
            @PathVariable UUID userID,
            @PathVariable UUID goalID,
            @RequestParam BigDecimal amount
            ){
        return null;
    }

    @PutMapping("/name/{goalID}")
    public ResponseEntity<GoalResponse> updateGoalName(
            @PathVariable UUID userID,
            @PathVariable UUID goalID,
            @RequestParam String name
    ){
        return null;
    }

    @DeleteMapping("/{goalID}")
    public ResponseEntity<GoalResponse> deleteGoal(
            @PathVariable UUID userID,
            @PathVariable UUID goalID
    ){
        GoalResponse goalResponse = goalService.deleteGoal(userID,goalID);
        return new ResponseEntity<>(goalResponse, HttpStatus.OK);
    }
}
