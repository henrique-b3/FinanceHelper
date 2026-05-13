package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.GoalDTO;
import com.app.FinanceHelper.payload.dto.GoalFilterDTO;
import com.app.FinanceHelper.payload.response.GoalResponse;
import com.app.FinanceHelper.payload.response.GoalStatusResponse;
import com.app.FinanceHelper.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Page<GoalResponse>> getAllGoals(
            @AuthenticationPrincipal UserProfile user,
            Pageable pageable
    ){
        Page<GoalResponse> goalResponses = goalService.getAllGoals(user.getId(), pageable);
        return new ResponseEntity<>(goalResponses, HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<GoalStatusResponse> getGoalsStatus(
            @AuthenticationPrincipal UserProfile user
    ){
        GoalStatusResponse goalStatusResponses = goalService.getGoalsStats(user.getId());
        return new ResponseEntity<>(goalStatusResponses, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<List<String>> getStatus(
            @AuthenticationPrincipal UserProfile user
    ){
        List<String> statusList = goalService.getGoalsStatus(user.getId());
        return new ResponseEntity<>(statusList, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<GoalResponse>> getFilteredGoals(
            @AuthenticationPrincipal UserProfile user,
            GoalFilterDTO goalFilterDTO,
            Pageable pageable
    ){
        Page<GoalResponse> result = goalService.    getGoalsWithFilters(user.getId(), goalFilterDTO, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/update")
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
