package com.weathermap.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ApiKeyRateLimiterService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Value("${APP_RATE_LIMIT:#{5}}")
    private int rateLimit;

    @Value("${APP_RATE_DURATION_IN_MS:#{60000}}")
    private long rateDuration;

    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        //Bandwidth limit = Bandwidth.classic(10, Refill.greedy(5, Duration.ofMinutes(60)));
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(rateLimit, Duration.ofMillis(rateDuration)));
        return Bucket.builder().addLimit(limit).build();
    }
}