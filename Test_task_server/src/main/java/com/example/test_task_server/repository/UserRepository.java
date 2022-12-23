package com.example.test_task_server.repository;

import com.example.test_task_server.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Balance, Long> {

//    Optional<Balance> findById(Long id);
}
