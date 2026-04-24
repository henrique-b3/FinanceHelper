package com.app.FinanceHelper.Filter;

import com.app.FinanceHelper.model.Transaction;
import com.app.FinanceHelper.payload.dto.TransactionFilterDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionFilter {

    public static Specification<Transaction> filter(UUID userID, TransactionFilterDTO filter){
        return(root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("userProfile").get("id"), userID));

            if(filter.description() != null && !filter.description().isBlank()){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + filter.description().toLowerCase() + "%"
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
        };
    }
}
