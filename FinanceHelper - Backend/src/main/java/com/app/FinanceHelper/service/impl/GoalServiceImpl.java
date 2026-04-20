package com.app.FinanceHelper.service.impl;

import ch.qos.logback.core.model.Model;
import com.app.FinanceHelper.controller.GoalController;
import com.app.FinanceHelper.enums.GoalStatus;
import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.model.Goal;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.GoalDTO;
import com.app.FinanceHelper.payload.response.GoalResponse;
import com.app.FinanceHelper.payload.response.GoalStatusResponse;
import com.app.FinanceHelper.repository.CategoryRepository;
import com.app.FinanceHelper.repository.GoalRepository;
import com.app.FinanceHelper.repository.TransactionRepository;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    GoalRepository goalRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public GoalResponse createGoal(UUID userID, GoalDTO goalDTO) {
        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userID", userID));

        if(goalRepository.existsByNameAndUserProfile_Id(goalDTO.getName(), userID)){
            throw new APIexception("Goal already exists with name: " + goalDTO.getName());
        }

        Category category = categoryRepository.findByIdAndUserProfile_Id(goalDTO.getCategoryID(),userID)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", userID));

        Goal goal = modelMapper.map(goalDTO, Goal.class);
        goal.setUserProfile(user);
        goal.setCategory(category);

        Goal savedGoal = goalRepository.save(goal);

        GoalResponse goalResponse = modelMapper.map(savedGoal, GoalResponse.class);

        goalResponse.setCategoryID(category.getId());

        return goalResponse;
    }

    @Override
    public GoalResponse getGoal(UUID userID, UUID goalID) {

        Goal foundGoal =  goalRepository.findByIdAndUserProfile_Id(goalID, userID)
               .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalID", userID));

        BigDecimal spent = transactionRepository.getTotalSpentByCategoryAndDate(
                userID,
                foundGoal.getCategory().getId(),
                foundGoal.getStartDate(),
                foundGoal.getEndDate()
        );

        foundGoal.setSpendAmount(spent);

        GoalResponse goalResponse = modelMapper.map(foundGoal, GoalResponse.class);

        goalResponse.setCategoryID(foundGoal.getCategory().getId());

        return goalResponse;
    }

    @Override
    public Set<GoalResponse> getAllGoals(UUID userID) {

        List<Goal> goals = goalRepository.findAllByUserProfile_Id(userID);

        return goals.stream().map(
                goal -> {
                    BigDecimal spent = transactionRepository.getTotalSpentByCategoryAndDate(
                            userID,
                            goal.getCategory().getId(),
                            goal.getStartDate(),
                            goal.getEndDate()
                    );

                    goal.setSpendAmount(spent);

                    GoalResponse goalResponse = modelMapper.map(goal, GoalResponse.class);

                    goalResponse.setCategoryID(goal.getCategory().getId());

                    return goalResponse;
                }).collect(Collectors.toSet());
    }

    @Override
    public GoalResponse updateGoal(UUID userID, UUID goalID, GoalDTO goalDTO) {

        Goal foundGoal =  goalRepository.findByIdAndUserProfile_Id(goalID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalID", userID));


        if(goalDTO.getName() != null && !goalDTO.getName().equals(foundGoal.getName())) {
            foundGoal.setName(goalDTO.getName());
        }


        if(goalDTO.getDescription() != null && !goalDTO.getDescription().equals(foundGoal.getDescription())) {
            foundGoal.setDescription(goalDTO.getDescription());
        }


        if(goalDTO.getColor() != null && !goalDTO.getColor().equals(foundGoal.getColor())) {
            foundGoal.setColor(goalDTO.getColor());
        }


        if(goalDTO.getIcon() != null && !goalDTO.getIcon().equals(foundGoal.getIcon())) {
            foundGoal.setIcon(goalDTO.getIcon());
        }


        if(goalDTO.getLimitAmount() != null && (foundGoal.getLimitAmount() == null || goalDTO.getLimitAmount().compareTo(foundGoal.getLimitAmount()) != 0)) {
            foundGoal.setLimitAmount(goalDTO.getLimitAmount());
        }


        if(goalDTO.getStartDate() != null && !goalDTO.getStartDate().equals(foundGoal.getStartDate())) {
            foundGoal.setStartDate(goalDTO.getStartDate());
        }


        if(goalDTO.getEndDate() != null && !goalDTO.getEndDate().equals(foundGoal.getEndDate())) {
            foundGoal.setEndDate(goalDTO.getEndDate());
        }

        if(goalDTO.getCategoryID() != null && !goalDTO.getCategoryID().equals(foundGoal.getCategory().getId())) {
            Category category = categoryRepository.findByIdAndUserProfile_Id(goalDTO.getCategoryID(), userID)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", userID));

            foundGoal.setCategory(category);
        }

        Goal savedGoal = goalRepository.save(foundGoal);

        BigDecimal spent = transactionRepository.getTotalSpentByCategoryAndDate(
                userID,
                savedGoal.getCategory().getId(),
                savedGoal.getStartDate(),
                savedGoal.getEndDate()
        );

        savedGoal.setSpendAmount(spent);

        GoalResponse goalResponse = modelMapper.map(savedGoal, GoalResponse.class);

        goalResponse.setCategoryID(savedGoal.getCategory().getId());

        return goalResponse;
    }

    @Override
    public GoalStatusResponse getGoalsStatus(UUID userID) {
        List<Goal> userGoals = goalRepository.findAllByUserProfile_Id(userID);

        userGoals.forEach(goal -> {
            BigDecimal spent = transactionRepository.getTotalSpentByCategoryAndDate(
                    userID,
                    goal.getCategory().getId(),
                    goal.getStartDate(),
                    goal.getEndDate()
            );
            goal.setSpendAmount(spent);
        });

        long finished = userGoals.stream()
                .filter(goal -> goal.getStatus() == GoalStatus.FINISHED)
                .count();

        long current = userGoals.stream()
                .filter(goal -> goal.getStatus() != GoalStatus.FINISHED)
                .count();

        return new GoalStatusResponse(current, finished, current + finished);
    }


    @Override
    public GoalResponse deleteGoal(UUID userID, UUID goalID) {

        Goal deletedGoal =  goalRepository.findByIdAndUserProfile_Id(goalID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalID", userID));

        goalRepository.delete(deletedGoal);

        return modelMapper.map(deletedGoal, GoalResponse.class);
    }
}
