package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.filter.GoalFilter;
import com.app.FinanceHelper.enums.GoalStatus;
import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.Category;
import com.app.FinanceHelper.model.Company;
import com.app.FinanceHelper.model.Goal;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.GoalDTO;
import com.app.FinanceHelper.payload.dto.GoalFilterDTO;
import com.app.FinanceHelper.payload.response.GoalResponse;
import com.app.FinanceHelper.payload.response.GoalStatusResponse;
import com.app.FinanceHelper.repository.*;
import com.app.FinanceHelper.service.GoalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
    CompanyRepository companyRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public GoalResponse createGoal(UUID userID, GoalDTO goalDTO) {
        UserProfile user = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userID", userID));

        if(goalRepository.existsByNameAndUserProfile_Id(goalDTO.getName(), userID)){
            throw new APIexception("Já existe um objetivo com esse nome");
        }

        Goal goal = modelMapper.map(goalDTO, Goal.class);
        goal.setUserProfile(user);

        if(goalDTO.getCategoryID() != null){
            Category category = categoryRepository.findByIdAndUserProfile_Id(goalDTO.getCategoryID(),userID)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", goalDTO.getCategoryID()));

            goal.setCategory(category);

        }else if(goalDTO.getCompanyID() != null){
            Company company = companyRepository.findByIdAndUserProfile_Id(goalDTO.getCompanyID(),userID)
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "CompanyID", goalDTO.getCompanyID()));

            goal.setCompany(company);
        }

        Goal savedGoal = goalRepository.save(goal);

        GoalResponse goalResponse = modelMapper.map(savedGoal, GoalResponse.class);

        if(savedGoal.getCategory() != null){
            goalResponse.setCategoryID(savedGoal.getCategory().getId());
        } else{
            goalResponse.setCompanyID(savedGoal.getCompany().getId());
        }

        return goalResponse;
    }

    @Override
    public GoalResponse getGoal(UUID userID, UUID goalID) {

        Goal foundGoal =  goalRepository.findByIdAndUserProfile_Id(goalID, userID)
               .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalID", goalID));

        BigDecimal spent = BigDecimal.ZERO;
        if (foundGoal.getCategory() != null) {
            spent = transactionRepository.getTotalSpentByCategoryAndDate(userID, foundGoal.getCategory().getId(), foundGoal.getStartDate(), foundGoal.getEndDate());
        } else if (foundGoal.getCompany() != null) {
            spent = transactionRepository.getTotalSpentByCompanyAndDate(userID, foundGoal.getCompany().getId(), foundGoal.getStartDate(), foundGoal.getEndDate());
        }
        foundGoal.setSpendAmount(spent);

        GoalResponse goalResponse = modelMapper.map(foundGoal, GoalResponse.class);

        if(foundGoal.getCategory() != null){
            goalResponse.setCategoryID(foundGoal.getCategory().getId());
        } else{
            goalResponse.setCompanyID(foundGoal.getCompany().getId());
        }

        return goalResponse;
    }

    @Override
    public Page<GoalResponse> getAllGoals(UUID userID, Pageable pageable) {

        Page<Goal> goalPage = goalRepository.findAllByUserProfile_Id(userID, pageable);

        if (goalPage.isEmpty()) return Page.empty();

        List<UUID> goalIds = goalPage.getContent().stream()
                .map(Goal::getId)
                .collect(Collectors.toList());

        List<GoalRepository.GoalSpendProjection> spends = goalRepository.getSpendAmountsForGoals(goalIds);
        Map<UUID, BigDecimal> spendMap = spends.stream()
                .collect(Collectors.toMap(
                        GoalRepository.GoalSpendProjection::getGoalId,
                        GoalRepository.GoalSpendProjection::getSpendAmount
                ));
        
        return goalPage.map(goal -> {
            goal.setSpendAmount(spendMap.getOrDefault(goal.getId(), BigDecimal.ZERO));

            GoalResponse goalResponse = modelMapper.map(goal, GoalResponse.class);
            if(goal.getCategory() != null) {
                goalResponse.setCategoryID(goal.getCategory().getId());
            } else {
                goalResponse.setCompanyID(goal.getCompany().getId());
            }
            return goalResponse;
        });
    }

    @Override
    public GoalResponse updateGoal(UUID userID, UUID goalID, GoalDTO goalDTO) {

        Goal foundGoal =  goalRepository.findByIdAndUserProfile_Id(goalID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalID", goalID));


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
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", goalDTO.getCategoryID()));

            foundGoal.setCategory(category);
            foundGoal.setCompany(null);
        }

        if(goalDTO.getCompanyID() != null && !goalDTO.getCompanyID().equals(foundGoal.getCompany().getId())) {
            Company company = companyRepository.findByIdAndUserProfile_Id(goalDTO.getCompanyID(),userID)
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "CompanyID", goalDTO.getCompanyID()));

            foundGoal.setCompany(company);
            foundGoal.setCategory(null);
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

        if(savedGoal.getCategory() != null){
            goalResponse.setCategoryID(savedGoal.getCategory().getId());
        } else{
            goalResponse.setCompanyID(savedGoal.getCompany().getId());
        }

        return goalResponse;
    }

    @Override
    public GoalStatusResponse getGoalsStats(UUID userID) {
        List<Goal> userGoals = goalRepository.findAllByUserProfile_Id(userID);

        userGoals.forEach(goal -> {
            BigDecimal spent = BigDecimal.ZERO;
            if (goal.getCategory() != null) {
                spent = transactionRepository.getTotalSpentByCategoryAndDate(userID, goal.getCategory().getId(), goal.getStartDate(), goal.getEndDate());
            } else if (goal.getCompany() != null) {
                spent = transactionRepository.getTotalSpentByCompanyAndDate(userID, goal.getCompany().getId(), goal.getStartDate(), goal.getEndDate());
            }
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
    public List<String> getGoalsStatus(UUID userID) {

        return Arrays.stream(GoalStatus.values())
                .map(GoalStatus::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public Page<GoalResponse> getGoalsWithFilters(UUID userID, GoalFilterDTO filter, Pageable pageable) {
        if(filter.orderBy() != null && !filter.orderBy().isBlank()) {
            Sort.Direction direction = (filter.direction() != null && filter.direction().equalsIgnoreCase("desc"))
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;


            String sortBy = filter.orderBy();

            if (sortBy.equalsIgnoreCase("data")) sortBy = "transactionDate";
            if (sortBy.equalsIgnoreCase("valor")) sortBy = "amount";

            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
        }

        Specification<Goal> spec = GoalFilter.filter(userID, filter);

        Page<Goal> goalPage = goalRepository.findAll(spec, pageable);

        return goalPage.map(
                goal -> {
                    BigDecimal spent = BigDecimal.ZERO;
                    if (goal.getCategory() != null) {
                        spent = transactionRepository.getTotalSpentByCategoryAndDate(userID, goal.getCategory().getId(), goal.getStartDate(), goal.getEndDate());
                    } else if (goal.getCompany() != null) {
                        spent = transactionRepository.getTotalSpentByCompanyAndDate(userID, goal.getCompany().getId(), goal.getStartDate(), goal.getEndDate());
                    }
                    goal.setSpendAmount(spent);

                    GoalResponse goalResponse = modelMapper.map(goal, GoalResponse.class);

                    if(goal.getCategory() != null){
                        goalResponse.setCategoryID(goal.getCategory().getId());
                    } else{
                        goalResponse.setCompanyID(goal.getCompany().getId());
                    }

                    return goalResponse;
                });
    }


    @Override
    public GoalResponse deleteGoal(UUID userID, UUID goalID) {

        Goal deletedGoal =  goalRepository.findByIdAndUserProfile_Id(goalID, userID)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "goalID", goalID));

        goalRepository.delete(deletedGoal);

        return modelMapper.map(deletedGoal, GoalResponse.class);
    }
}
