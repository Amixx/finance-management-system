package edu.lu.financemanagementsystem.repository;

import edu.lu.financemanagementsystem.model.IncomeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    Page<IncomeCategory> findAllByAuthorId(Long authorId, Pageable pageable);

    List<IncomeCategory> findAllByAuthorId(Long authorId);

    List<IncomeCategory> findAllByAuthorIdIsNull();

    Optional<IncomeCategory> findByTitle(String title);
}
