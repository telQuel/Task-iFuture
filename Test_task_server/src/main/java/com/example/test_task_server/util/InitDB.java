package com.example.test_task_server.util;

import com.example.test_task_server.entity.Balance;
import com.example.test_task_server.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        for (long i = 1; i < 1000; i++) {
            userRepository.save(new Balance(i, 1L));
        }

    }
}
