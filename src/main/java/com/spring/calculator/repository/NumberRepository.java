package com.spring.calculator.repository;

import com.spring.calculator.model.Number;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberRepository extends JpaRepository<Number, Integer> {

    @Query("select n from Number n where n.id = :id")
    Number findById(int id);
}
