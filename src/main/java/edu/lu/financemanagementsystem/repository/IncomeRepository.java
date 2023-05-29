package edu.lu.financemanagementsystem.repository;

import edu.lu.financemanagementsystem.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    Page<Income> findAllByUserId(Long userId, Pageable pageable);
}
