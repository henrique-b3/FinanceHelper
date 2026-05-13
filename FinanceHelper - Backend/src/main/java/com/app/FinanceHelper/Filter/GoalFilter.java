package com.app.FinanceHelper.filter;

import com.app.FinanceHelper.model.Goal;
import com.app.FinanceHelper.payload.dto.GoalFilterDTO;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoalFilter {

    public static Specification<Goal> filter(UUID userID, GoalFilterDTO filter) {

        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("userProfile").get("id"),userID));

            if(filter.name() != null && !filter.name().isBlank()){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filter.name().toLowerCase() + "%"
                ));
            }

            if(filter.status() != null && !filter.status().isBlank()){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("status")),
                        "%" + filter.status().toLowerCase() + "%"
                ));
            }

            if(filter.categoryID() != null){
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), filter.categoryID()));
            }

            if(filter.companyID() != null){
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), filter.companyID()));
            }

            if (filter.startDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), filter.startDate()));
            }

            if (filter.endDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), filter.endDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
