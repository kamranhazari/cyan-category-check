package com.cyanapp.api.persist.repository;

import com.cyanapp.api.enums.CategoryType;
import com.cyanapp.api.persist.entity.DomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DomainRepository extends JpaRepository<DomainEntity, Long> {
    Optional<DomainEntity> findByDomain(String domain);

    long countByCategoryAndBlocked(CategoryType category, boolean blocked);

    long countByBlocked(boolean blocked);

    @Query("SELECT COUNT(d) FROM DomainEntity d WHERE d.blocked = :blocked AND d.category != :excludedCategory")
    long countExcludedCategoryAndBlocked(CategoryType excludedCategory, boolean blocked);

}
