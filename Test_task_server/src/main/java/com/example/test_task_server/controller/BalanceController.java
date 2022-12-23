package com.example.test_task_server.controller;

import com.example.test_task_server.service.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/balance")
@Slf4j
public class BalanceController {

    private final BalanceService balanceService;

    private AtomicLong countCallingMethodGetBalanceById = new AtomicLong();
    private AtomicLong countCallingMethodChangeBalance = new AtomicLong();

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Long> getBalanceById(@PathVariable Long id) {
        ResponseEntity<Long> longResponseEntity = balanceService.getBalance(id)
                .map(balance -> new ResponseEntity<>(balance, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        countCallingMethodGetBalanceById.incrementAndGet();
        return longResponseEntity;
    }

    @PutMapping("/{id}/{amount}")
    public ResponseEntity<Long> changeBalance(@PathVariable Long id, @PathVariable Long amount) {
        balanceService.changeBalance(id, amount);
        countCallingMethodChangeBalance.incrementAndGet();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Scheduled(fixedRate = 1000)
    public void countingMethodCalls() {
        log.info("Number of method calls per unit time getBalanceById(): "+ countCallingMethodGetBalanceById);
        log.info("Number of method calls per unit time changeBalance(): "+ countCallingMethodChangeBalance);
        countCallingMethodGetBalanceById.set(0);
        countCallingMethodChangeBalance.set(0);
    }

}
