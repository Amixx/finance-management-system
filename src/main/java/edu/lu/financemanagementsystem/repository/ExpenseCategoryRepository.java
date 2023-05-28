package edu.lu.financemanagementsystem.repository;

import edu.lu.financemanagementsystem.model.ExpenseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    Page<ExpenseCategory> findAllByAuthorId(Long authorId, Pageable pageable);

    List<ExpenseCategory> findAllByAuthorId(Long authorId);

    List<ExpenseCategory> findAllByAuthorIdIsNull();

    Optional<ExpenseCategory> findByTitle(String title);
}
