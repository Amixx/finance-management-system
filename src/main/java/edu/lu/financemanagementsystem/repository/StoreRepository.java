package edu.lu.financemanagementsystem.repository;

import edu.lu.financemanagementsystem.model.Store;
import edu.lu.financemanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);
}
