package com.app.FinanceHelper.repository;

import com.app.FinanceHelper.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByNameAndUserProfile_Id(String name, UUID userID);

    Optional<Company> findByIdAndUserProfile_Id(UUID companyID, UUID userID);

    Set<Company> findAllByUserProfile_Id(UUID userID);

    List<Company> findByNameStartingWithIgnoreCaseAndUserProfile_Id(String companyName, UUID userID);
}
