package com.app.FinanceHelper.service.impl;

import ch.qos.logback.core.model.Model;
import com.app.FinanceHelper.controller.GoalController;
import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.Goal;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.GoalDTO;
import com.app.FinanceHelper.payload.response.GoalResponse;
import com.app.FinanceHelper.repository.GoalRepository;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    GoalRepository goalRepository;
    UserProfileRepository userProfileRepository;
    ModelMapper modelMapper;

    @Override
    public GoalResponse createGoal(UUID userID, GoalDTO goalDTO) {
        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userID", userID));

        if(goalRepository.existsByNameAndUserProfile_Id(goalDTO.getName(), userID)){
            throw new APIexception("Goal already exists with name: " + goalDTO.getName());
        }

        Goal goal = modelMapper.map(goalDTO, Goal.class);
        goal.setUserProfile(user);

        Goal savedGoal = goalRepository.save(goal);

        return modelMapper.map(savedGoal, GoalResponse.class);
    }

    @Override
    public GoalResponse getGoal(UUID userID, UUID goalID) {
        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userID", userID));

        Goal foundGoal =  goalRepository.findByIdAndUserProfile_Id(goalID, userID)
               .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalID", userID));


       return modelMapper.map(foundGoal, GoalResponse.class);
    }

    @Override
    public GoalResponse deleteGoal(UUID userID, UUID goalID) {
        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userID", userID));

        Goal deletedGoal =  goalRepository.findByIdAndUserProfile_Id(goalID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalID", userID));

        goalRepository.delete(deletedGoal);

        return modelMapper.map(deletedGoal, GoalResponse.class);
    }
}
