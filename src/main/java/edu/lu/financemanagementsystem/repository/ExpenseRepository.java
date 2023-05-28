package edu.lu.financemanagementsystem.repository;

import edu.lu.financemanagementsystem.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findAllByUserId(Long userId, Pageable pageable);
}
