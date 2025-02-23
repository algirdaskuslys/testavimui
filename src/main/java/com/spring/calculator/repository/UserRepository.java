package com.spring.calculator.repository;

import com.spring.calculator.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.calculationsList WHERE u.id = :id")
    User findByIdWithCalculations(@Param("id") int id);

    @Query("SELECT n FROM User n WHERE n.email = :email")
    User findByEmail(String email);

    User findByUsername(String username);
}
