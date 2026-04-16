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
import java.util.Set;
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

    @GetMapping("/all")
    public ResponseEntity<Set<GoalResponse>> getAllGoals(
            @AuthenticationPrincipal UserProfile user
    ){
        Set<GoalResponse> goalResponses = goalService.getAllGoals(user.getId());
        return new ResponseEntity<>(goalResponses, HttpStatus.OK);
    }

    @PutMapping("/{goalID}")
    public ResponseEntity<GoalResponse> updateGoal(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam UUID goalID,
            @Valid @RequestBody GoalDTO goalDTO
    ){
        GoalResponse goalResponse = goalService.updateGoal(user.getId(),goalID, goalDTO);
        return new ResponseEntity<>(goalResponse, HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<GoalResponse> deleteGoal(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam UUID goalID
    ){
        GoalResponse goalResponse = goalService.deleteGoal(user.getId(),goalID);
        return new ResponseEntity<>(goalResponse, HttpStatus.OK);
    }
}
