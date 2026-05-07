package com.example.API_Aggregator.client;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface ApiClient {
    String getKey();
    Mono<Map<String, Object>> fetch(String userId, String requestId);
}