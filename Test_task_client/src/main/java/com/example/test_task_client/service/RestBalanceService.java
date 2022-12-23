package com.example.test_task_client.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

@Service
public class RestBalanceService {

    @Value("${client.url}")
    private String url;

    private final RestTemplate restTemplate;

    @Value("${client.threadCount}")
    private Integer threadCount;
    @Value("${client.readQuota}")
    private Double readQuota;
    @Value("${client.writeQuota}")
    private Double writeQuota;
    @Value("${client.readIdList}")
    private List<Long> readIdList;
    @Value("${client.writeIdList}")
    private List<Long> writeIdList;


    public RestBalanceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void test() {

        Stream<Void> stream = Stream.empty();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < threadCount; i++) {
            new Thread (() -> {
                while (true) {
                // вероятность вызова метода getBalance
                double readProbability = (double)readQuota/(double)(readQuota+writeQuota);

                if (ThreadLocalRandom.current().nextDouble() < readProbability) {
                    System.out.println(getBalance(url, readIdList.get(ThreadLocalRandom.current().nextInt(0, readIdList.size()))));
                } else {
                    changeBalance(url, writeIdList.get(ThreadLocalRandom.current().nextInt(0, writeIdList.size())), 1L);
                }
                }

            }).start();
        }
    }



    public Long getBalance(String url, long id) {
        ResponseEntity<String> exchange = restTemplate.getForEntity(url + id, String.class);
        return Long.parseLong(Objects.requireNonNull(exchange.getBody()));
    }

    public void changeBalance(String url, long id, long amount) {
        restTemplate.exchange(url + id + "/" + amount, HttpMethod.PUT, HttpEntity.EMPTY, Void.class
        );
    }
}
