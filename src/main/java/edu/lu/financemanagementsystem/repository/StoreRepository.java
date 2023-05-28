package edu.lu.financemanagementsystem.repository;

import edu.lu.financemanagementsystem.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);

    Page<Store> findAllByUserId(Long userId, Pageable pageable);

    List<Store> findAllByUserId(Long userId);
}
