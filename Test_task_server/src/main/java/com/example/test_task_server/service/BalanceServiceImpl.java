package com.example.test_task_server.service;

import com.example.test_task_server.entity.Balance;
import com.example.test_task_server.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BalanceServiceImpl implements BalanceService{

    private final UserRepository userRepository;

    public BalanceServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    @Cacheable(value = "balance", key = "#id")
    public Optional<Long> getBalance(Long id) {
        return Optional.ofNullable(userRepository.findById(id).orElse(new Balance()).getBalance());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @CachePut(value = "balance", key = "#id")
        public Optional<Long> changeBalance(Long id, Long amount) {
            Balance balance = userRepository.findById(id).orElse(new Balance());
        balance.addAmount(amount);
        return Optional.of(userRepository.save(balance).getBalance());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @CacheEvict(value = "balance", key = "#id")
    public void deleteBalance(Long id) {
        userRepository.deleteById(id);
    }
}
