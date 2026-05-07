package com.example.API_Aggregator.service;

import com.example.API_Aggregator.client.ApiClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApiClientRegistry {

    private final Map<String, ApiClient> clientMap;

    public ApiClientRegistry(List<ApiClient> clients) {
        this.clientMap = clients.stream()
                .collect(Collectors.toMap(ApiClient::getKey, c -> c));
    }

    public Map<String, ApiClient> getClientMap() {
        return clientMap;
    }
}